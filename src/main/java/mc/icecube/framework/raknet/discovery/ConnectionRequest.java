package mc.icecube.framework.raknet.discovery;

public class ConnectionRequest extends Packet {

    public long id;
    public byte[] buffer;
    private long clientGUID;
    private long requestTimestamp;
    private boolean useSecurity;

    public ConnectionRequest(long id, byte[] buffer, long clientGUID, long requestTimestamp, boolean useSecurity) {
        this.id = id;
        this.buffer = buffer;
        this.clientGUID = clientGUID;
        this.requestTimestamp = requestTimestamp;
        this.useSecurity = useSecurity;
    }

    @Override
    public String toString() {
        return "ConnectionRequest{" +
                "id=" + id +
                ", clientGUID=" + clientGUID +
                ", requestTimestamp=" + requestTimestamp +
                ", useSecurity=" + useSecurity +
                '}';
    }
}
