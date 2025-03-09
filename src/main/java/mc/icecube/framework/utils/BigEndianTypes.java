package mc.icecube.framework.utils;

import java.io.DataInputStream;
import java.io.IOException;

import static mc.icecube.framework.utils.BigEndian.fromBE;
import static mc.icecube.framework.utils.ByteStreamHelper.readBytes;


public class BigEndianTypes {
    public static int readShortBE(DataInputStream dataInputStream) throws IOException {
        return (int) fromBE(readBytes(dataInputStream, 2));
    }

    public static int readIntBE(DataInputStream dataInputStream) throws IOException {
        return (int) fromBE(readBytes(dataInputStream, 4));
    }
}
