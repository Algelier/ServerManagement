package net.algelier.servermanagement.client.remote;

import net.algelier.servermanagement.ServerManagement;

import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationListener;

class RemoteListener implements NotificationListener  {

    private RemoteControl remoteControl;

    @Override
    public void handleNotification(Notification notification, Object handback) {
        MBeanServerNotification mBeanServerNotification = (MBeanServerNotification) notification;
        if (MBeanServerNotification.REGISTRATION_NOTIFICATION.equals(mBeanServerNotification.getType())) {
            remoteControl.addService(mBeanServerNotification.getMBeanName());
            ServerManagement.getLogger().info("New service: " + mBeanServerNotification.getMBeanName().getCanonicalName());
        } else if (MBeanServerNotification.UNREGISTRATION_NOTIFICATION.equals(mBeanServerNotification.getType())) {
            remoteControl.remoteService(mBeanServerNotification.getMBeanName());
        }
    }

    public RemoteListener(RemoteControl remoteControl) {
        this.remoteControl = remoteControl;
    }
}
