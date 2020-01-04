package io.izzel.amber.commons.i18n.args;

import com.google.common.reflect.TypeToken;
import io.izzel.amber.commons.i18n.AmberLocale;
import io.izzel.amber.commons.i18n.args.Arg;
import io.izzel.amber.commons.i18n.objects.SimpleStringObject;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.spongepowered.api.text.Text;

@ToString
@RequiredArgsConstructor(staticName = "of")
class StringArg implements Arg {
    private final String text;

    @Override
    public Text toText(AmberLocale holder, Object... args) {
        return SimpleStringObject.of(text).mapAs(TypeToken.of(Text.class), args);
    }
}
