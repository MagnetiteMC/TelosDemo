@file:Suppress("UnstableApiUsage")

package dev.magnetite.telosdemo

import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import io.papermc.paper.registry.data.EnchantmentRegistryEntry
import io.papermc.paper.registry.event.RegistryComposeEvent
import io.papermc.paper.registry.event.RegistryEvents
import io.papermc.paper.registry.keys.EnchantmentKeys
import io.papermc.paper.registry.keys.tags.EnchantmentTagKeys
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlotGroup
import java.util.function.Consumer


class TelosDemoBootstrap : PluginBootstrap {
    private val CUSTOM_POINTY_ENCHANT: TypedKey<Enchantment> = EnchantmentKeys.create(Key.key("telosdemo:regeneration"))
    override fun bootstrap(context: BootstrapContext) {
        println("here")
        val manager = context.lifecycleManager
                context.lifecycleManager.registerEventHandler(
                    RegistryEvents.ENCHANTMENT.compose()
                        .newHandler(LifecycleEventHandler { event: RegistryComposeEvent<Enchantment, EnchantmentRegistryEntry.Builder>? ->
                            event!!.registry().register( // The key of the registry
                                // Plugins should use their own namespace instead of minecraft or papermc
                                EnchantmentKeys.create(CUSTOM_POINTY_ENCHANT),
                                Consumer { b: EnchantmentRegistryEntry.Builder? ->
                                    b!!.description(Component.text("Regenerates 0.5 health points per second per level!"))
                                        .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.CHEST_ARMOR))
                                        .anvilCost(1)
                                        .maxLevel(25)
                                        .weight(10)
                                        .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                                        .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(3, 1))
                                        .activeSlots(EquipmentSlotGroup.CHEST)
                                }
                            )
                        })
                )
        manager.registerEventHandler(
            LifecycleEvents.TAGS.postFlatten(
                RegistryKey.ENCHANTMENT
            ), LifecycleEventHandler { event ->
                val registrar = event.registrar()
                registrar.addToTag(
                    EnchantmentTagKeys.TRADEABLE,
                    mutableSetOf(CUSTOM_POINTY_ENCHANT)
                )
            })


    }
}