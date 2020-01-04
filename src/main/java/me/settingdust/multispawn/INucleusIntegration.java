package me.settingdust.multispawn;

import com.flowpowered.math.vector.Vector3d;
import com.google.inject.ImplementedBy;
import javax.annotation.Nullable;
import me.settingdust.multispawn.integration.NucleusIntegration;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

@ImplementedBy(NucleusIntegration.class)
public interface INucleusIntegration {
    void setWarp(String warpName, Location<World> location, Vector3d rotation);

    void setWarpCategory(String warpName, @Nullable String category);

    void removeWarp(String warpName);
}
