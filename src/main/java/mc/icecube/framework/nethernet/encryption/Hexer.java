package mc.icecube.framework.nethernet.encryption;


public class Hexer {
    // Simple class, yeah?
    public static byte[] hexToBytes(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
    // Метод для конвертации массива байт в строку в формате hex
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = Character.forDigit(v >>> 4, 16);
            hexChars[j * 2 + 1] = Character.forDigit(v & 0x0F, 16);
        }
        return new String(hexChars);
    }
}