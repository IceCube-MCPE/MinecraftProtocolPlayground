package mc.icecube.framework.raknet.discovery;

public class ConnectedPing extends Packet {
    public long id;
    public long time;
    public ConnectedPing(long id, long time) {
        this.id = id;
        this.time = time;
    }

    @Override
    public String toString() {
        return "ConnectedPing{" +
                "id=" + id +
                ", time=" + time +
                '}';
    }
}
