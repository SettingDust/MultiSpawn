package me.settingdust.multispawn.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.SneakyThrows;
import lombok.val;
import me.settingdust.multispawn.api.MultiSpawnService;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

@Singleton
public class SpawnCommand implements CommandExecutor {
    @Inject
    private MultiSpawnService spawnService;

    @Override
    @SneakyThrows
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof Player) {
            val player = (Player) src;
            player.setLocation(spawnService.getCorrectSpawn(player.getLocation()));
            return CommandResult.success();
        } else {
            return CommandResult.empty();
        }
    }
}
