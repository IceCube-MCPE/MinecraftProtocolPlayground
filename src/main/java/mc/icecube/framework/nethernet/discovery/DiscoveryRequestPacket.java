package mc.icecube.framework.nethernet.discovery;

public class DiscoveryRequestPacket extends DiscoveryPacket {
    public DiscoveryRequestPacket(long id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "DiscoveryRequestPacket{" + "id=" + id + '}';
    }
}