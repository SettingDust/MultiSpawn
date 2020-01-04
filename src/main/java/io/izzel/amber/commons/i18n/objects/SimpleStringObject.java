package io.izzel.amber.commons.i18n.objects;

import com.google.common.reflect.TypeToken;
import io.izzel.amber.commons.i18n.objects.LocaleObject;
import io.izzel.amber.commons.i18n.objects.MetaObject;
import lombok.ToString;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.Text;

@ToString
public class SimpleStringObject extends LocaleObject implements MetaObject {
    private final Text text;

    private SimpleStringObject(Text text) {
        this.text = text;
    }

    public static io.izzel.amber.commons.i18n.objects.SimpleStringObject of(String text) {
        return new io.izzel.amber.commons.i18n.objects.SimpleStringObject(
            TextSerializers.FORMATTING_CODE.deserialize(text)
        );
    }

    @Override
    public void send(MessageReceiver receiver, Object... args) {
        receiver.sendMessage(applyMeta(-1, text, args));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T mapAs(TypeToken<T> typeToken, Object... args) {
        if (typeToken.equals(TypeToken.of(Text.class))) {
            return (T) applyMeta(-1, text, args);
        } else {
            return null;
        }
    }

    @Override
    public Text apply(Text text, Object... args) {
        return applyMeta(-1, this.text, args);
    }
}
