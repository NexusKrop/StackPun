package x.nexuskrop.stackpun.util;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;

/**
 * Provides methods to manipulate worlds.
 * @author WithLithum
 */
public final class Worlds {
    private Worlds() {}

    public static World overworld() {
        return Bukkit.getWorld(NamespacedKey.fromString("overworld"));
    }
}