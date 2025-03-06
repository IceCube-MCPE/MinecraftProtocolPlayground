package mc.icecube.framework.nethernet;

import mc.icecube.framework.nethernet.codec.NetherNetCodec;
import mc.icecube.framework.nethernet.discovery.DiscoveryPacket;
import mc.icecube.framework.nethernet.discovery.DiscoveryResponsePacket;
import mc.icecube.framework.nethernet.encryption.DecryptorCipher;
import mc.icecube.framework.nethernet.encryption.Hexer;
import mc.icecube.framework.utils.SimpleLog;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Random;

public class Main {
    private static String TAG = "NetherNet";
    private static boolean showKey = true;
    // This is class just for checking decryption/encription by codec; you can save your own dump of packet via some Proxy and check codec here also you can fix codec if some bugs with weird packets
    public static void main(String[] args) throws Exception {
        if(showKey) {
            SimpleLog.log(TAG, "Decryptor Key: " + Hexer.bytesToHex(DecryptorCipher.getKey()));
        }
        // Simple usage for Codec
        // If DiscoveryResponsePacket have version 2, hardcore doesn't matter
        DiscoveryResponsePacket discoveryResponsePacket = new DiscoveryResponsePacket(
                new Random().nextLong(),
                2,
                "Checking",
                "Vilatik Chicken.",
                1,
                1,
                5,
                false,
                1
        );
        byte[] encoded = NetherNetCodec.nethernetEncode(discoveryResponsePacket);
        // Now we have ready encoded payload. Now we have a ready encoded payload. All that's left is to send it as a response to any client that pings 7551
        // Let's try to decode it?
        DiscoveryPacket discoveryResponsePacketDecoded = NetherNetCodec.nethernetDecode(encoded);
        // Codec return DiscoveryPacket, so we need check myself if it needed for us packet
        if(discoveryResponsePacketDecoded instanceof DiscoveryResponsePacket packet) {
            // If it is. We're done
            SimpleLog.log(TAG, packet.toString());
            if(packet.equals(discoveryResponsePacket)) {
                SimpleLog.log(TAG, "DiscoveryResponsePackets are equals. Codec working correctly.");
            }
        } else if (discoveryResponsePacketDecoded == null) {
            SimpleLog.log(TAG, "Packet is NULL. Check Codec for bugs.");
        }
    }
}