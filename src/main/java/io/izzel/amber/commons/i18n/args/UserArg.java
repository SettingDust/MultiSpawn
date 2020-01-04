package io.izzel.amber.commons.i18n.args;

import io.izzel.amber.commons.i18n.AmberLocale;
import io.izzel.amber.commons.i18n.args.Arg;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

@ToString
@RequiredArgsConstructor(staticName = "of")
public class UserArg implements Arg {
    private final UUID uuid;

    @Override
    public Text toText(AmberLocale holder, Object... args) {
        return Sponge
            .getServiceManager()
            .provideUnchecked(UserStorageService.class)
            .get(uuid)
            .map(User::getName)
            .map(Text::of)
            .orElse(Text.of("Unknown user " + uuid));
    }
}
