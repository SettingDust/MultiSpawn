package me.settingdust.multispawn.module;

import static me.settingdust.multispawn.Constants.NUCLEUS_ID;

import com.google.inject.AbstractModule;
import lombok.val;
import me.settingdust.multispawn.integration.NucleusIntegration;
import me.settingdust.multispawn.INucleusIntegration;
import org.spongepowered.api.Sponge;

public class IntegrationModule extends AbstractModule {

    @Override
    protected void configure() {
        val pluginManager = Sponge.getPluginManager();
        if (pluginManager.isLoaded(NUCLEUS_ID)) {
            bind(INucleusIntegration.class).to(NucleusIntegration.class);
        }
    }
}
