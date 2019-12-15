package fun.mooncraftgames.luna.astroitemlib.commands.debug;

import com.google.gson.Gson;
import fun.mooncraftgames.luna.astroitemlib.items.ItemTemplate;
import fun.mooncraftgames.luna.astroitemlib.loot.SupplyLoot;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

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
public class CommandWriteDefaults implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        SupplyLoot supplyLoot = new SupplyLoot();
        supplyLoot.setToDefault();

        ItemTemplate item = new ItemTemplate();
        item.setDefault();

        Gson gson = new Gson();

        try {
            File f = new File("./generated_loottable.json");
            f.createNewFile();

            FileWriter w = new FileWriter(f);
            BufferedWriter write = new BufferedWriter(w);
            String string = gson.toJson(supplyLoot);
            write.write(string);
            write.close();
            src.sendMessage(Text.of(TextColors.GOLD, TextStyles.BOLD, "DEBUG ", TextStyles.RESET, "Created loot table file at "+f.getAbsolutePath()));

            File fi = new File("./generated_item.json");
            fi.createNewFile();

            FileWriter wi = new FileWriter(fi);
            BufferedWriter writei = new BufferedWriter(wi);
            String stringi = gson.toJson(item);
            writei.write(stringi);
            writei.close();
            src.sendMessage(Text.of(TextColors.GOLD, TextStyles.BOLD, "DEBUG ", TextStyles.RESET, "Created item file at "+fi.getAbsolutePath()));
        } catch (Exception err){
            err.printStackTrace();
        }

        return CommandResult.success();
    }
}
