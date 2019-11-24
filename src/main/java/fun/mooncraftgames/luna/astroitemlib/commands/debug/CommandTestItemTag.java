package fun.mooncraftgames.luna.astroitemlib.commands.debug;

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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * (C) Copyright 2019 - Will Scully (CloudGamer360), All rights reserved
 *
 * By using the following application, you are accepting all responsibility
 * for any damage or legal issues which arise over it. Additionally, you are
 * agreeing not to use this application or it's components anywhere other
 * than the Mooncraft Minecraft Server unless you have written permission from
 * the copyright holder.
 *
 *
 * Failure to follow the license will result in a termination of license.
 */
public class CommandTestItemTag implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if(src instanceof Player){
            Player player = (Player) src;
            Optional<ItemStack> i = player.getItemInHand(HandTypes.MAIN_HAND);
            if(i.isPresent()){
                Optional<List<String>> data = i.get().get(AstroKeys.FUNCTION_TAGS);
                if(data.isPresent()){
                    src.sendMessage(Text.of(TextColors.GREEN, TextStyles.BOLD,  "TAGS ", TextStyles.RESET, Arrays.toString(data.get().toArray())));
                } else {
                    src.sendMessage(Text.of(TextColors.RED, "Data not present"));
                }
            }
        } else {
            src.sendMessage(Text.of(TextColors.RED, "You're not a player. This command uses inventories."));
        }

        return CommandResult.success();
    }

}
