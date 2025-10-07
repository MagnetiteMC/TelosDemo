package dev.magnetite.telosdemo

import org.bukkit.plugin.java.JavaPlugin

class TelosDemo : JavaPlugin() {

    override fun onEnable() {
        registerTasks()
    }

    override fun onDisable() {
    }

    fun registerTasks() {
        server.scheduler.runTaskTimer(this, RegenTask(this), 0, 20)
    }
}
