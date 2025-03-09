package mc.icecube.framework.raknet.discovery;

import java.util.Arrays;

public class LostConnection extends Packet {
    private long id;
    private byte[] buffer;

    public LostConnection(long id, byte[] buffer) {
        this.id = id;
        this.buffer = buffer;
    }

    @Override
    public String toString() {
        return "LostConnection{" +
                "id=" + id +
                ", buffer=" + Arrays.toString(buffer) +
                '}';
    }
}