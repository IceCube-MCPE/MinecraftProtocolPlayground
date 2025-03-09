package mc.icecube.framework.utils;

public class RakNetConstans {
    public static final byte[] RAKNET_MAGIC_FIRST_CONSTANT = new byte[]{0, -1, -1, 0, -2, -2, -2, -2, -3, -3, -3, -3, 18, 52, 86, 120};


    public static long calculateUptime(long epoch) {
        return System.nanoTime() / 1_000_000 - epoch;
    }
}
