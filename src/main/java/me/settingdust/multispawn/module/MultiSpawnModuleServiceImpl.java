package me.settingdust.multispawn.module;

import com.google.inject.Inject;
import com.google.inject.Injector;
import lombok.Getter;
import me.settingdust.multispawn.MultiSpawnModuleService;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.Listener;

public class MultiSpawnModuleServiceImpl implements MultiSpawnModuleService {
    @Inject
    Injector injector;

    @Getter
    Injector moduleInjector;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        this.moduleInjector = injector.createChildInjector(new IntegrationModule());
    }
}
