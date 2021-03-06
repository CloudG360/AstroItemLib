package fun.mooncraftgames.luna.astroitemlib.commands.admin;

import fun.mooncraftgames.luna.astroitemlib.data.AstroKeys;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class CommandTestItemTag implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if(src instanceof Player){
            Player player = (Player) src;
            Optional<ItemStack> i = player.getItemInHand(HandTypes.MAIN_HAND);
            if(i.isPresent()){
                Optional<List<String>> data = i.get().get(AstroKeys.FUNCTION_TAGS);
                if(data.isPresent()){
                    src.sendMessage(Text.of(TextColors.DARK_GREEN, TextStyles.BOLD, "TAGS ", TextStyles.RESET, TextColors.GREEN, "Here are the item's tags:"));
                    for(String tg : data.get()){
                        String[] split = tg.split(Pattern.quote(":"), 2);
                        String s1 = split.length > 0 ? split[0] : "NULL_TAG_UH_OH";
                        String s2 = split.length > 1 ? split[1] : "(No Parameters)";
                        src.sendMessage(Text.of(TextColors.DARK_AQUA, s1, TextColors.DARK_GRAY, " : ", TextColors.AQUA, s2));
                    }
                } else {
                    src.sendMessage(Text.of(TextColors.DARK_RED, TextStyles.BOLD, "COOLDOWN ", TextStyles.RESET, TextColors.RED, "As this command uses inventories, this command source cannot use it."));
                }
            }
        } else {
            src.sendMessage(Text.of(TextColors.DARK_RED, TextStyles.BOLD, "COOLDOWN ", TextStyles.RESET, TextColors.RED, "As this command uses inventories, this command source cannot use it."));
        }

        return CommandResult.success();
    }

}
