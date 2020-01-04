package me.settingdust.multispawn;

import static me.settingdust.multispawn.Constants.NUCLEUS_ID;

import com.google.common.util.concurrent.Atomics;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import io.izzel.amber.commons.i18n.AmberLocale;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Map;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import me.settingdust.multispawn.api.MultiSpawnService;
import me.settingdust.multispawn.config.MainConfigService;
import me.settingdust.multispawn.config.SpawnConfigService;
import me.settingdust.multispawn.handler.SpawnMarkHandler;
import me.settingdust.multispawn.handler.SpawnWaystoneHandler;
import me.settingdust.multispawn.integration.NucleusIntegration;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.TeleportHelper;
import org.spongepowered.api.world.World;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimpleMultiSpawnService implements MultiSpawnService {
    @Inject
    AmberLocale locale;

    PluginContainer pluginContainer;

    @Inject
    TeleportHelper teleportHelper;

    @Inject
    PluginManager pluginManager;

    @Inject
    Injector injector;

    NucleusIntegration nucleusIntegration = null;

    @Inject
    MainConfigService mainConfigService;

    @Inject
    SpawnConfigService spawnConfigService;

    @Inject
    public SimpleMultiSpawnService(
        SpawnMarkHandler markHandler,
        SpawnWaystoneHandler waystoneHandler,
        PluginContainer pluginContainer
    ) {
        Sponge.getEventManager().registerListeners(pluginContainer, this);
        this.pluginContainer = pluginContainer;

        Objects.requireNonNull(markHandler);
        Objects.requireNonNull(waystoneHandler);
    }

    @Override
    public MultiSpawnService set(String name, Location spawnLocation) {
        getAll().put(name, spawnLocation);
        if (nucleusIntegration != null && mainConfigService.isEnableWarp()) {
            nucleusIntegration.setWarp(name, spawnLocation, spawnLocation.getPosition());
            locale
                .get(Constants.ID + ".nucleus.warp.category")
                .ifPresent(
                    category -> nucleusIntegration.setWarpCategory(name, category.toPlain())
                );
        }
        return this;
    }

    @Override
    public MultiSpawnService remove(String name) {
        getAll().remove(name);
        if (nucleusIntegration != null && mainConfigService.isEnableWarp()) {
            nucleusIntegration.removeWarp(name);
        }
        return this;
    }

    @Override
    public Map<String, Location> getAll() {
        return spawnConfigService.get();
    }

    /**
     * @param origin location to get the closest spawn
     * @return the closest spawn or another spawn(when there is no spawn in this world)
     */
    @Override
    public Location<World> getCorrectSpawn(Location<World> origin) {
        AtomicReference<Location> result = Atomics.newReference(
            origin.getExtent().getSpawnLocation()
        );
        val spawnLocations = getAll().values();
        val closestSpawn = spawnLocations
            .parallelStream()
            .filter(location -> location.getExtent().equals(origin.getExtent()))
            .min(
                Comparator.comparingDouble(
                    value -> value.getPosition().distance(origin.getPosition())
                )
            );
        if (closestSpawn.isPresent()) {
            result.set(closestSpawn.get());
        } else {
            spawnLocations.parallelStream().findFirst().ifPresent(result::set);
        }
        teleportHelper.getSafeLocation((Location<World>) result.get()).ifPresent(result::set);
        return result.get();
    }

    @Listener
    public void onGameInitialization(GameInitializationEvent event) {
        if (pluginManager.isLoaded(NUCLEUS_ID)) {
            this.nucleusIntegration = injector.getInstance(NucleusIntegration.class);
        }
    }

    @Listener
    public void onPostInit(GamePostInitializationEvent event) {
        Sponge.getServiceManager().setProvider(pluginContainer, MultiSpawnService.class, this);
    }
}
