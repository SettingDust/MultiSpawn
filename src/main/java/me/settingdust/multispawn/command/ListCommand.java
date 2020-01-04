package me.settingdust.multispawn.command;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.izzel.amber.commons.i18n.AmberLocale;
import lombok.SneakyThrows;
import lombok.val;
import me.settingdust.multispawn.api.MultiSpawnService;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.Text;

@SuppressWarnings("unchecked")
@Singleton
public class ListCommand implements CommandExecutor {
    @Inject
    private AmberLocale locale;

    @Inject
    private MultiSpawnService spawnService;

    @Inject
    private SpawnCommands spawnCommands;

    @SuppressWarnings("NullableProblems")
    @Override
    @SneakyThrows
    public CommandResult execute(CommandSource src, CommandContext args) {
        val spawns = Sets.<Text>newHashSet();
        val allSpawn = spawnService.getAll();
        if (allSpawn.size() > 0) {
            allSpawn.forEach(
                (spawnName, location) -> {
                    val spawnText = Text.of(spawnName).toBuilder();
                    val locationToText = spawnCommands.locationToText(location);
                    if (src instanceof Player) {
                        val hoverText = Text.builder();
                        hoverText.append(
                            locale.get(
                                SpawnCommands.COMMAND_PREFIX + ".list.hover",
                                Text.of("&aClick to {0}"),
                                locationToText
                            )
                        );
                        spawnText.onHover(TextActions.showText(hoverText.build()));
                        spawnText.onClick(
                            TextActions.executeCallback(
                                commandSource -> ((Player) commandSource).setLocationSafely(
                                        location
                                    )
                            )
                        );
                    } else {
                        spawnText.append(locationToText);
                    }
                    spawns.add(spawnText.build());
                }
            );
            src.sendMessage(
                locale.get(
                    SpawnCommands.COMMAND_PREFIX + ".list.message",
                    Text.of("&aSpawns：{0}"),
                    Text.joinWith(Text.of(", "), spawns)
                )
            );
            return CommandResult.success();
        } else {
            src.sendMessage(
                locale.get(
                    SpawnCommands.COMMAND_PREFIX + ".list.empty",
                    Text.joinWith(Text.of(", "), spawns)
                )
            );
        }

        return CommandResult.empty();
    }
}
