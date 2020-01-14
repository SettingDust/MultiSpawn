package me.settingdust.multispawn.handler;

import com.google.inject.Inject;
import me.settingdust.multispawn.api.MultiSpawnService;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.PluginContainer;

public class RespawnHandler {
    @Inject
    private MultiSpawnService spawnService;

    @Inject
    public RespawnHandler(EventManager eventManager, PluginContainer pluginContainer) {
        eventManager.registerListeners(pluginContainer, this);
    }

    @Listener
    public void onRespawn(RespawnPlayerEvent event) {
        event.setToTransform(
            new Transform<>(spawnService.getCorrectSpawn(event.getFromTransform().getLocation()))
        );
    }
}
