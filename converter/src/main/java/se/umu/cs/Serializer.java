package se.umu.cs;

public final class Serializer {
    public static ProtoPayload deserialize(byte[] data) throws RuntimeException {
        try {
            return ProtoPayload.parseFrom(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize ProtoPayload", e);
        }
    }

    public static byte[] serialize(ProtoPayload payload) {
        return payload.toByteArray();
    }
}
