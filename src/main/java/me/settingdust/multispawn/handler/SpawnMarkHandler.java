package me.settingdust.multispawn.handler;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.Atomics;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.izzel.amber.commons.i18n.AmberLocale;
import java.util.function.Consumer;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import me.settingdust.multispawn.api.MultiSpawnService;
import me.settingdust.multispawn.config.MainConfigService;
import me.settingdust.multispawn.Constants;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.message.MessageChannelEvent.Chat;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpawnMarkHandler extends SpawnSyncHandler {
    Map<UUID, Location<World>> typingMap = Maps.newHashMap();

    AmberLocale locale;

    @Inject
    MultiSpawnService spawnService;

    @Inject
    MainConfigService mainConfigService;

    @Inject
    public SpawnMarkHandler(AmberLocale locale, PluginContainer pluginContainer) {
        this.locale = locale;

        Sponge.getEventManager().registerListeners(pluginContainer, this);
    }

    @Listener
    public void onMessageChannelChat(
        Chat event,
        @Root Player player,
        @Getter("getRawMessage") Text text
    ) {
        val uuid = player.getUniqueId();
        typingMap
            .entrySet()
            .parallelStream()
            .filter(entry -> entry.getKey().equals(uuid))
            .findFirst()
            .ifPresent(
                entry -> {
                    val name = text.toPlain();
                    spawnService.set(name, entry.getValue());
                    locale.to(player, Constants.ID + ".command.set.success", name);
                    typingMap.remove(uuid);
                    event.setCancelled(true);
                }
            );
    }

    @Override
    protected Optional<String> handleBreak(Transaction<BlockSnapshot> transaction, Player player) {
        val result = Atomics.<String>newReference();
        val original = transaction.getOriginal();

        mainConfigService
            .getMarks()
            .parallelStream()
            .filter(strings -> strings.equalsIgnoreCase(original.getState().getType().getName()))
            .findFirst()
            .flatMap(strings -> original.getLocation())
            .flatMap(
                location -> spawnService
                    .getAll()
                    .entrySet()
                    .parallelStream()
                    .filter(
                        entry -> entry.getValue().getPosition().distance(location.getPosition()) < 1
                    )
                    .findFirst()
                    .map(Entry::getKey)
            )
            .ifPresent(
                name -> {
                    spawnService.remove(name);
                    result.set(name);
                }
            );

        return Optional.ofNullable(result.get());
    }

    @Override
    protected void handlePlace(Transaction<BlockSnapshot> transaction, Player player) {
        val transactionFinal = transaction.getFinal();
        mainConfigService
            .getMarks()
            .parallelStream()
            .filter(
                strings -> strings.equalsIgnoreCase(transactionFinal.getState().getType().getName())
            )
            .findFirst()
            .filter(strings -> strings.contains(transactionFinal.getState().getType().getName()))
            .flatMap(strings -> transactionFinal.getLocation())
            .ifPresent(
                location -> {
                    locale.to(player, Constants.ID + ".message.waitingForType");
                    typingMap.put(player.getUniqueId(), location);
                }
            );
    }

    @Override
    protected void handleEvent(
        Player player,
        List<Transaction<BlockSnapshot>> transactions,
        Consumer<Transaction<BlockSnapshot>> consumer
    ) {
        if (mainConfigService.isEnableMark()) {
            transactions.parallelStream().filter(Transaction::isValid).forEach(consumer);
        }
    }
}
