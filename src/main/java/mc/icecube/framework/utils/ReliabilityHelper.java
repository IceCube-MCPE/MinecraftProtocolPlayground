package mc.icecube.framework.utils;


// Just helpful class for constants in Frame
public class ReliabilityHelper {

    public static final byte UNRELIABLE = 0x00;
    public static final byte UNRELIABLE_SEQUENCED = 0x01;
    public static final byte RELIABLE = 0x02;
    public static final byte RELIABLE_ORDERED = 0x03;
    public static final byte RELIABLE_SEQUENCED = 0x04;
    public static final byte UNRELIABLE_WITH_ACK_RECEIPT = 0x05;
    public static final byte RELIABLE_WITH_ACK_RECEIPT = 0x06;
    public static final byte RELIABLE_ORDERED_WITH_ACK_RECEIPT = 0x07;

    public static boolean isReliable(int reliability) {
        return reliability == RELIABLE ||
                reliability == RELIABLE_ORDERED ||
                reliability == RELIABLE_SEQUENCED ||
                reliability == RELIABLE_WITH_ACK_RECEIPT ||
                reliability == RELIABLE_ORDERED_WITH_ACK_RECEIPT;
    }

    public static boolean isSequenced(int reliability) {
        return reliability == UNRELIABLE_SEQUENCED ||
                reliability == RELIABLE_SEQUENCED;
    }

    public static boolean isOrdered(int reliability) {
        return reliability == UNRELIABLE_SEQUENCED ||
                reliability == RELIABLE_ORDERED ||
                reliability == RELIABLE_SEQUENCED ||
                reliability == RELIABLE_ORDERED_WITH_ACK_RECEIPT;
    }
}