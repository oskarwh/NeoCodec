package se.umu.cs.pulsar.interfaces;

public interface PulsarAdminCommunication {
    public String createClientTopic(String ip, String port);
    public int removeClientTopic(String ip, String port);
}