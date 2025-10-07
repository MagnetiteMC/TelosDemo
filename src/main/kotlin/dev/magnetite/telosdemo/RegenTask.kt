package dev.magnetite.telosdemo

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import net.kyori.adventure.key.Key
import org.bukkit.Bukkit
import org.bukkit.Registry
import org.bukkit.enchantments.Enchantment


class RegenTask(private val plugin: TelosDemo) : Runnable {
    val enchantmentRegistry: Registry<Enchantment> = RegistryAccess.registryAccess().getRegistry<Enchantment>(RegistryKey.ENCHANTMENT);
    val enchantment: Enchantment = enchantmentRegistry.getOrThrow(
        TypedKey.create(
            RegistryKey.ENCHANTMENT, Key.key("telosdemo:regeneration")
        )
    )

    override fun run() {
        Bukkit.getOnlinePlayers().filter { player -> player.inventory.chestplate?.containsEnchantment(enchantment) == true }.forEach {
            player -> player.heal(.5) // TODO: based on level
        }
    }
}
