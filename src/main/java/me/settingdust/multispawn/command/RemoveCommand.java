package me.settingdust.multispawn.command;

import static me.settingdust.multispawn.command.SpawnCommands.COMMAND_PREFIX;

import com.google.inject.Inject;
import io.izzel.amber.commons.i18n.AmberLocale;
import me.settingdust.multispawn.api.MultiSpawnService;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

public class RemoveCommand implements CommandExecutor {
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
                        spawnService.remove(name);
                        locale.to(src, COMMAND_PREFIX + ".remove.success", name);
                    }
                );
            return CommandResult.success();
        } else {
            return CommandResult.empty();
        }
    }
}
