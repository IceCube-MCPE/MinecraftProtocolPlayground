package mc.icecube.framework.raknet.framesetpacket;

import java.nio.ByteBuffer;

public class Frame {

    public int reliability;
    public byte[] buffer;
    public boolean isFragmented;
    public int reliableFrameIndex;
    public int sequencedFrameIndex;
    public int orderedFrameIndex;
    public int orderChannel;
    public int compoundSize;
    public int compoundID;
    public int compoundEntryIndex;
    public ByteBuffer body; //it's just bytes

    public Frame(int reliability, byte[] buffer, boolean isFragmented, int reliableFrameIndex, int sequencedFrameIndex, int orderedFrameIndex, int orderChannel, int compoundSize, int compoundID, int compoundEntryIndex, ByteBuffer body) {
        this.reliability = reliability;
        this.buffer = buffer;
        this.isFragmented = isFragmented;
        this.reliableFrameIndex = reliableFrameIndex;
        this.sequencedFrameIndex = sequencedFrameIndex;
        this.orderedFrameIndex = orderedFrameIndex;
        this.orderChannel = orderChannel;
        this.compoundSize = compoundSize;
        this.compoundID = compoundID;
        this.compoundEntryIndex = compoundEntryIndex;
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Frame{");
        sb.append("reliability=").append(reliability);
        sb.append(", buffer=");
        for (byte b : buffer) {
            sb.append(String.format("0x%02X ", b));
        }
        sb.append(", isFragmented=").append(isFragmented);
        sb.append(", reliableFrameIndex=").append(reliableFrameIndex);
        sb.append(", sequencedFrameIndex=").append(sequencedFrameIndex);
        sb.append(", orderedFrameIndex=").append(orderedFrameIndex);
        sb.append(", orderChannel=").append(orderChannel);
        sb.append(", compoundSize=").append(compoundSize);
        sb.append(", compoundID=").append(compoundID);
        sb.append(", compoundEntryIndex=").append(compoundEntryIndex);
        sb.append(", body=");
        for (byte b : body.array()) {
            sb.append(String.format("0x%02X ", b));
        }
        sb.append('}');
        return sb.toString();
    }
}