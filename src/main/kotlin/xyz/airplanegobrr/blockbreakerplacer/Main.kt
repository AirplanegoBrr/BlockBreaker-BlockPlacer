package xyz.airplanegobrr.blockbreakerplacer

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import xyz.airplanegobrr.blockbreakerplacer.onBlockDispense

class Main: JavaPlugin() {

    val blockBreakerCommands = arrayOf("bb", "blockbreaker", "breaker")
    val blockPlacerCommands = arrayOf("bp", "blockplacer", "placer")

    // TODO: The current way its setup isn't that good
    // We should make a "claim" command that will claim a block
    // Then a player can set it as a breaker or a placer
    // Data will be stored under each player
    // EG: UUID.World-X,Y,Z (each player will have its own file)
    // then this will have a "type" and a "enable" flag

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            var player = sender;
            if (!player.isOp() && !player.hasPermission("blockbreakerblockplacer.use")) {
                player.sendMessage("No perm!")
                // TODO: Make it log to console if log mode is on
                return true;
            }
            var b = player.getTargetBlock(null, 5)
            var loc = b.location

            val xyzName: String = "${loc.world?.name}${loc.blockX},${loc.blockY},${loc.blockZ}"

            if (blockBreakerCommands.contains(command.name)) {
                // Code to be executed if the condition is true
                var isAlready = config.getBoolean("breakers.${xyzName}")
                if (isAlready) {
                    config.set("breakers.${xyzName}", null)
                    player.sendMessage("Disabled block breaker at $xyzName")
                } else {
                    config.set("breakers.${xyzName}", true)
                    player.sendMessage("Enabled block breaker at $xyzName")
                }
                saveConfig()
                return true
            } else if (blockPlacerCommands.contains(command.name)) {
                var isAlready = config.getBoolean("placers.${xyzName}")
                if (isAlready) {
                    config.set("placers.${xyzName}", null)
                    player.sendMessage("Disabled block placer at $xyzName")
                } else {
                    config.set("placers.${xyzName}", true)
                    player.sendMessage("Enabled block placer at $xyzName")
                }
                saveConfig()
                return true
            }
        }
        return false; // Return false by default
    }

    override fun onEnable() {
        logger.info("Plugin loaded!")
        config.addDefault("debug", false);
        config.options().copyDefaults(true);
        saveConfig();

        val onBlockDispense = onBlockDispense(this)
        server.pluginManager.registerEvents(onBlockDispense, this)
    }

    override fun onDisable() {
        logger.info("Unloaded!")
    }
}