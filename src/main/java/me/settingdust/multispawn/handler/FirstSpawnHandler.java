package me.settingdust.multispawn.handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.nucleuspowered.nucleus.api.events.NucleusFirstJoinEvent;
import me.settingdust.multispawn.api.MultiSpawnService;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.plugin.PluginContainer;

@Singleton
public class FirstSpawnHandler {

    @Inject
    private MultiSpawnService spawnService;

    @Inject
    public FirstSpawnHandler(EventManager eventManager, PluginContainer pluginContainer) {
        eventManager.registerListeners(pluginContainer, this);
    }

    @Listener
    public void onNucleusFirstJoin(
        NucleusFirstJoinEvent event,
        @Getter("getTargetEntity") Player player
    ) {
        spawnService.getAll().values().stream().findFirst().ifPresent(player::setLocationSafely);
    }
}
