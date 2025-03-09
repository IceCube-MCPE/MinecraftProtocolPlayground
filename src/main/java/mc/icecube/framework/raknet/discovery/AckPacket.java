package mc.icecube.framework.raknet.discovery;

public class AckPacket extends Packet {
    private int recordCount;
    private boolean singleSequenceNumber;
    private int startSequenceNumber;
    private int endSequenceNumber;
    private int sequenceNumber;

    public AckPacket(int recordCount, boolean singleSequenceNumber, int sequenceNumber, int startSequenceNumber, int endSequenceNumber) {
        this.recordCount = recordCount;
        this.singleSequenceNumber = singleSequenceNumber;
        this.sequenceNumber = sequenceNumber;
        this.startSequenceNumber = startSequenceNumber;
        this.endSequenceNumber = endSequenceNumber;
    }

    @Override
    public String toString() {
        return "AckPacket{" +
                "recordCount=" + recordCount +
                ", singleSequenceNumber=" + singleSequenceNumber +
                ", sequenceNumber=" + sequenceNumber +
                ", startSequenceNumber=" + startSequenceNumber +
                ", endSequenceNumber=" + endSequenceNumber +
                '}';
    }
}
