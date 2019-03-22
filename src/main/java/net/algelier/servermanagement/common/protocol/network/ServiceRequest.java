package net.algelier.servermanagement.common.protocol.network;

import net.algelier.servermanagement.common.packets.AbstractPacket;

import java.util.UUID;

public class ServiceRequest extends AbstractPacket {

    private UUID requiredId;
    private String target;
    private String name;
    private String operation;
    private Object[] arguments;
    private String[] signature;

    public ServiceRequest(UUID requiredId, String target, String name, String operation, Object[] arguments) {
        this.requiredId = requiredId;
        this.target = target;
        this.name = name;
        this.operation = operation;
        this.arguments = arguments;
    }

    public UUID getRequiredId() {
        return requiredId;
    }

    public void setRequiredId(UUID requiredId) {
        this.requiredId = requiredId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public String[] getSignature() {
        return signature;
    }

    public void setSignature(String[] signature) {
        this.signature = signature;
    }
}
