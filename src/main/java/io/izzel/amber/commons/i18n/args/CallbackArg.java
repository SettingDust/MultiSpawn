package io.izzel.amber.commons.i18n.args;

import io.izzel.amber.commons.i18n.AmberLocale;
import io.izzel.amber.commons.i18n.args.Arg;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.Text;

@ToString
@RequiredArgsConstructor(staticName = "of")
class CallbackArg implements Arg {
    private final Arg arg;
    private final Consumer<CommandSource> callback;

    @Override
    public Text toText(AmberLocale holder, Object... args) {
        return Text
            .builder()
            .append(arg.toText(holder, args))
            .onClick(TextActions.executeCallback(callback))
            .build();
    }
}
