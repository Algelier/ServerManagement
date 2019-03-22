package net.algelier.servermanagement.client.docker;

import net.algelier.servermanagement.ServerManagement;

import java.io.File;

public class DockerContainer {

    private String name;
    private String id;
    private String image;

    private String[] command;

    private int port;
    private long allowedRam;

    private File souce;

    private DockerAPI dockerAPI;

    public DockerContainer(String name, File source, String image, String[] command, int port, String allowedRam) {
        this.name = name;
        this.image = "frolvlad/alpine-oraclejdk8";;
        this.command = command;
        this.port = port;
        this.souce = source;

        int coef = allowedRam.endsWith("M") ? 1024*1024 : 1024*1024*1024;
        this.allowedRam = Long.valueOf(allowedRam.substring(0, allowedRam.length() -1)) * coef;

        dockerAPI = ServerManagement.getInstance().getAsClient().getDockerAPI();
    }

    public String createContainer() {
        dockerAPI.deleteContainerWithName(name);
        this.id = dockerAPI.createContainer(this);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dockerAPI.startContainer(id);
        return this.id;
    }

    public void stopContainer() {
        dockerAPI.stopContainer(id);
    }

    public void killContainer() {
        dockerAPI.killContainer(id);
    }

    public void removeContainer() {
        try {
            killContainer();
        } catch (Exception e) {}

        dockerAPI.removeContainer(id);
    }

    public boolean isRunning() {
        return dockerAPI.isRunning(id);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public File getSouce() {
        return souce;
    }

    public int getPort() {
        return port;
    }

    public String[] getCommand() {
        return command;
    }

    public long getAllowedRam() {
        return allowedRam;
    }

    public String getImage() {
        return image;
    }

    public File getSource() {
        return souce;
    }
}
