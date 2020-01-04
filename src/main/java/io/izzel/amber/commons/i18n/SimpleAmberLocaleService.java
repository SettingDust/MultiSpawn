package io.izzel.amber.commons.i18n;

import io.izzel.amber.commons.i18n.annotation.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.Map;
import lombok.val;
import org.spongepowered.api.Sponge;

class SimpleAmberLocaleService implements AmberLocaleService {
    private final Map<String, Map<Locale, AmberLocaleProvider>> locale = new ConcurrentHashMap<>();

    @Override
    public AmberLocale get(Object plugin, Locale info) {
        val container = Sponge
            .getPluginManager()
            .fromInstance(plugin)
            .orElseThrow(RuntimeException::new);
        return locale
            .computeIfAbsent(container.getId(), it -> new HashMap<>())
            .computeIfAbsent(info, it -> new AmberLocaleProvider(plugin, info));
    }
}
