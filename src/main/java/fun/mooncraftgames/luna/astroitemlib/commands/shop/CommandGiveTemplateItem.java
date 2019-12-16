package fun.mooncraftgames.luna.astroitemlib.commands.shop;

import fun.mooncraftgames.luna.astroitemlib.AstroItemLib;
import fun.mooncraftgames.luna.astroitemlib.items.ItemTemplate;
import fun.mooncraftgames.luna.astroitemlib.utilities.Utils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Optional;

public class CommandGiveTemplateItem implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Optional<String> itemplate = args.getOne(Text.of("Item Template"));
        Integer quantity = (Integer) args.getOne(Text.of("Quantity")).orElse(1);
        Optional<String> p = args.getOne(Text.of("Player Name"));

        String pname;

        if(p.isPresent()){
            pname = p.get();
        } else {
            if(src instanceof Player){
                pname = ((Player) src).getName();
            } else {
                src.sendMessage(Text.of(TextColors.DARK_RED, TextStyles.BOLD, "COOLDOWN ", TextStyles.RESET, TextColors.RED, "Non-players must specify a target (Player Name)"));
                return CommandResult.success();
            }
        }

        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
        Optional<User> u =  userStorage.get().get(pname);

        Optional<ItemTemplate> item = AstroItemLib.getAstroManager().getItem(itemplate.get());

        if(!u.isPresent()){
            AstroItemLib.getLogger().error(String.format("Player %s was not found when applying item '%s' x %s", pname, itemplate.get(), quantity) );
            throw new CommandException(Text.of("Player not found."));
        }
        if(!item.isPresent()){
            AstroItemLib.getLogger().error(String.format("Player %s didn't recieve the item '%s' x %s as it didn't exist.", pname, itemplate.get(), quantity) );
            throw new CommandException(Text.of("Item not found."));
        }

        for(int i = 0; i < quantity; i++) { Utils.givePlayerItem(u.get().getUniqueId(), item.get().generateStack(1).createSnapshot()); }

        return CommandResult.success();
    }
}
