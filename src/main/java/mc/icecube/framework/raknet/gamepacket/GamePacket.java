package mc.icecube.framework.raknet.gamepacket;

import mc.icecube.framework.raknet.discovery.Packet;

import java.util.Arrays;

public class GamePacket extends Packet {
    public long id;
    public byte[] buffer;

    public GamePacket(long id, byte[] buffer) {
        this.id = id;
        this.buffer = buffer;
    }

    @Override
    public String toString() {
        return "GamePacket{" +
                "id=" + id +
                ", buffer=" + Arrays.toString(buffer) +
                '}';
    }
}