package mc.icecube.framework.raknet.discovery;

import java.util.Arrays;

public class Disconnect extends Packet {
    private long id;
    private byte[] buffer;

    public Disconnect(long id, byte[] buffer) {
        this.id = id;
        this.buffer = buffer;
    }

    @Override
    public String toString() {
        return "Disconnect{" +
                "id=" + id +
                ", buffer=" + Arrays.toString(buffer) +
                '}';
    }
}