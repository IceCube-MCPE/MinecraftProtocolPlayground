package mc.icecube.framework.raknet.discovery;

import java.util.Arrays;

public class ConnectionRequestAccepted extends Packet {

    public long id;
    public byte[] buffer;
    public String clientAddress;
    public int systemIndex;
    public String[] systemAdresses;
    public long pingTime;
    public long pongTime;

    public ConnectionRequestAccepted(long id, byte[] buffer, String clientAddress, int systemIndex, String[] systemAdresses, long pingTime, long pongTime) {
        this.id = id;
        this.buffer = buffer;
        this.clientAddress = clientAddress;
        this.systemIndex = systemIndex;
        this.systemAdresses = systemAdresses;
        this.pingTime = pingTime;
        this.pongTime = pongTime;
    }

    @Override
    public String toString() {
        return "ConnectionRequestAccepted{" +
                "id=" + id +
                ", clientAddress='" + clientAddress + '\'' +
                ", systemIndex=" + systemIndex +
                ", systemAdresses=" + Arrays.toString(systemAdresses) +
                ", pingTime=" + pingTime +
                ", pongTime=" + pongTime +
                '}';
    }
}