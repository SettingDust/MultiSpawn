package io.izzel.amber.commons.i18n.args;

import io.izzel.amber.commons.i18n.AmberLocale;
import java.util.function.Consumer;
import java.util.Optional;
import java.util.UUID;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;

@FunctionalInterface
public interface Arg {
    Text toText(AmberLocale holder, Object... args);

    default io.izzel.amber.commons.i18n.args.Arg withCallback(Consumer<CommandSource> callback) {
        return io.izzel.amber.commons.i18n.args.CallbackArg.of(this, callback);
    }

    static io.izzel.amber.commons.i18n.args.Arg of(Object object) {
        if (object instanceof Optional) return of(((Optional<?>) object).orElse(null));
        if (object == null) return new NullArg();
        if (object instanceof io.izzel.amber.commons.i18n.args.Arg) return (
            (io.izzel.amber.commons.i18n.args.Arg) object
        );
        if (object instanceof String) return io.izzel.amber.commons.i18n.args.StringArg.of(
            (String) object
        );
        if (object instanceof ItemStack) return ItemStackArg.of(
            ((ItemStack) object).createSnapshot()
        );
        if (object instanceof ItemStackSnapshot) return ItemStackArg.of((ItemStackSnapshot) object);
        if (object instanceof Entity) return io.izzel.amber.commons.i18n.args.EntityArg.of(
            (Entity) object
        );
        if (object instanceof Text) return (holder, args) -> ((Text) object);
        return io.izzel.amber.commons.i18n.args.StringArg.of(String.valueOf(object));
    }

    static io.izzel.amber.commons.i18n.args.Arg user(UUID uuid) {
        return UserArg.of(uuid);
    }

    static io.izzel.amber.commons.i18n.args.Arg ref(String node) {
        return RefArg.of(node);
    }
}
