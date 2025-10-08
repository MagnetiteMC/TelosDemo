@file:Suppress("UnstableApiUsage")

package dev.magnetite.telosdemo

import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import io.papermc.paper.registry.data.EnchantmentRegistryEntry
import io.papermc.paper.registry.event.RegistryEvents
import io.papermc.paper.registry.keys.EnchantmentKeys
import io.papermc.paper.registry.keys.tags.EnchantmentTagKeys
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlotGroup
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.function.Consumer


class TelosDemoBootstrap : PluginBootstrap {
    private val regenEnchant: TypedKey<Enchantment> = EnchantmentKeys.create(Key.key("telosdemo:regeneration"))

    override fun bootstrap(context: BootstrapContext) {
        val manager = context.lifecycleManager
        // Register the enchant
        context.lifecycleManager.registerEventHandler(
            RegistryEvents.ENCHANTMENT.compose()
                .newHandler(LifecycleEventHandler { event ->
                    val maxLevel = getConfig(context).getInt("max-regen-level")

                    event.registry().register(
                        EnchantmentKeys.create(regenEnchant),
                        Consumer { b ->
                            b.description(Component.text("Regeneration"))
                                .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.CHEST_ARMOR))
                                .anvilCost(1)
                                .maxLevel(maxLevel)
                                .weight(10) // Let's make it pretty high but "realistic"
                                .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                                .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(3, 1))
                                .activeSlots(EquipmentSlotGroup.CHEST)
                        }
                    )
                })
        )

        // Give it the "tradeable" tag so it shows up in villager trades
        manager.registerEventHandler(
            LifecycleEvents.TAGS.postFlatten(RegistryKey.ENCHANTMENT), LifecycleEventHandler { event ->
                val registrar = event.registrar()
                registrar.addToTag(EnchantmentTagKeys.TRADEABLE, mutableSetOf(regenEnchant))
        })
    }

    // We cannot use the standard method due to the bootstrap running before onEnable
    private fun getConfig(context: BootstrapContext): YamlConfiguration {
        val pluginDataFolder = context.dataDirectory.toFile()
        val configFile = File(pluginDataFolder, "config.yml")

        // Ensure the data folder and file exist

        if (!pluginDataFolder.exists()) {
            pluginDataFolder.mkdirs()
        }

        if (!configFile.exists()) {
            try {
                context.logger.info("creating config.yml")
                val inputStream = TelosDemoBootstrap::class.java.getResourceAsStream("/config.yml")

                if (inputStream != null) {
                    inputStream.use { stream ->
                        Files.copy(stream, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                    }
                    context.logger.info("Default config.yml copied successfully.")
                } else {
                    context.logger.error("CRITICAL: 'config.yml' resource not found in plugin JAR.")
                }
            } catch (e: IOException) {
                context.logger.error("Failed to copy default config.yml: ${e.message}")
            }
        }

        return YamlConfiguration.loadConfiguration(configFile)
    }
}