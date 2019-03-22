package net.algelier.servermanagement.client.docker;

import com.google.gson.*;
import net.algelier.servermanagement.ServerManagement;
import okhttp3.*;

import java.util.Random;

public class DockerAPI {

    private static final Gson GSON = new GsonBuilder().create();
    //private DockerClient docker;
    private String url = "";

    public DockerAPI() {
        //docker = DefaultDockerClient.build().uri("url").build();

    }

    public String createContainer(DockerContainer container) {
        JsonObject object = ServerManagement.getInstance().getAsClient().getDockerConfig();

        ifNotSet(object, "Hostname", "");
        ifNotSet(object, "Domainname", "");
        ifNotSet(object, "User", "");
        ifNotSet(object, "AttachStdin", false);
        ifNotSet(object, "AttachStdout", true);
        ifNotSet(object, "AttachStderr", true);
        ifNotSet(object, "Tty", true);
        ifNotSet(object, "OpenStdin", false);
        ifNotSet(object, "StdinOnce", false);
        ifNotSet(object, "Env", new JsonArray());

        JsonArray command = new JsonArray();
        for (String string : container.getCommand()) {
            command.add(string);
        }

        object.add("Command", command);
        ifNotSet(object, "Entrypoint");

        object.addProperty("Image", container.getImage());
        object.add("Labels", new JsonObject());

        JsonArray mounts = new JsonArray();
        JsonObject volume = new JsonObject();

        volume.addProperty("Source", container.getSouce().getAbsolutePath());
        volume.addProperty("Destination", container.getSouce().getAbsolutePath());
        volume.addProperty("Mode", "rw");
        volume.addProperty("RW", true);

        mounts.add(volume);
        object.add("Mounts", mounts);

        JsonObject volumes = new JsonObject();
        volumes.add(container.getSource().getAbsolutePath(), new JsonObject());

        object.add("Volumes", volumes);
        object.addProperty("WorkingDir", container.getSource().getAbsolutePath());
        object.addProperty("NetworkDisabled", false);
        //r.addProperty("MacAddress", randomMACAddress()); //Don't know how to use

        JsonObject exposedPorts = new JsonObject();
        exposedPorts.add(container.getPort()+ "/tcp", new JsonObject());
        exposedPorts.add(container.getPort()+ "/udp", new JsonObject());

        object.add("ExposedPorts", exposedPorts);
        object.addProperty("StopSignal", "SIGTERM");

        JsonObject hostconfig = new JsonObject();

        JsonArray binds = new JsonArray();
        binds.add(container.getSource().getAbsolutePath() + ":" + container.getSource().getAbsolutePath() + ":rw");

        hostconfig.add("Binds", binds);
        ifNotSet(hostconfig, "Links", new JsonArray());

        ifNotSet(hostconfig, "LxcConf", new JsonObject());
        ifNotSet(hostconfig, "Memory", container.getAllowedRam() * 2);
        ifNotSet(hostconfig, "MemorySwap", (container.getAllowedRam() * 2 ) + container.getAllowedRam() << 2);
        ifNotSet(hostconfig, "MemoryReservation", container.getAllowedRam() * 2);
        ifNotSet(hostconfig, "KernelMemory", 0);
        ifNotSet(hostconfig, "CpuShares", 512);
        ifNotSet(hostconfig, "CpuPeriod", 100000);
        ifNotSet(hostconfig, "CpuQuota" , 800000);
        ifNotSet(hostconfig, "BlkioWeight", 1000);
        ifNotSet(hostconfig, "MemorySwappiness", 80);
        ifNotSet(hostconfig, "OomKillDisable", true);

        JsonObject portBindings = new JsonObject();

        JsonArray hostPorts = new JsonArray();
        JsonObject hostPort = new JsonObject();
        hostPort.addProperty("HostIp", "0.0.0.0");
        hostPort.addProperty("HostPort", ""+container.getPort());
        hostPorts.add(hostPort);

        portBindings.add(container.getPort()+"/tcp", hostPorts);
        portBindings.add(container.getPort()+"/udp", hostPorts);

        hostconfig.add("PortBindings", portBindings);
        ifNotSet(hostconfig, "PublishAllPorts", true);
        ifNotSet(hostconfig, "Privileged", false);
        ifNotSet(hostconfig, "ReadonlyRootfs", false);

        /*JsonArray dns = new JsonArray();
        dns.add("8.8.8.8");
        hostconfig.add("Dns", dns);
        hostconfig.add("DnsOptions", new JsonArray());
        hostconfig.add("DnsSearch", new JsonArray());*/
        //hostconfig.add("ExtraHosts", null);
        //hostconfig.add("VolumesFrom", new JsonArray());

        JsonArray caps = new JsonArray();
        caps.add("ALL");

        ifNotSet(hostconfig, "CapAdd", caps);
        ifNotSet(hostconfig, "CapDrop", new JsonArray());

        JsonObject restartPolicy = new JsonObject();
        restartPolicy.addProperty("Name", "");
        restartPolicy.addProperty("MaximumRetryCount", 0);

        ifNotSet(hostconfig, "RestartPolicy", restartPolicy);
        ifNotSet(hostconfig, "NetworkMode", "host");
        ifNotSet(hostconfig, "Devices", new JsonArray());
        ifNotSet(hostconfig, "Ulimits", new JsonArray());

        JsonObject logConfig = new JsonObject();
        logConfig.addProperty("Type", "json-file");
        logConfig.add("Config", new JsonObject());

        ifNotSet(hostconfig, "LogConfig", logConfig);

        ifNotSet(hostconfig, "SecurityOpt", new JsonArray());
        ifNotSet(hostconfig, "CgroupParent", "");
        ifNotSet(hostconfig, "VolumeDriver", "");

        object.add("HostConfig", hostconfig);

        Response response = sendRequest("/containers/create?name=" + container.getName(), object, "POST");

        if(response.getStatus() == 201) {

            JsonObject data = response.getResponse().getAsJsonObject();
            String id = data.get("Id").getAsString();
            JsonElement warnings1 = data.get("Warnings");
            if(warnings1 != null && warnings1.isJsonArray()) {

                JsonArray warnings = warnings1.getAsJsonArray();
                if(warnings != null && warnings.size() > 0) {

                    for(JsonElement s : warnings) {
                        System.err.print(s);
                    }
                }
            }

            return id;
        }

        return null;
    }

