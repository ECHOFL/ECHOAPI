package me.fliqq.echomenu.enums;

import org.mineacademy.fo.remain.CompMaterial;

public enum Couleur {
    BLANC("&fBlanc", CompMaterial.WHITE_WOOL, "&f", "echo.chat.blanc"),
    ORANGE("&6Orange", CompMaterial.ORANGE_WOOL, "&6", "echo.chat.orange"),
    MAGENTA("&dMagenta", CompMaterial.MAGENTA_WOOL, "&d", "echo.chat.magenta"),
    BLEU_CLAIR("&bBleu Clair", CompMaterial.LIGHT_BLUE_WOOL, "&b", "echo.chat.bleuclair"),
    JAUNE("&eJaune", CompMaterial.YELLOW_WOOL, "&e", "echo.chat.jaune"),
    CITRON_VERT("&aCitron Vert", CompMaterial.LIME_WOOL, "&a", "echo.chat.citronvert"),
    ROSE("&dRose", CompMaterial.PINK_WOOL, "&d", "echo.chat.rose"),
    GRIS("&8Gris", CompMaterial.GRAY_WOOL, "&8", "echo.chat.gris"),
    GRIS_CLAIR("&7Gris Clair", CompMaterial.LIGHT_GRAY_WOOL, "&7", "echo.chat.grisclair"),
    CYAN("&3Cyan", CompMaterial.CYAN_WOOL, "&3", "echo.chat.cyan"),
    VIOLET("&5Violet", CompMaterial.PURPLE_WOOL, "&5", "echo.chat.violet"),
    BLEU("&9Bleu", CompMaterial.BLUE_WOOL, "&9", "echo.chat.bleu"),
    MARRON("&6Marron", CompMaterial.BROWN_WOOL, "&6", "echo.chat.marron"),
    VERT("&2Vert", CompMaterial.GREEN_WOOL, "&2", "echo.chat.vert"),
    ROUGE("&cRouge", CompMaterial.RED_WOOL, "&c", "echo.chat.rouge"),
    NOIR("&0Noir", CompMaterial.BLACK_WOOL, "&0", "echo.chat.noir");

    private final String displayName;
    private final CompMaterial woolMaterial;
    private final String colorCode;
    private final String permission;

    Couleur(String displayName, CompMaterial woolMaterial, String colorCode, String permission) {
        this.displayName = displayName;
        this.woolMaterial = woolMaterial;
        this.colorCode = colorCode;
        this.permission = permission;
    }

    public String getDisplayName() {
        return displayName;
    }

    public CompMaterial getWoolMaterial() {
        return woolMaterial;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getPermission() {
        return permission;
    }
}