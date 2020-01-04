package me.settingdust.multispawn.api;

import com.google.inject.ImplementedBy;
import java.util.Map;
import java.util.Optional;
import me.settingdust.multispawn.SimpleMultiSpawnService;
import org.spongepowered.api.world.extent.Extent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

@SuppressWarnings("rawtypes")
@ImplementedBy(SimpleMultiSpawnService.class)
public interface MultiSpawnService {
    default Optional<Location> get(String name) {
        return Optional.ofNullable(getAll().get(name));
    }

    Map<String, Location> getAll();

    default MultiSpawnService set(String name, Location spawnLocation) {
        getAll().put(name, spawnLocation);
        return this;
    }

    default MultiSpawnService remove(String name) {
        getAll().remove(name);
        return this;
    }

    Location<World> getCorrectSpawn(Location<World> origin);
}
