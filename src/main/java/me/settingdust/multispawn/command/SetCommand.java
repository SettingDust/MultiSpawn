package me.settingdust.multispawn.command;

import static me.settingdust.multispawn.command.SpawnCommands.COMMAND_PREFIX;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.izzel.amber.commons.i18n.AmberLocale;
import me.settingdust.multispawn.api.MultiSpawnService;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

@Singleton
public class SetCommand implements CommandExecutor {
    @Inject
    private AmberLocale locale;

    @Inject
    private MultiSpawnService spawnService;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            args
                .<String>getOne("name")
                .ifPresent(
                    name -> {
                        spawnService.set(name, ((Player) src).getLocation());
                        locale.to(src, COMMAND_PREFIX + ".set.success", name);
                    }
                );
            return CommandResult.success();
        } else {
            return CommandResult.empty();
        }
    }
}
