package me.settingdust.multispawn.config;

import static me.settingdust.multispawn.Constants.SPAWNS_TYPE_WOKEN;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import java.nio.file.Paths;
import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.SneakyThrows;
import me.settingdust.multispawn.ConfigService;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.world.Location;

@SuppressWarnings("rawtypes")
@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpawnConfigService
    extends ConfigServiceImpl
    implements Provider<Map<String, Location>> {
    Map<String, Location> spawns;

    @Inject
    public SpawnConfigService(PluginContainer pluginContainer) {
        super(Paths.get("spawns.conf"), pluginContainer);
    }

    @Override
    @SneakyThrows
    public ConfigService save() {
        this.setValue(SPAWNS_TYPE_WOKEN, spawns);
        return super.save();
    }

    @Override
    public Map<String, Location> get() {
        return spawns;
    }

    @Listener
    @SneakyThrows
    public void onGameStartingServer(GameStartingServerEvent event) {
        this.spawns = this.getValue(SPAWNS_TYPE_WOKEN, Maps.newHashMap());
    }
}
