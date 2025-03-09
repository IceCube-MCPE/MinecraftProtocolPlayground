package mc.icecube.framework.raknet.discovery;

import java.util.Arrays;

public class UnknownPacket extends Packet {

    public long id;
    public byte[] buffer;

    public UnknownPacket(long id, byte[] buffer) {
        this.id = id;
        this.buffer = buffer;
    }

    @Override
    public String toString() {
        return "UnknownPacket{" +
                "id=" + String.format("0x%02X", id) +
                ", buffer=" + Arrays.toString(buffer) +
                '}';
    }
}