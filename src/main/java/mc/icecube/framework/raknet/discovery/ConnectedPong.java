package mc.icecube.framework.raknet.discovery;

public class ConnectedPong extends Packet {
    public long id;
    public long pingTime, pongTime;
    public ConnectedPong(long id, long pingTime, long pongTime) {
        this.id = id;
        this.pingTime = pingTime;
        this.pongTime = pongTime;
    }

    @Override
    public String toString() {
        return "ConnectedPong{" +
                "id=" + id +
                ", pingTime=" + pingTime +
                ", pongTime=" + pongTime +
                '}';
    }
}