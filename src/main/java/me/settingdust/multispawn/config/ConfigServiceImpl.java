package me.settingdust.multispawn.config;

import com.google.inject.Inject;
import java.nio.file.Path;
import lombok.AccessLevel;
import lombok.experimental.Delegate;
import lombok.experimental.FieldDefaults;
import lombok.SneakyThrows;
import lombok.val;
import me.settingdust.multispawn.ConfigService;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.world.SaveWorldEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.Sponge;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigServiceImpl implements ConfigService {
    ConfigurationLoader<CommentedConfigurationNode> configLoader;

    @Delegate(types = ConfigurationNode.class)
    ConfigurationNode node;

    @Inject
    @ConfigDir(sharedRoot = false)
    Path configDir;

    Path configPath;

    public ConfigServiceImpl(Path configPath, PluginContainer pluginContainer) {
        this.configPath = configPath;
        Sponge.getEventManager().registerListeners(pluginContainer, this);
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public ConfigService load() {
        val absolutePath = configDir.resolve(configPath);
        absolutePath.getParent().toFile().mkdirs();

        this.configLoader = HoconConfigurationLoader.builder().setPath(absolutePath).build();
        this.node = configLoader.load();
        return this;
    }

    @Override
    @SneakyThrows
    public ConfigService save() {
        configLoader.save(node);
        return this;
    }

    @Listener
    public void onPreInit(GameInitializationEvent event) {
        this.load();
    }

    @Listener
    public void onGameReload(GameReloadEvent event) {
        this.load();
    }

    @Listener
    public void onSaveWorld(SaveWorldEvent event) {
        this.save();
    }
}
