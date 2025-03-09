package mc.icecube.framework.raknet.discovery;

public class ConnectionBanned extends Packet {
    private long id;
    private byte[] buffer;
    private byte[] magic;
    private long serverGUID;

    public ConnectionBanned(long id, byte[] buffer, byte[] magic, long serverGUID) {
        this.id = id;
        this.buffer = buffer;
        this.magic = magic;
        this.serverGUID = serverGUID;
    }

    @Override
    public String toString() {
        return "ConnectionBanned{" +
                "serverGUID=" + serverGUID +
                ", id=" + id +
                '}';
    }
}