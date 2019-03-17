package net.algelier.servermanagement.client.remote;

import net.algelier.servermanagement.ServerManagement;
import net.algelier.servermanagement.client.servers.MinecraftServerC;

import javax.management.*;
import javax.management.relation.MBeanServerNotificationFilter;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class RemoteControl {

    private HashMap<String, RemoteService> services = new HashMap<String, RemoteService>();
    private JMXConnector jmxConnector;
    private MBeanServerConnection mBeanServerConnection;
    private RemoteListener remoteListener;

    private boolean isConnected = false;

    public RemoteControl(MinecraftServerC serverC, String host, int port) {
        remoteListener = new RemoteListener(this);

        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {}

            int i = 0;
            while (!isConnected) {
                if (i > 5) {
                    ServerManagement.getLogger().info("Failed to connect at RMI shutdown: ");//serverC.getServerName());
                    serverC.stopServer();
                    return;
                }

                try {
                    Thread.sleep(1000);
                    JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi");

                    jmxConnector = JMXConnectorFactory.connect(url);
                    mBeanServerConnection = jmxConnector.getMBeanServerConnection();
                    MBeanServerNotificationFilter filter = new MBeanServerNotificationFilter();

                    filter.enableAllObjectNames();
                    mBeanServerConnection.addNotificationListener(MBeanServerDelegate.DELEGATE_NAME, remoteListener, filter, null);
                    isConnected = true;
                } catch (IOException | InstanceNotFoundException | InterruptedException e) {
                    ServerManagement.getLogger().info("Cannot connect to " + host + ":" + port);
                }
                i++;
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {}

            loadAllServices();
        }).start();
    }


    private void loadAllServices() {
        try {
            Set<ObjectInstance> objectInstances = mBeanServerConnection.queryMBeans(null, null);
            for (ObjectInstance objectInstance : objectInstances) {
                addService(objectInstance.getObjectName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean addService(ObjectName name) {
        try {
            MBeanInfo mBeanInfo = mBeanServerConnection.getMBeanInfo(name);
            RemoteService remoteService = new RemoteService(name, mBeanInfo);
            services.put(remoteService.getName(), remoteService);

        } catch (InstanceNotFoundException | ReflectionException | IntrospectionException | IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    boolean remoteService(ObjectName name) {
        Iterator<RemoteService> values = services.values().iterator();

        while (values.hasNext()) {
            RemoteService next = values.next();
            if (next.getObjectName().equals(name)) {
                values.remove();
                break;
            }
        }
        return true;
    }

    public void removeService(String name) {
        services.remove(name);
    }

    public RemoteService getService(String name) {
        return services.get(name);
    }

    public Collection<RemoteService> getServices() {
        return services.values();
    }

    public Object invokeService(RemoteService remoteService, String operation, Object[] args, String[] signatures)
    throws ReflectionException, MBeanException, InstanceNotFoundException, IOException {
        return mBeanServerConnection.invoke(remoteService.getObjectName(), operation, args, signatures);
    }

    public boolean disconnect() {
        try {
            jmxConnector.close();
            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
}


