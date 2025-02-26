package com.lambda_professional.lambdaupdate.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class Text {
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacyAmpersand();

    public static final Component PREFIX = Component.text("[LambdaUpdate] ").color(NamedTextColor.BLUE);

    public static Component formatLegacyMessage(String legacyText) {
        return PREFIX.append(LEGACY_SERIALIZER.deserialize(legacyText));
    }

    public static Component formatComponentMessage(Component message) {
        return PREFIX.append(message);
    }
}