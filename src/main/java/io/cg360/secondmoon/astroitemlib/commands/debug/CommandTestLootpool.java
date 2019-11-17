package io.cg360.secondmoon.astroitemlib.commands.debug;

import io.cg360.secondmoon.astroitemlib.AstroItemLib;
import io.cg360.secondmoon.astroitemlib.loot.SupplyLoot;
import io.cg360.secondmoon.astroitemlib.utilities.Utils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

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
public class CommandTestLootpool implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if(src instanceof Player){

            Optional<String> a =  args.getOne(Text.of("Loot Pool"));

            SupplyLoot supplyLoot = new SupplyLoot().setToDefault();

            if(a.isPresent()) {
                Optional<SupplyLoot> p = AstroItemLib.getAstroManager().getPool(a.get());
                if(p.isPresent()) supplyLoot = p.get();
            }

            Player player = (Player) src;
            Inventory i = Inventory.builder().of(InventoryArchetypes.CHEST)
                    .property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(Text.of("Debug Crate")))
                    .build(AstroItemLib.get());
            ItemStack[] items = supplyLoot.rollLootPool(27);

            Utils.fillInventory(i, items);

            player.openInventory(i);
        } else {
            src.sendMessage(Text.of(TextColors.RED, "You're not a player. This command uses inventories."));
        }

        return CommandResult.success();
    }

}
