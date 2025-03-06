package mc.icecube.framework.nethernet.discovery;

import java.util.Objects;

public class DiscoveryResponsePacket extends DiscoveryPacket {
    public int version;
    public String server_name;
    public String level_name;
    public int game_type;
    public int player_count;
    public int max_player_count;
    public boolean is_editor_world;
    public boolean hardcore; // Added in Version 3 (maybe 1.21+)
    public int transport_layer;

    // Constructor for versions 3 and above (includes hardcore)
    public DiscoveryResponsePacket(long id, int version, String server_name, String level_name, int game_type, int player_count, int max_player_count, boolean is_editor_world, boolean hardcore, int transport_layer) {
        this.id = id;
        this.version = version;
        this.server_name = server_name;
        this.level_name = level_name;
        this.game_type = game_type;
        this.player_count = player_count;
        this.max_player_count = max_player_count;
        this.is_editor_world = is_editor_world;
        this.hardcore = hardcore; // Only relevant for version 3+
        this.transport_layer = transport_layer;
        if(version <= 2) {
            throw new RuntimeException("Bro, NetherNet ResponsePacket have version and hardcore field. Hardcore only implemented at 3+");
        }
    }

    // Constructor for versions less than 3 (excludes hardcore)
    public DiscoveryResponsePacket(long id, int version, String server_name, String level_name, int game_type, int player_count, int max_player_count, boolean is_editor_world, int transport_layer) {
        this.id = id;
        this.version = version;
        this.server_name = server_name;
        this.level_name = level_name;
        this.game_type = game_type;
        this.player_count = player_count;
        this.max_player_count = max_player_count;
        this.is_editor_world = is_editor_world;
        this.hardcore = false; // Default value for versions less than 3
        this.transport_layer = transport_layer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DiscoveryResponsePacket{")
                .append("version=").append(version)
                .append(", server_name='").append(server_name).append('\'')
                .append(", level_name='").append(level_name).append('\'')
                .append(", game_type=").append(game_type)
                .append(", player_count=").append(player_count)
                .append(", max_player_count=").append(max_player_count)
                .append(", is_editor_world=").append(is_editor_world);

        // Only include hardcore in the string representation if version is 3 or higher
        if (version >= 3) {
            sb.append(", hardcore=").append(hardcore);
        }

        sb.append(", transport_layer=").append(transport_layer)
                .append(", id=").append(id)
                .append('}');

        return sb.toString();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Check if the same instance
        if (o == null || getClass() != o.getClass()) return false; // Check if the other object is null or of a different class
        DiscoveryResponsePacket that = (DiscoveryResponsePacket) o;
        // Compare all fields except `id` (if `id` is not part of the equality check)
        return version == that.version &&
                game_type == that.game_type &&
                player_count == that.player_count &&
                max_player_count == that.max_player_count &&
                is_editor_world == that.is_editor_world &&
                transport_layer == that.transport_layer &&
                (version < 3 || hardcore == that.hardcore) && // Only compare hardcore if version >= 3
                Objects.equals(server_name, that.server_name) &&
                Objects.equals(level_name, that.level_name);
    }
    @Override
    public int hashCode() {
        // Generate a hash code based on the fields used in the `equals` method
        return Objects.hash(version, server_name, level_name, game_type, player_count, max_player_count, is_editor_world, transport_layer, version >= 3 ? hardcore : false);
    }
}