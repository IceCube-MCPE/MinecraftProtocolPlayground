package mc.icecube.framework.utils;

public class BigEndian {
    // to big endian
    public static byte[] toBE(long n, int c) {
        byte[] result = new byte[c];
        for (int i = 0; i < c; i++) {
            result[c - i - 1] = (byte) ((n >> (8 * i)) & 0xFF);
        }
        return result;
    }

    // from big endian
    public static long fromBE(byte[] b) {
        long result = 0;
        for (int i = 0; i < b.length; i++) {
            result |= (b[i] & 0xFFL) << (8 * (b.length - i - 1));
        }
        return result;
    }
}
