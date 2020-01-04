package me.settingdust.multispawn.command;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.izzel.amber.commons.i18n.AmberLocale;
import lombok.val;
import me.settingdust.multispawn.api.MultiSpawnService;
import me.settingdust.multispawn.config.MainConfigService;
import me.settingdust.multispawn.config.SpawnConfigService;
import me.settingdust.multispawn.Constants;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.teleport.TeleportHelperFilters;
import org.spongepowered.api.world.TeleportHelper;
import org.spongepowered.api.world.World;

@SuppressWarnings("rawtypes")
@Singleton
public class SpawnCommands {
    @Inject
    private AmberLocale locale;

    @Inject
    private CommandManager commandManager;

    @Inject
    private MultiSpawnService spawnService;

    @Inject
    private SpawnCommand spawnCommand;

    @Inject
    private ListCommand listCommand;

    @Inject
    private SetCommand setCommand;

    @Inject
    private RemoveCommand removeCommand;

    private PluginContainer pluginContainer;

    public static final String LOCALE_PREFIX = Constants.ID;
    public static final String COMMAND_PREFIX = LOCALE_PREFIX + ".command";

    @Inject
    public SpawnCommands(EventManager eventManager, PluginContainer pluginContainer) {
        this.pluginContainer = pluginContainer;
        eventManager.registerListener(
            pluginContainer,
            GameStartingServerEvent.class,
            this::onStarting
        );
    }

    Text getDescription(String name, String defaultString) {
        return getText(name, "description", defaultString);
    }

    Text getText(String name, String type, String defaultString) {
        return locale.get(COMMAND_PREFIX + name + "." + type).orElse(Text.of(defaultString));
    }

    Text locationToText(Location location) {
        return Text.of(
            Joiner
                .on(" ")
                .join(
                    new Object[] {
                        (int) location.getX(),
                        (int) location.getY(),
                        (int) location.getZ(),
                        ((World) location.getExtent()).getName()
                    }
                )
        );
    }

    @Listener
    public void onStarting(GameStartingServerEvent event) {
        commandManager.register(
            pluginContainer,
            CommandSpec
                .builder()
                .permission(COMMAND_PREFIX)
                .description(getDescription("main", "MultiSpawn"))
                .executor(spawnCommand)
                .child(
                    CommandSpec
                        .builder()
                        .permission(COMMAND_PREFIX)
                        .description(getDescription("list", "List all point"))
                        .executor(listCommand)
                        .build(),
                    "list",
                    "ls",
                    "l",
                    "all"
                )
                .child(
                    CommandSpec
                        .builder()
                        .permission(COMMAND_PREFIX + ".set")
                        .description(getDescription("set", "Set a spawn point"))
                        .arguments(GenericArguments.string(Text.of("name")))
                        .executor(setCommand)
                        .build(),
                    "set",
                    "s",
                    "add",
                    "a"
                )
                .child(
                    CommandSpec
                        .builder()
                        .permission(COMMAND_PREFIX + ".remove")
                        .description(getDescription("set", "Remove a spawn point"))
                        .arguments(
                            GenericArguments.choices(
                                Text.of("name"),
                                () -> spawnService.getAll().keySet(),
                                name -> name
                            )
                        )
                        .executor(removeCommand)
                        .build(),
                    "remove",
                    "rm",
                    "r",
                    "delete",
                    "del"
                )
                .build(),
            "spawn"
        );
    }
}
