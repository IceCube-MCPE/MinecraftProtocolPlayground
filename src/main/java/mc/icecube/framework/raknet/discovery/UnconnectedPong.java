package mc.icecube.framework.raknet.discovery;

public class UnconnectedPong extends Packet {
    public long id;
    public long uptime;
    public byte[] magic;
    public String edition;
    public String serverName;
    public String protocolVersion;
    public String versionNumber;
    public String playerCount;
    public String maxPlayerCount;
    public String levelName;
    public String serverId;
    public String gameType;
    public String gameTypeNumeric;
    public String portv4;
    public String portv6;
    public UnconnectedPong(long id, long uptime, byte[] magic, String edition, String serverName, String protocolVersion, String versionNumber, String playerCount, String maxPlayerCount, String serverId, String levelName, String gameType, String gameTypeNumeric, String portv4, String portv6) {
        this.id = id;
        this.uptime = uptime;
        this.magic = magic;
        this.edition = edition;
        this.serverName = serverName;
        this.protocolVersion = protocolVersion;
        this.versionNumber = versionNumber;
        this.playerCount = playerCount;
        this.maxPlayerCount = maxPlayerCount;
        this.serverId = serverId;
        this.levelName = levelName;
        this.gameType = gameType;
        this.gameTypeNumeric = gameTypeNumeric;
        this.portv4 = portv4;
        this.portv6 = portv6;
    }

    @Override
    public String toString() {
        return "UnconnectedPong{" +
                "id=" + id +
                ", uptime=" + uptime +
                ", edition='" + edition + '\'' +
                ", serverName='" + serverName + '\'' +
                ", protocolVersion='" + protocolVersion + '\'' +
                ", versionNumber='" + versionNumber + '\'' +
                ", playerCount='" + playerCount + '\'' +
                ", maxPlayerCount='" + maxPlayerCount + '\'' +
                ", levelName='" + levelName + '\'' +
                ", serverId='" + serverId + '\'' +
                ", gameType='" + gameType + '\'' +
                ", gameTypeNumeric='" + gameTypeNumeric + '\'' +
                ", portv4='" + portv4 + '\'' +
                ", portv6='" + portv6 + '\'' +
                '}';
    }
}
