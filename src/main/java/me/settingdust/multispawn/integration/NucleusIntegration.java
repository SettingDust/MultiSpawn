package me.settingdust.multispawn.integration;

import com.flowpowered.math.vector.Vector3d;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import io.github.nucleuspowered.nucleus.api.NucleusAPI;
import java.util.Objects;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import me.settingdust.multispawn.handler.FirstSpawnHandler;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true, fluent = true)
public class NucleusIntegration {

    @Inject
    @SneakyThrows
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public NucleusIntegration(
        PluginContainer pluginContainer,
        EventManager eventManager,
        Injector injector,
        FirstSpawnHandler spawnHandler
    ) {
        Objects.requireNonNull(spawnHandler);
        NucleusAPI.getModuleService().removeModule("spawn", pluginContainer.getInstance().get());
    }

    public void setWarp(String warpName, Location<World> location, Vector3d rotation) {
        NucleusAPI
            .getWarpService()
            .ifPresent(
                nucleusWarpService -> nucleusWarpService.setWarp(warpName, location, rotation)
            );
    }

    public void setWarpCategory(String warpName, @Nullable String category) {
        NucleusAPI
            .getWarpService()
            .ifPresent(
                nucleusWarpService -> nucleusWarpService.setWarpCategory(warpName, category)
            );
    }

    public void removeWarp(String warpName) {
        NucleusAPI
            .getWarpService()
            .ifPresent(nucleusWarpService -> nucleusWarpService.removeWarp(warpName));
    }
}
