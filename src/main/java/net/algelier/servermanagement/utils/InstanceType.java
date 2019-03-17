package net.algelier.servermanagement.utils;

public enum InstanceType {

    CLIENT("Client"),
    SERVER("Server");

    private final String text;

    InstanceType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
