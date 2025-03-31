package ru.leymooo.antirelog.wg;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import org.bukkit.entity.Player;
import ru.leymooo.antirelog.config.Settings;
import ru.leymooo.antirelog.manager.PvPManager;
import ru.leymooo.antirelog.util.Utils;

import java.util.Set;


public class AntiExitFlag extends Handler {
    private static Settings settings;
    private static PvPManager pvpManager;
    public static StateFlag ANTI_EXIT_FLAG;

    public static final AntiExitFlag.Factory FACTORY = new AntiExitFlag.Factory();

    public static class Factory extends Handler.Factory<AntiExitFlag> {
        @Override
        public AntiExitFlag create(Session session) {
            return new AntiExitFlag(session);
        }
    }

    public static void initializeFlag()
    {
        if (WorldGuard.getInstance().getFlagRegistry().get("anti-exit") == null) {
            try {
                StateFlag flag = new StateFlag("anti-exit", true);
                WorldGuard.getInstance().getFlagRegistry().register(flag);
                ANTI_EXIT_FLAG = flag;
            } catch (FlagConflictException e) {
                Flag existing = WorldGuard.getInstance().getFlagRegistry().get("anti-exit");
                ANTI_EXIT_FLAG = (StateFlag) existing;
            }
        }
    }

    public static void setSettingsAndManager(Settings settings, PvPManager pvpManager)
    {
        AntiExitFlag.settings = settings;
        AntiExitFlag.pvpManager = pvpManager;
    }


    protected AntiExitFlag(Session session) {
        super(session);
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        if (exited.isEmpty())
            return true;

        Player bukkitPlayer = BukkitAdapter.adapt(player);
        if (!AntiExitFlag.pvpManager.isInPvP(bukkitPlayer))
            return true;
        for (ProtectedRegion reg : exited) {
            StateFlag.State state = reg.getFlag(ANTI_EXIT_FLAG);
            if (state == StateFlag.State.ALLOW) {
                Settings settings = AntiExitFlag.settings;
                String message = settings.getMessages().getPvpCantExit();
                if (!message.isEmpty()) {
                    bukkitPlayer.sendMessage(Utils.color(message));
                }
                return false;
            }

        }

        return true;
    }
}
