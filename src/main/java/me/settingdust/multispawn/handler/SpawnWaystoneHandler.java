package me.settingdust.multispawn.handler;

import com.google.common.base.Strings;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import me.settingdust.multispawn.api.MultiSpawnService;
import me.settingdust.multispawn.config.MainConfigService;
import me.settingdust.multispawn.config.model.MainConfigModel;
import me.settingdust.multispawn.Constants;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpawnWaystoneHandler extends SpawnSyncHandler {
    Map<UUID, Location<World>> typingMap = Maps.newHashMap();

    AmberLocale locale;

    @Inject
    MainConfigService mainConfigService;

    @Inject
    private MultiSpawnService spawnService;

    @Inject
    public SpawnWaystoneHandler(AmberLocale locale, PluginContainer pluginContainer) {
        this.locale = locale;

        Sponge.getEventManager().registerListeners(pluginContainer, this);
    }

    @Listener
    public void onMove(MoveEntityEvent event, @Root Player player) {
        val uuid = player.getUniqueId();
        typingMap
            .entrySet()
            .parallelStream()
            .filter(entry -> entry.getKey().equals(uuid))
            .findFirst()
            .ifPresent(
                entry -> typingMap
                    .get(entry.getKey())
                    .getTileEntity()
                    .filter(TileEntity::isValid)
                    .map(DataSerializable::toContainer)
                    .flatMap(dataContainer -> dataContainer.getString(DataQuery.of("UnsafeData")))
                    .map(unsafeData -> Pattern.compile("WaystoneName=(.*?),").matcher(unsafeData))
                    .filter(Matcher::find)
                    .map(matcher -> matcher.group(1))
                    .filter(s -> !Strings.isNullOrEmpty(s))
                    .ifPresent(
                        name -> {
                            spawnService.set(name, entry.getValue());
                            typingMap.remove(uuid);
                            locale.to(player, Constants.ID + ".command.set.success", name);
                        }
                    )
            );
    }

    @Override
    protected void handlePlace(Transaction<BlockSnapshot> transaction, Player player) {
        handleWaystone(
            transaction.getFinal(),
            location -> typingMap.put(player.getUniqueId(), location)
        );
    }

    @Override
    protected Optional<String> handleBreak(Transaction<BlockSnapshot> transaction, Player player) {
        val result = Atomics.<String>newReference();
        val original = transaction.getOriginal();
        handleWaystone(
            original,
            location -> spawnService
                .getAll()
                .entrySet()
                .parallelStream()
                .filter(
                    entry -> entry.getValue().getPosition().distance(location.getPosition()) < 1
                )
                .findFirst()
                .map(Entry::getKey)
                .ifPresent(
                    name -> {
                        spawnService.remove(name);
                        result.set(name);
                    }
                )
        );
        return Optional.ofNullable(result.get());
    }

    private void handleWaystone(BlockSnapshot blockSnapshot, Consumer<Location<World>> consumer) {
        if (mainConfigService.isEnableWaystone()) {
            val state = blockSnapshot.getState();
            if (state.getType().getName().equalsIgnoreCase(Constants.WAYSTONE_BLOCK_ID)) {
                state
                    .getTrait("base")
                    .flatMap(
                        blockTrait -> state
                            .getTraitValue(blockTrait)
                            .filter(o -> o instanceof Boolean)
                            .map(o -> (Boolean) o)
                            .filter(Boolean::booleanValue)
                            .flatMap(isBase -> blockSnapshot.getLocation())
                    )
                    .ifPresent(consumer);
            }
        }
    }

    @Override
    protected void handleEvent(
        Player player,
        List<Transaction<BlockSnapshot>> transactions,
        Consumer<Transaction<BlockSnapshot>> consumer
    ) {
        if (mainConfigService.isEnableWaystone()) {
            transactions.parallelStream().forEach(consumer);
        }
    }
}
