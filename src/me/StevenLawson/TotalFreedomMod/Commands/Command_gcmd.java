package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
import me.StevenLawson.TotalFreedomMod.TFM_CommandBlocker;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.BOTH)
@CommandParameters(description = "Send a command as someone else.", usage = "/<command> <fromname> <outcommand>")
public class Command_gcmd extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 2)
        {
            return false;
        }

        final Player player = getPlayer(args[0]);

        if (player == null)
        {
            sender.sendMessage(TotalFreedomMod.PLAYER_NOT_FOUND);
            return true;
        }

        final String outCommand = StringUtils.join(args, " ", 1, args.length);

        if (TFM_CommandBlocker.isCommandBlocked(outCommand, sender))
        {
            return true;
        }
        
        if (TFM_AdminList.isSuperAdmin(player) && !senderIsConsole)
        {
            if (!TFM_Util.isHighRank(sender_p))
            {
                TFM_Util.playerMsg(sender, ChatColor.RED + "You cannot gcmd other admins, stop trying to cause trouble!");
                return true;
            }
        }

        try
        {
            playerMsg("Sending command as " + player.getName() + ": " + outCommand);
            if (server.dispatchCommand(player, outCommand))
            {
                playerMsg("Command sent.");
            }
            else
            {
                playerMsg("Unknown error sending command.");
            }
        }
        catch (Throwable ex)
        {
            playerMsg("Error sending command: " + ex.getMessage());
        }

        return true;
    }
}