    private void ifNotSet(JsonObject element, String property, String defaut) {
        if(!element.has(property))
            element.addProperty(property, defaut);
    }

    private void ifNotSet(JsonObject element, String property) {
        if(!element.has(property))
            element.add(property, null);
    }

    private void ifNotSet(JsonObject element, String property, Boolean defaut) {
        if(!element.has(property))
            element.addProperty(property, defaut);
    }

    private void ifNotSet(JsonObject element, String property, Number defaut) {
        if(!element.has(property))
            element.addProperty(property, defaut);
    }

    private void ifNotSet(JsonObject element, String property, JsonElement defaut) {
        if(!element.has(property))
            element.add(property, defaut);
    }

    public void deleteContainerWithName(String name) {
        Response response = sendRequest("/container/json?all=1", new JsonObject(), "GET");

        if (response.getStatus() == 200) {

            for (JsonElement object : response.getResponse().getAsJsonArray()) {
                JsonObject container = object.getAsJsonObject();
                String id = container.get("Id").getAsString();

                for (JsonElement obj : container.get("Names").getAsJsonArray()) {
                    if (obj.getAsString().contains(name)) {

                        try {
                            killContainer(id);
                        } catch (Exception e) {}

                        removeContainer(id);
                    }
                }
            }
        }
    }

    public JsonArray listRunningContainers() {
        Response response = sendRequest("/containers/json?all=1&filter=[status=running]", new JsonObject(), "GET");

        if (response.getStatus() == 200 && response.getResponse().isJsonArray()) {
            return response.getResponse().getAsJsonArray();
        }

        return null;
    }

    public boolean startContainer(String id) {
        Response response = sendRequest("/containers/" + id + "/start", new JsonObject(), "POST");
        return response.getStatus() == 204;
    }

    public JsonObject inspectContainer(String id) {
        Response response = sendRequest("/containers/" + id + "/json?=size=0", new JsonObject(), "GET");

        if (response.getStatus() == 200) {
            return response.getResponse().getAsJsonObject();
        }

        return null;
    }

    public boolean isRunning(String id) {
        JsonObject inspect = inspectContainer(id);

        if (inspect != null) {
            String status = inspect.get("State").getAsJsonObject().get("Status").getAsString();
            ServerManagement.getLogger().info("Status: " + status);

            return status.equals("running");
        }
        return false;
    }

    public void stopContainer(String id) {
        sendRequest("/containers/" + id + "/stop?t=0", new JsonObject(), "POST");
    }

    public void killContainer(String id) {
        sendRequest("/containers/" + id + "/kill", new JsonObject(), "POST");
    }

    public void removeContainer(String id) {
        sendRequest("/containers/" + id + "?v=1&force=1", new JsonObject(), "DELETE");
    }

    private String randomMacAddress() {
        Random random = new Random();
        byte[] macAddress = new byte[6];

        random.nextBytes(macAddress);

        macAddress[0] = (byte) (macAddress[0] & (byte) 254);
        StringBuilder builder = new StringBuilder();

        for (byte b : macAddress) {
            if (builder.length() > 0)
                builder.append(":");

            builder.append(String.format("%02x", b));
        }

        return builder.toString();
    }

    public Response sendRequest(String point, JsonElement element, String method) {
        String reqest = GSON.toJson(element);

        OkHttpClient client = new OkHttpClient();

        try {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, reqest);

            Request request;
            if (method.equalsIgnoreCase("get")) {
                request = new Request.Builder().url(this.url + point).get().build();

            } else
                request = new Request.Builder().url(this.url + point).method(method, body).build();

            okhttp3.Response responses = client.newCall(request).execute();

            int statusCode = responses.code();
            JsonElement json = null;
            if (responses.body() != null) {
                try {
                    json = new JsonParser().parse(responses.body().string());
                } catch (Exception e) {
                    json = null;
                }
            }

            return new Response(statusCode, json);

        } catch (Exception e) {
            ServerManagement.getLogger().severe("Error for ");
        }
        return new Response(500, null);
    }

    public class Response {
        private int status;
        private JsonElement response;

        public Response(int status, JsonElement response) {
            this.status = status;
            this.response = response;
        }

        public int getStatus() {
            return status;
        }

        public JsonElement getResponse() {
            return response;
        }
    }

}
