package me.settingdust.multispawn;

import com.google.inject.ImplementedBy;
import com.google.inject.Injector;
import me.settingdust.multispawn.module.MultiSpawnModuleServiceImpl;

@ImplementedBy(MultiSpawnModuleServiceImpl.class)
public interface MultiSpawnModuleService {
    Injector getModuleInjector();
}
