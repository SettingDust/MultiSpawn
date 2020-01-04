package me.settingdust.multispawn.config.model;

import com.google.common.collect.Sets;
import java.util.Set;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.Getter;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import ninja.leaping.configurate.objectmapping.Setting;
import org.spongepowered.api.block.BlockTypes;

@ConfigSerializable
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainConfigModel {
    @Getter
    @Setting(comment = "Enable nucleus warp sync")
    boolean enableWarp = true;

    @Getter
    @Setting(comment = "Enable waystone(https://minecraft.curseforge.com/projects/waystones)")
    boolean enableWaystone = true;

    @Getter
    @Setting(comment = "Enable mark block warp sync")
    boolean enableMark = true;

    @Getter
    @Setting(comment = "Mark block list")
    Set<String> marks = Sets.newHashSet(BlockTypes.BEACON.getName());
}
