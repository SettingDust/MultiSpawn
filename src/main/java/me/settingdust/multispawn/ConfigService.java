package me.settingdust.multispawn;

import ninja.leaping.configurate.ConfigurationNode;

public interface ConfigService extends ConfigurationNode {
    ConfigService save();

    ConfigService load();
}
