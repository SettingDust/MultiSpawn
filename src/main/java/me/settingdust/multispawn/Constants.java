package me.settingdust.multispawn;

import com.google.common.reflect.TypeToken;
import java.util.Map;
import org.spongepowered.api.world.Location;

public class Constants {
    public static final String ID = "multispawn";
    public static final String NUCLEUS_ID = "nucleus";

    public static final String WAYSTONE_BLOCK_ID = "waystones:waystone";

    @SuppressWarnings({ "UnstableApiUsage", "rawtypes" })
    public static final TypeToken<Map<String, Location>> SPAWNS_TYPE_WOKEN = new TypeToken<Map<String, Location>>() {
        private static final long serialVersionUID = -1;
    };
}
