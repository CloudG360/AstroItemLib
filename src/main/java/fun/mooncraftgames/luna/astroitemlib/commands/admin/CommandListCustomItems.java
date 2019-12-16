package fun.mooncraftgames.luna.astroitemlib.commands.admin;

import fun.mooncraftgames.luna.astroitemlib.AstroItemLib;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Arrays;
import java.util.Iterator;

public class CommandListCustomItems implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String[] pools = AstroItemLib.getAstroManager().getItems();
        String s = "";
        Iterator<String> i = Arrays.asList(pools).iterator();
        while(i.hasNext()){
            s = s.concat(i.next());
            if(i.hasNext()) s = s.concat(", ");
        }
        src.sendMessage(Text.of(TextColors.AQUA, TextStyles.BOLD, "ITEMS ", TextStyles.RESET, "Here are the available item templates: \n", TextColors.GRAY, s));
        return CommandResult.success();
    }
}
