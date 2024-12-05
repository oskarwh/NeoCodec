package se.umu.cs.pulsar.interfaces;

public interface PulsarAdminCommunication {
    public String createClientTopic(String ip);
    public int removeClientTopic(String topic);
}