package mc.icecube.framework.raknet;


import mc.icecube.framework.nethernet.codec.NetherNetCodec;
import mc.icecube.framework.nethernet.discovery.DiscoveryPacket;
import mc.icecube.framework.nethernet.discovery.DiscoveryResponsePacket;
import mc.icecube.framework.nethernet.encryption.DecryptorCipher;
import mc.icecube.framework.nethernet.encryption.Hexer;
import mc.icecube.framework.raknet.codec.RakNetCodec;
import mc.icecube.framework.raknet.discovery.UnconnectedPing;
import mc.icecube.framework.raknet.discovery.UnconnectedPong;
import mc.icecube.framework.utils.RakNetConstans;
import mc.icecube.framework.utils.SimpleLog;

import java.util.Random;

public class Main {
   private static String TAG = "RakNet";
   // This is class just for checking decryption/encription by codec; you can save your own dump of packet via some Proxy and check codec here also you can fix codec if some bugs with weird packets
   public static void main(String[] args) throws Exception {
      RakNetCodec codec = new RakNetCodec();
      // create instance of codec.
      // at the moment ALL RakNet packets based on 1.20.50 minecraft version

      // try to encode ping
      // based on https://wiki.bedrock.dev/servers/raknet.html
      // MCPE;Dedicated Server;527;1.19.1;0;10;13253860892328930865;Bedrock level;Survival;1;19132;19133;
      // unsigned long
      long guid = (long) (new Random().nextDouble() * 0x7FFFFFFFFFFFFFFFL);
      long createdPacketUpTime = System.nanoTime() / 1_000_000;
      byte[] encoded = codec.raknetEncode(new UnconnectedPong(
              guid,
              RakNetConstans.calculateUptime(createdPacketUpTime),
              RakNetConstans.RAKNET_MAGIC_FIRST_CONSTANT,
              "MCPE",
              "Dedicated Server",
              "527",
              "1.19.1",
              "0",
              "10",
              String.valueOf(new Random().nextLong()),
              "Bedrock level",
              "Survival",
              "0",
              "19132",
              "19133"
      ));
      SimpleLog.log(TAG, codec.raknetDecode(encoded).toString());
      // ready bytes for some proxy or just broadcasting
      // same as UnconnectedPing
      byte[] encodedPing = codec.raknetEncode(new UnconnectedPing(guid, RakNetConstans.calculateUptime(createdPacketUpTime)));
      // also ready packet
      // you can send this ping to signal server 19132 or some RakNet external server to get response (UnconnectedPong)
      SimpleLog.log(TAG, codec.raknetDecode(encodedPing).toString());
   }
}