package mc.icecube.framework.nethernet.discovery;

public class DiscoveryMessagePacket extends DiscoveryPacket {
    public long recipient_id;
    public String data;
    public DiscoveryMessagePacket(long id, long recipient_id, String data) {
        this.id = id;
        this.recipient_id = recipient_id;
        this.data = data;
    }
    @Override
    public String toString() {
        return "DiscoveryMessagePacket{" + "recipient_id=" + recipient_id + ", data='" + data + '\'' + ", id=" + id + '}';
    }
}