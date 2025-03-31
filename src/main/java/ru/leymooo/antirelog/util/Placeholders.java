package ru.leymooo.antirelog.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.leymooo.antirelog.Antirelog;
import ru.leymooo.antirelog.manager.PvPManager;

public class Placeholders extends PlaceholderExpansion {
    private final PvPManager pvpManger;

    public Placeholders(PvPManager pvpManger) {

        this.pvpManger = pvpManger;
    }
    @Override
    public @NotNull String getIdentifier() {
        return "antirelog";
    }

    @Override
    public @NotNull String getAuthor() {
        return "ycoord";
    }

    @Override
    public @NotNull String getVersion() {
        return "3.0.11";
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        } else if (identifier.equals("is_in_pvp")) {
            return pvpManger.isInPvP(player) ? "yes" : "no";
        } else if (identifier.equals("time_left")) {
            if (!pvpManger.isInPvP(player))
                return null;
            return String.valueOf(pvpManger.getTimeRemainingInPvP(player));
        }
        return null;
    }
}
