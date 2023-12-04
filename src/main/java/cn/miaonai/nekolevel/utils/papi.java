package cn.miaonai.nekolevel.utils;

import cn.miaonai.nekolevel.Event.papiEvent;
import cn.miaonai.nekolevel.NekoLevel;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class papi extends PlaceholderExpansion {

    private final NekoLevel nekoLevel;

    public papi(NekoLevel nekoLevel) {
        this.nekoLevel = nekoLevel;
    }

    @Override
    public @NotNull String getAuthor() {
        return "爱炒菜的真寻酱";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "nekolevel";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.1";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("level")){
            int exp = Integer.parseInt(Objects.requireNonNull(papiEvent.getPlayerEXP((Player) player)));
            int level = exp / NekoLevel.needexp;
            return String.valueOf(level);
        }

        if(params.equalsIgnoreCase("exp")) {
            int exp = Integer.parseInt(Objects.requireNonNull(papiEvent.getPlayerEXP((Player) player)));
            return String.valueOf(exp);
        }


        return null; // Placeholder is unknown by the Expansion
    }
}