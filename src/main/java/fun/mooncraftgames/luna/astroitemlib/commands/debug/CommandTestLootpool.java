package fun.mooncraftgames.luna.astroitemlib.commands.debug;

import fun.mooncraftgames.luna.astroitemlib.AstroItemLib;
import fun.mooncraftgames.luna.astroitemlib.loot.LootPool;
import fun.mooncraftgames.luna.astroitemlib.utilities.Utils;
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
import org.spongepowered.api.text.format.TextStyles;

import java.util.Optional;

public class CommandTestLootpool implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if(src instanceof Player){

            Optional<String> a =  args.getOne(Text.of("Loot Pool"));

            LootPool supplyLoot = new LootPool().setToDefault();

            if(a.isPresent()) {
                Optional<LootPool> p = AstroItemLib.getAstroManager().getPool(a.get());
                if(p.isPresent()) supplyLoot = p.get();
            }

            Player player = (Player) src;
            Inventory i = Inventory.builder().of(InventoryArchetypes.CHEST)
                    .property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(Text.of("Debug Crate")))
                    .build(AstroItemLib.get());
            ItemStack[] items = supplyLoot.rollLootPool(27);

            Utils.fillInventory(i, items);

            src.sendMessage(Text.of(TextColors.GOLD, TextStyles.BOLD, "DEBUG ", TextStyles.RESET, TextColors.YELLOW, "Bloop! There's a loot table!"));
            player.openInventory(i);
        } else {
            src.sendMessage(Text.of(TextColors.DARK_RED, TextStyles.BOLD, "COOLDOWN ", TextStyles.RESET, TextColors.RED, "As this command uses inventories, this command source cannot use it."));
        }

        return CommandResult.success();
    }

}
