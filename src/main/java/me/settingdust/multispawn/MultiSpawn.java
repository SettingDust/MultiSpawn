package me.settingdust.multispawn;

import static me.settingdust.multispawn.Constants.NUCLEUS_ID;

import com.google.inject.Inject;
import io.izzel.amber.commons.i18n.AmberLocale;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.SneakyThrows;
import me.settingdust.multispawn.api.MultiSpawnService;
import me.settingdust.multispawn.command.SpawnCommands;
import me.settingdust.multispawn.handler.SpawnMarkHandler;
import me.settingdust.multispawn.handler.SpawnWaystoneHandler;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.ServiceManager;

@Plugin(
    id = Constants.ID,
    name = "MultiSpawn",
    description = "RPG like plugin. Respawn at the closest spawn point",
    authors = { "SettingDust" },
    dependencies = { @Dependency(id = NUCLEUS_ID, optional = true) }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MultiSpawn {
    @Inject
    EventManager eventManager;

    @Inject
    public MultiSpawn(
        AmberLocale locale,
        SpawnCommands spawnCommands,
        MultiSpawnService spawnService
    ) {
        Objects.requireNonNull(locale);
        Objects.requireNonNull(spawnCommands);
        Objects.requireNonNull(spawnService);
    }
}
