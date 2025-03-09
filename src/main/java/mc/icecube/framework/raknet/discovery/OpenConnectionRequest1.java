package mc.icecube.framework.raknet.discovery;

public class OpenConnectionRequest1 extends Packet {
    public long id;
    public int protocolVersion, mtu;
    public byte[] magic;
    public byte[] buffer;
    public OpenConnectionRequest1(long id, byte[] buffer, byte[] magic, int protocolVersion, int mtu) {
        this.id = id;
        this.buffer = buffer;
        this.magic = magic;
        this.protocolVersion = protocolVersion;
        this.mtu = mtu;
    }

    @Override
    public String toString() {
        return "OpenConnectionRequest1{" +
                "id=" + id +
                ", protocolVersion=" + protocolVersion +
                ", mtu=" + mtu +
                '}';
    }
}
