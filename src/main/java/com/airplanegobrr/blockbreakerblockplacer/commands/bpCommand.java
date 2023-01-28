package com.airplanegobrr.blockbreakerblockplacer.commands;

import com.airplanegobrr.blockbreakerblockplacer.Blockbreakerblockplacer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


public class bpCommand implements CommandExecutor {
    private final Blockbreakerblockplacer main;

    public bpCommand(Blockbreakerblockplacer main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            FileConfiguration config = main.getConfig();
            Player player = (Player) sender;

            //Check perm of the player
            if (!player.isOp() && !player.hasPermission("blockbreakerblockplacer.use")) {
                player.sendMessage(ChatColor.RED + "No perm!");
                return true;
            }

            //Get the block that is 5 blocks in front of the player
            Block b = player.getTargetBlock(null, 5);

            //Add check here to make sure block is dispenser or dropper
            if (b.getType() != Material.DISPENSER) {
                player.sendMessage(ChatColor.RED + "You must be looking at a dispenser!");
                return true;
            }

            //Get the loc of the block we are looking at
            Location loc = b.getLocation();

            //Compile that loc to a string (will add world later!)
            String xyzName = (loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());

            //Set that dealt to be false
            config.addDefault("blockPlacers." + xyzName, false);
            //Get the block info
            boolean con = config.getBoolean("blockPlacers." + xyzName);

            //if the block is false the set it true + the other way around

            //false -> true
            if (con == false) {
                // add the block to data.blockBreakers
                config.set("blockPlacers." + xyzName, true);
                player.sendMessage("Block added to blockPlacers!");
            } else {
                // remove the block from data.blockBreakers
                config.set("blockPlacers." + xyzName, false);
                player.sendMessage("Block removed from blockPlacers!");
            }
        }
        return true;
    }
}
