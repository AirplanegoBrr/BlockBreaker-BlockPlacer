package me.airplanegobrr.blockbreakerplacer

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Main: JavaPlugin() {
    companion object {
        val logger = Bukkit.getLogger()
    }

    override fun onEnable() {
        logger.info("Plugin loaded!")
    }

    override fun onDisable() {
        logger.info("Unloaded!")
    }
}