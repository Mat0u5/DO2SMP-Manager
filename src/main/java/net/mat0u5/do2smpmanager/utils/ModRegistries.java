package net.mat0u5.do2smpmanager.utils;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.mat0u5.do2smpmanager.command.Command;
import net.mat0u5.do2smpmanager.events.Events;

public class ModRegistries {
    public static void registerModStuff() {
        registerCommands();
        registerEvents();
        TextUtils.setEmotes();
    }
    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(Command::register);
    }
    private static void registerEvents() {
        Events.register();
    }
}
