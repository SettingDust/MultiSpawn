package me.settingdust.multispawn;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.world.teleport.TeleportHelperFilter;

public class WaystoneTeleportHelperFilter implements TeleportHelperFilter {

    @Override
    public boolean isSafeFloorMaterial(BlockState blockState) {
        return !(blockState.getType().getName().equalsIgnoreCase(Constants.WAYSTONE_BLOCK_ID));
    }

    @Override
    public boolean isSafeBodyMaterial(BlockState blockState) {
        return !(blockState.getType().getName().equalsIgnoreCase(Constants.WAYSTONE_BLOCK_ID));
    }

    @Override
    public String getId() {
        return "multispawn:waystone";
    }

    @Override
    public String getName() {
        return "Waystone Teleport Helper Filter";
    }
}
