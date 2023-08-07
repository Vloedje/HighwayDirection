package nl.vloedje.highwaydirection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HighwayDirection extends JavaPlugin {

    private double centerX;
    private double centerZ;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        centerX = getConfig().getDouble("center.x");
        centerZ = getConfig().getDouble("center.y");
        getLogger().info("HighwayDirection has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("HighwayDirection has been disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MiniMessage miniMessage = MiniMessage.miniMessage(); // Create the MiniMessage instance
        if (label.equalsIgnoreCase("highway")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(miniMessage.deserialize("This command can only be used by players."));
                return true;
            }

            Player player = (Player) sender;
            if (!player.hasPermission("highwaydirection.use")) {
                player.sendMessage(miniMessage.deserialize("<gray>[Server] You do not have permission to use this command.</gray>"));
                return true;
            }

            if (player.getWorld().getName().equals("world_nether")) {
                double x = player.getLocation().getX() - centerX;
                double z = player.getLocation().getZ() - centerZ;

                String highwayDirection = getClosestHighwayDirection(x, z);
                player.sendMessage(miniMessage.deserialize("<gray>[Server] You need to build to the " + highwayDirection + " highway.</gray>"));
            } else {
                player.sendMessage(miniMessage.deserialize("<gray>[Server] You must be in the Nether to use this command.</gray>"));
            }
        }

        return true;
    }

    private String getClosestHighwayDirection(double x, double z) {
        double distanceToNorth = z;
        double distanceToSouth = -z;
        double distanceToEast = -x;
        double distanceToWest = x;

        String closestHighway = "<color:#E02443>North</color>";
        double closestDistance = distanceToNorth;

        if (distanceToSouth < closestDistance) {
            closestHighway = "<color:#17BF63>South</color>";
            closestDistance = distanceToSouth;
        }

        if (distanceToEast < closestDistance) {
            closestHighway = "<color:#EBB617>East</color>";
            closestDistance = distanceToEast;
        }

        if (distanceToWest < closestDistance) {
            closestHighway = "<color:#1D72F2>West</color>";
            closestDistance = distanceToWest;
        }

        return closestHighway;
    }
}
