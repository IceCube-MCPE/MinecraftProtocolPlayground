package mc.icecube.framework.raknet.discovery;

public class UnconnectedPing extends Packet {
    public long id;
    public long uptime;
    public UnconnectedPing(long id, long uptime) {
        this.id = id;
        this.uptime = uptime;
    }
    @Override
    public String toString() {
        return "UnconnectedPing{" + "id=" + id + ", uptime=" + uptime + '}';
    }
}