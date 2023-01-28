package com.airplanegobrr.blockbreakerblockplacer;

import com.airplanegobrr.blockbreakerblockplacer.commands.bbCommand;
import com.airplanegobrr.blockbreakerblockplacer.commands.bpCommand;
import com.airplanegobrr.blockbreakerblockplacer.events.onBlockDispense;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class Blockbreakerblockplacer extends JavaPlugin {

    FileConfiguration config = this.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp() && !player.hasPermission("blockbreakerblockplacer.use")) {
                player.sendMessage("No perm!");
                return true;
            }
            Block b = player.getTargetBlock(null, 5);
            Location loc = b.getLocation();
            String xyzName = (loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());

            // log what command was used
            if (command.getName() == "bb") {
                // blockbreaker

                // check that the block is not already in data.blockBreakers
                config.addDefault("blockBreakers." + xyzName, false);
                boolean con = config.getBoolean("blockBreakers." + xyzName);

                if (con == false) {
                    // add the block to data.blockBreakers
                    config.set("blockBreakers." + xyzName, true);
                    player.sendMessage("Block added to blockBreakers!");
                } else {
                    // remove the block from data.blockBreakers
                    config.set("blockBreakers." + xyzName, false);
                    player.sendMessage("Block removed from blockBreakers!");
                }
                return true;
            }

            if (command.getName() == "bp") {
                // blockplacer
            }
        } else {
            getLogger().info("You must be a player to use this command!");
        }
        return true;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("BlockbreakerBlockplacer has been enabled!");
        config.addDefault("blockBreakers.0,0,0", false);
        config.addDefault("blockPlacers.0,0,0", false);
        config.addDefault("debug", false);
        config.options().copyDefaults(true);
        this.saveConfig();



        getServer().getPluginCommand("blockbreaker").setExecutor(new bbCommand(this));
        getServer().getPluginCommand("bb").setExecutor(new bbCommand(this));
        
        getServer().getPluginCommand("blockplacer").setExecutor(new bpCommand(this));
        getServer().getPluginCommand("bp").setExecutor(new bpCommand(this));

        //block dispense event
        getServer().getPluginManager().registerEvents(new onBlockDispense(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // save config
        this.saveConfig();
    }
}
