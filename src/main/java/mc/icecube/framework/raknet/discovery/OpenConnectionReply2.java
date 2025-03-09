package mc.icecube.framework.raknet.discovery;

public class OpenConnectionReply2 extends Packet {

    public long id;
    public byte[] buffer;
    private long serverGUID;
    private String clientAddress;
    private int mtuSize;
    private boolean security;

    public OpenConnectionReply2(long id, byte[] buffer, long serverGUID, String clientAddress, int mtuSize, boolean security) {
        this.id = id;
        this.buffer = buffer;
        this.serverGUID = serverGUID;
        this.clientAddress = clientAddress;
        this.mtuSize = mtuSize;
        this.security = security;
    }

    @Override
    public String toString() {
        return "OpenConnectionReply2{" +
                "id=" + id +
                ", serverGUID=" + serverGUID +
                ", mtuSize=" + mtuSize +
                ", security=" + security +
                ", (Public IPv4) clientAddress='" + clientAddress + '\'' +
                '}';
    }
}
