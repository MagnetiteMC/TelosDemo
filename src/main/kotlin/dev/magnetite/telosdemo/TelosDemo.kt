package dev.magnetite.telosdemo

import org.bukkit.plugin.java.JavaPlugin

class TelosDemo : JavaPlugin() {
    override fun onEnable() {
        registerTasks()
    }

    // A little overkill to have a function for one line, but it's a demo
    fun registerTasks() {
        // Task for regenerating player health with the "Regeneration Enchant"
        server.scheduler.runTaskTimer(this, RegenTask(), 0, 20)
    }
}
