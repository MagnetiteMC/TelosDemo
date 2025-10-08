package dev.magnetite.telosdemo

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import net.kyori.adventure.key.Key
import org.bukkit.Bukkit
import org.bukkit.Registry
import org.bukkit.enchantments.Enchantment


class RegenTask() : Runnable {
    val enchantmentRegistry: Registry<Enchantment> =
        RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)

    // Get the custom Regeneration enchantment
    val enchantment: Enchantment = enchantmentRegistry.getOrThrow(TypedKey.create(
            RegistryKey.ENCHANTMENT, Key.key("telosdemo:regeneration")
    ))

    // Get each player with the regeneration enchant, and heal them .5 * level
    override fun run() {
        Bukkit.getOnlinePlayers()
            .filter { player -> player.inventory.chestplate?.containsEnchantment(enchantment) == true }
            .forEach { player ->
                player.heal(.5 * player.inventory.chestplate!!.getEnchantmentLevel(enchantment))
            }
    }
}
