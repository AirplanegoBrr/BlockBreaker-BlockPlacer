package xyz.airplanegobrr.blockbreakerplacer

import org.bukkit.Material
import org.bukkit.block.Dispenser
import org.bukkit.block.data.Directional
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.function.Supplier

class onBlockDispense(main: Main) : Listener {
    private val main: Main

    init {
        this.main = main
        main.logger.info("Event is listening")
    }
    @EventHandler
    fun BlockDispenseEvent(event: BlockDispenseEvent) {
        val block = event.block
        val loc = block.location
        val xyzName: String = "${loc.world?.name}${loc.blockX},${loc.blockY},${loc.blockZ}"
        val item = event.item
        val itemType = item.type

        // main.config.load()
        val debug: Boolean = main.config.getBoolean("debug")
        val bbEnabled: Boolean = main.config.getBoolean("breakers.$xyzName")
        val bpEnabled: Boolean = main.config.getBoolean("placers.$xyzName")

        if (debug) main.logger.info("Checking $xyzName $bbEnabled $bpEnabled $debug")
        // Real stuff

        var noBreak = arrayOf(Material.WATER, Material.LAVA, Material.AIR)

        // blockbreaker
        if (bbEnabled) {
            if (debug) {
                main.logger.info(item.toString())
                main.logger.info(item.type.toString())
                main.logger.info(item.type.name)
                main.logger.info(xyzName)
            }
            if (item.type.toString().contains("PICKAXE")) {
                if (debug) main.logger.info("Pickaxe detected!")

                val bsFace = (block.blockData as Directional).facing
                val bsBlock = block.getRelative(bsFace)
                val bsMaterial = bsBlock.type
                // if the material is not bedrock then break it
                if (bsMaterial != Material.BEDROCK && !noBreak.contains(bsMaterial)) {
                    try {
                        bsBlock.breakNaturally()
                        event.isCancelled = true
                        return
                    } catch (e: Exception) {
                        if (debug) {
                            main.logger.info("Failed break block!")
                            main.logger.warning(e as Supplier<String?>)
                        }
                        return
                    }
                } else {
                    event.isCancelled = true
                }
            }
        }

        var noPlace = arrayOf(Material.WATER, Material.LAVA, Material.AIR)

        // blockplacer
        if (bpEnabled) {
            if (debug) {
                main.logger.info(item.toString())
                main.logger.info(item.type.toString())
                main.logger.info(item.type.name)
                main.logger.info(xyzName)
            }
            if (itemType.isBlock) {
                val bsFace = (block.blockData as Directional).facing
                val bsBlock = block.getRelative(bsFace)
                val bsMaterial = bsBlock.type
                main.logger.info(bsBlock.type.name)
                if (noPlace.contains(bsMaterial)) {
                    main.logger.info("Passed check!")
                    bsBlock.type = itemType
                    event.isCancelled = true
                    val blockUpdated = event.block
                    val dispenserUpdated = blockUpdated.state as Dispenser
                    val itemUpdated = event.item

                    //remove item from dispenser with a BukkitRunnable
                    object : BukkitRunnable() {
                        override fun run() {
                            if (debug) main.logger.info("Removing item from dispenser")

                            dispenserUpdated.inventory.removeItem(itemUpdated)
                        }
                    }.runTaskLater(main, 1)
                } else {
                    event.isCancelled = true
                }
            }
        }
    }
}