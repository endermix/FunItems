package org.endermix.funitems;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public ReloadCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("funitems.reload")) {
            plugin.reloadConfig();
            sender.sendMessage("§eКонфигурация успешно перезагружена!");
            return true;
        } else {
            sender.sendMessage("§cУ вас нет прав на выполнение этой команды.");
            return false;
        }
    }
}
