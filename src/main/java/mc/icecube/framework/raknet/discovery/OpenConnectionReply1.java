package mc.icecube.framework.raknet.discovery;

public class OpenConnectionReply1 extends Packet {

    public long id;
    public byte[] buffer;
    public byte[] magic;
    public Long serverGUID;
    public boolean security;
    public int cookie;
    public int MTU;

    public OpenConnectionReply1(long id, byte[] buffer, byte[] magic, Long serverGUID, boolean security, int cookie, int MTU) {
        this.id = id;
        this.buffer = buffer;
        this.magic = magic;
        this.serverGUID = serverGUID;
        this.security = security;
        this.cookie = cookie;
        this.MTU = MTU;
    }

    @Override
    public String toString() {
        return "OpenConnectionReply1{" +
                "id=" + id +
                ", serverGUID=" + serverGUID +
                ", security=" + security +
                ", MTU=" + MTU +
                '}';
    }
}