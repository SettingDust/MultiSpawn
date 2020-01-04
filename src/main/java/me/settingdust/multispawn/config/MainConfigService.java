package me.settingdust.multispawn.config;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.nio.file.Path;
import lombok.AccessLevel;
import lombok.experimental.Delegate;
import lombok.experimental.FieldDefaults;
import lombok.SneakyThrows;
import me.settingdust.multispawn.config.model.MainConfigModel;
import me.settingdust.multispawn.ConfigService;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.plugin.PluginContainer;

@Singleton
@SuppressWarnings("UnstableApiUsage")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainConfigService extends ConfigServiceImpl {
    @Delegate(types = MainConfigModel.class)
    MainConfigModel configModel;

    @Inject
    public MainConfigService(
        PluginContainer pluginContainer,
        @DefaultConfig(sharedRoot = false) Path path
    ) {
        super(path, pluginContainer);
    }

    @Override
    @SneakyThrows
    public ConfigService load() {
        super.load();
        this.configModel =
            this.getValue(TypeToken.of(MainConfigModel.class), new MainConfigModel());
        return this;
    }

    @Override
    @SneakyThrows
    public ConfigService save() {
        this.setValue(TypeToken.of(MainConfigModel.class), configModel);
        return super.save();
    }
}
