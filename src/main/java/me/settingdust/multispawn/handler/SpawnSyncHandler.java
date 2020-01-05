package me.settingdust.multispawn.handler;

import com.google.inject.Inject;
import io.izzel.amber.commons.i18n.AmberLocale;
import java.util.function.Consumer;
import java.util.List;
import java.util.Optional;
import me.settingdust.multispawn.Constants;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.ChangeBlockEvent.Break;
import org.spongepowered.api.event.block.ChangeBlockEvent.Place;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.Listener;

public abstract class SpawnSyncHandler {
    @Inject
    private AmberLocale locale;

    protected abstract Optional<String> handleBreak(
        Transaction<BlockSnapshot> transaction,
        Player player
    );

    protected abstract void handlePlace(Transaction<BlockSnapshot> blockSnapshot, Player player);

    protected abstract void handleEvent(
        Player player,
        List<Transaction<BlockSnapshot>> transactions,
        Consumer<Transaction<BlockSnapshot>> consumer
    );

    @Listener
    public void onBlockPlace(
        Place event,
        @Root Player player,
        @Getter("getTransactions") List<Transaction<BlockSnapshot>> transactions
    ) {
        if (player.hasPermission("spawn.set")) {
            handleEvent(player, transactions, transaction -> handlePlace(transaction, player));
        }
    }

    @Listener
    public void onBlockBreak(
        Break event,
        @Root Player player,
        @Getter("getTransactions") List<Transaction<BlockSnapshot>> transactions
    ) {
        if (player.hasPermission("spawn.remove")) {
            handleEvent(
                player,
                transactions,
                transaction -> handleBreak(transaction, player)
                    .ifPresent(
                        name -> locale.to(player, Constants.ID + ".command.remove.success", name)
                    )
            );
        }
    }
}
