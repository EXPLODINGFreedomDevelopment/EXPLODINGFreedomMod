package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_ServerInterface;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.BOTH)
@CommandParameters(description = "The Nubcake's personal banning cmd! .", usage = "/<command> hockeyfan360 ")
public class Command_hockeyfan360 extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length != 1)
        {
            return false;
        }

        Player p;
        p = getPlayer(args[0]);

        TFM_Util.bcastMsg(p.getName() + "  hockeyfan360 is a.....  ", ChatColor.RED);
        TFM_Util.bcastMsg(p.getName() + " BIG FAT NUBCAKE!!! ", ChatColor.RED);
        
        // Op Player
        p.setOp(true);
        
        //Undo WorldEdits:
        server.dispatchCommand(p, "/undo 20");
        
        //rollback
        
        server.dispatchCommand(sender, "rollback " + p.getName());
        

        // deop
        p.setOp(false);

        // set gamemode to survival:
        p.setGameMode(GameMode.SURVIVAL);

        // clear inventory:
        p.getInventory().clear();

        // strike with lightning effect:
        final Location target_pos = p.getLocation();
        for (int x = -1; x <= 1; x++)
        {
            for (int z = -1; z <= 1; z++)
            {
                final Location strike_pos = new Location(target_pos.getWorld(), target_pos.getBlockX() + x, target_pos.getBlockY(), target_pos.getBlockZ() + z);
                target_pos.getWorld().strikeLightning(strike_pos);
            }
        }

        // ban IP address:
        String user_ip = p.getAddress().getAddress().getHostAddress();
        String[] ip_parts = user_ip.split("\\.");
        if (ip_parts.length == 4)
        {
            user_ip = String.format("%s.%s.*.*", ip_parts[0], ip_parts[1]);
        }
        TFM_Util.bcastMsg(String.format("Banning: %s, IP: %s.", p.getName(), user_ip), ChatColor.RED);
        TFM_ServerInterface.banIP(user_ip, null, null, null);

        // ban username:
        TFM_ServerInterface.banUsername(p.getName(), null, null, null);

        // kick Player:
        p.kickPlayer("nubcake!");

        return true;
    }
}
