package net.algelier.servermanagement.client.remote;

import javax.management.MBeanInfo;
import javax.management.ObjectName;

public class RemoteService {

    private ObjectName objectName;
    private MBeanInfo mBeanInfo;
    private String name;

    public RemoteService(ObjectName objectName, MBeanInfo mBeanInfo) {
        this.objectName = objectName;
        setmBeanInfo(mBeanInfo);
    }

    public ObjectName getObjectName() {
        return objectName;
    }

    public void setObjectName(ObjectName objectName) {
        this.objectName = objectName;
    }

    public MBeanInfo getmBeanInfo() {
        return mBeanInfo;
    }

    public void setmBeanInfo(MBeanInfo mBeanInfo) {
        this.mBeanInfo = mBeanInfo;
        String[] split = mBeanInfo.getClassName().split("\\.");
        name = split[split.length -1];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
