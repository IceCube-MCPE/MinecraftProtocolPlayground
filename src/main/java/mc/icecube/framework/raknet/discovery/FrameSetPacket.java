package mc.icecube.framework.raknet.discovery;

import mc.icecube.framework.raknet.framesetpacket.Frame;

import java.util.ArrayList;

public class FrameSetPacket extends Packet {

    public long id;
    public int sequenceNumber;
    public ArrayList<Frame> frames;

    public FrameSetPacket(long id, int sequenceNumber, ArrayList<Frame> frames) {
        this.id = id;
        this.sequenceNumber = sequenceNumber;
        this.frames = frames;
    }

    @Override
    public String toString() {
        return "FrameSetPacket{" +
                "id=" + id +
                ", sequenceNumber=" + sequenceNumber +
                ", frames=" + frames +
                '}';
    }
}