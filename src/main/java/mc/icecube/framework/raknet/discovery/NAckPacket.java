package mc.icecube.framework.raknet.discovery;

public class NAckPacket extends Packet {
    private int recordCount;
    private boolean singleSequenceNumber;
    private int startSequenceNumber;
    private int endSequenceNumber;
    private int sequenceNumber;

    public NAckPacket(int recordCount, boolean singleSequenceNumber, int sequenceNumber, int startSequenceNumber, int endSequenceNumber) {
        this.recordCount = recordCount;
        this.singleSequenceNumber = singleSequenceNumber;
        this.sequenceNumber = sequenceNumber;
        this.startSequenceNumber = startSequenceNumber;
        this.endSequenceNumber = endSequenceNumber;
    }

    @Override
    public String toString() {
        return "NAckPacket{" +
                "recordCount=" + recordCount +
                ", singleSequenceNumber=" + singleSequenceNumber +
                ", startSequenceNumber=" + startSequenceNumber +
                ", endSequenceNumber=" + endSequenceNumber +
                ", sequenceNumber=" + sequenceNumber +
                '}';
    }
}