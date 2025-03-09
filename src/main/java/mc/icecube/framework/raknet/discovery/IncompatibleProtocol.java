package mc.icecube.framework.raknet.discovery;

public class IncompatibleProtocol extends Packet {

    private long id;
    private byte[] buffer;
    private int protocol;
    private byte[] magic;
    private long serverGUID;

    public IncompatibleProtocol(long id, byte[] buffer, int protocol, byte[] magic, long serverGUID) {
        this.id = id;
        this.buffer = buffer;
        this.protocol = protocol;
        this.magic = magic;
        this.serverGUID = serverGUID;
    }

    @Override
    public String toString() {
        return "IncompatibleProtocol{" +
                "id=" + id +
                ", protocol=" + protocol +
                ", serverGUID=" + serverGUID +
                '}';
    }
}