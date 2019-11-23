package io.cg360.secondmoon.astroitemlib.utilities;

import com.flowpowered.math.vector.Vector3d;
import io.cg360.secondmoon.astroitemlib.AstroItemLib;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.regex.Pattern;

public class Utils {

    /** Picks a random String out of an array.*/
    public static String pickRandomFromList(String[] list){
         Random random = new Random();
         return list[random.nextInt(list.length)];
    }

    /**
     * Previously used to calculate where an item should drop in
     * relation to a player.
     *
     * Deprecated due to it not working reliably. Will be fixed in
     * the future
     */
    @Deprecated
    public static Vector3d convertYawToWorld(double yaw, double distance){
        // +Z is 0
        // +X is -90

        // + 180 =
        // -X is 0
        // +Z is 90
        // +X is 180
        // -Z is 270

        // +Z 0, -x 90

        if(yaw > 360 || yaw < -360){
            //Out of bounds angle
            AstroItemLib.getLogger().info("Out of range yaw.");
            return new Vector3d(0, 1, 0);
        }

        double y = yaw;

        // Have 5 cases. -2 to 2 where the two extremes are the same.

        int q = (int) Math.floor(y/90);
        AstroItemLib.getLogger().info("Q: "+q);
        switch (q){
            case -3:
                // -x A | -z O | dist H
                double x3a = -(Math.cos(y+270)*distance);
                double z3a = -(Math.sin(y+270)*distance);
                return new Vector3d(x3a, 1, z3a);
            case -2:
                // x+ = O | z- = A | dist = H
                double xa = Math.sin(y+180)*distance;
                double za = -(Math.cos(y+180)*distance);
                return new Vector3d(xa, 1, za);
            case -1:
                // x+ A | z+ O | dist H
                double x1a = Math.cos(y+90)*distance;
                double z1a = Math.sin(y+90)*distance;
                return new Vector3d(-x1a, 1, -z1a);
            case 0:
                // z+ A | -x O | dist H
                double z2 = Math.cos(y)*distance;
                double x2 = -(Math.sin(y)*distance);
                return new Vector3d(z2, 1, x2);
            case 1:
                // -x A | -z O | dist H
                double x3 = -(Math.cos(y-90)*distance);
                double z3 = -(Math.sin(y-90)*distance);
                return new Vector3d(z3, 1, x3);
            case 2:
                // x+ = O | z- = A | dist = H
                double x = Math.sin(y-180)*distance;
                double z = -(Math.cos(y-180)*distance);
                return new Vector3d(z, 1, x);
            case 3:
                // x+ A | z+ O | dist H
                double x1 = Math.cos(y-270)*distance;
                double z1 = Math.sin(y-270)*distance;
                return new Vector3d(z1, 1, x1);
            default:
                return new Vector3d(0, 1, 0);
        }
    }

    public static String dataToMap(Map<?, ?> map){
        String string = "";
        for(Map.Entry<?, ?> entry : map.entrySet()){
            string = string.concat(entry.getKey().toString()+": "+entry.getValue().toString()+",\n");
        }
        return string;
    }

    /**
     * Drops an itemstack for a player.
     *
     */
    public static void dropItem (Location<World> loc, ItemStackSnapshot snapshot, int delay){

        //double yaw = player.getHeadRotation().getY();
        //Vector3d view = convertYawToWorld(yaw, distance);

        //Vector3d velocity = new Vector3d(0.2, 0.05, 0.2).mul(view);

        //AstroItemLib.getLogger().info(String.format("Yaw: %s, View: %s, Velocity: %s", yaw, view, velocity));

        Entity e = loc.getExtent().createEntity(EntityTypes.ITEM, loc.getPosition());
        e.offer(Keys.REPRESENTED_ITEM, snapshot);
        e.offer(Keys.PICKUP_DELAY, delay);
        try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
            frame.addContext(EventContextKeys.SPAWN_TYPE, SpawnTypes.PLUGIN);
            loc.getExtent().spawnEntity(e);
        }
        loc.getExtent().spawnParticles(
                ParticleEffect.builder()
                        .type(ParticleTypes.BARRIER)
                        .build(),
                loc.getPosition()
        );
        //e.setVelocity(new Vector3d(0.2, 0.2, 0.2).mul(d));
    }

    public static void givePlayerItem(UUID uuid, ItemStackSnapshot snapshot){
        if(uuid == null) return;
        if(snapshot == null) return;
        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
        Optional<User> u =  userStorage.get().get(uuid);
        if(!u.isPresent()){
            AstroItemLib.getLogger().error("Player @ "+uuid.toString()+" doesn't exist");
            return;
        }

        User user = u.get();

        if(user.isOnline()){
            Optional<Player> p = user.getPlayer();
            if(!p.isPresent()){
                AstroItemLib.getLogger().error("Player @ "+uuid.toString()+" is online but doesn't exist");
                return;
            }
            Player player = p.get();
            InventoryTransactionResult r = player.getInventory().offer(snapshot.createStack());
            if(r.getType() != InventoryTransactionResult.Type.SUCCESS){
                AstroItemLib.getLogger().error("Player @ "+player.getName()+" had a full inventory. Replacing offhand");
                player.setItemInHand(HandTypes.OFF_HAND, snapshot.createStack());
            }
        } else {
            InventoryTransactionResult r = user.getInventory().offer(snapshot.createStack());
            if(r.getType() != InventoryTransactionResult.Type.SUCCESS){
                AstroItemLib.getLogger().error("Player @ "+uuid.toString()+" had a full inventory. Replacing offhand");
                user.setItemInHand(HandTypes.OFF_HAND, snapshot.createStack());
            }
        }

    }

    public static void messageToWorld(World world, Text text){
        for(Player player:world.getPlayers()){
            player.sendMessage(text);
        }
    }

    public static void messageToServer(Text text){
        for(Player player: Sponge.getServer().getOnlinePlayers()){
            player.sendMessage(text);
        }
    }

    public static void soundToWorld(World world, SoundType type, double volume){
        for(Player player:world.getPlayers()){
            player.playSound(type, player.getPosition(), volume);
        }
    }

    public static String generateLocationID(Location<World> loc){
        return String.format("%s:%f,%f,%f", loc.getExtent(), Math.floor(loc.getX()), Math.floor(loc.getY()), Math.floor(loc.getZ()));
    }

    public static void fillInventory(Inventory inventory, ItemStack[] items){
        Iterator<Inventory> in = inventory.slots().iterator();
        int iterate = 0;

        for(ItemStack is:items) {
            if(in.hasNext()){
                InventoryTransactionResult t = in.next().set(is);
            }
        }
    }

    public static Object[] parseToSpongeString(String string){
        ArrayList<Object> objs = new ArrayList<Object>();
        String[] codes = string.split(Pattern.quote("&"));
        objs.add(codes[0]);
        for(int i = 1; i < codes.length; i++){
            if(codes[i] == null) continue;
            if(codes[i].equals("")) continue;
            switch (codes[i].toLowerCase().charAt(0)){
                case '0':
                    objs.add(TextColors.BLACK);
                    objs.add(codes[i].substring(1));
                    break;
                case '1':
                    objs.add(TextColors.DARK_BLUE);
                    objs.add(codes[i].substring(1));
                    break;
                case '2':
                    objs.add(TextColors.DARK_GREEN);
                    objs.add(codes[i].substring(1));
                    break;
                case '3':
                    objs.add(TextColors.DARK_AQUA);
                    objs.add(codes[i].substring(1));
                    break;
                case '4':
                    objs.add(TextColors.DARK_RED);
                    objs.add(codes[i].substring(1));
                    break;
                case '5':
                    objs.add(TextColors.DARK_PURPLE);
                    objs.add(codes[i].substring(1));
                    break;
                case '6':
                    objs.add(TextColors.GOLD);
                    objs.add(codes[i].substring(1));
                    break;
                case '7':
                    objs.add(TextColors.GRAY);
                    objs.add(codes[i].substring(1));
                    break;
                case '8':
                    objs.add(TextColors.DARK_GRAY);
                    objs.add(codes[i].substring(1));
                    break;
                case '9':
                    objs.add(TextColors.BLUE);
                    objs.add(codes[i].substring(1));
                    break;
                case 'a':
                    objs.add(TextColors.GREEN);
                    objs.add(codes[i].substring(1));
                    break;
                case 'b':
                    objs.add(TextColors.AQUA);
                    objs.add(codes[i].substring(1));
                    break;
                case 'c':
                    objs.add(TextColors.RED);
                    objs.add(codes[i].substring(1));
                    break;
                case 'd':
                    objs.add(TextColors.LIGHT_PURPLE);
                    objs.add(codes[i].substring(1));
                    break;
                case 'e':
                    objs.add(TextColors.YELLOW);
                    objs.add(codes[i].substring(1));
                    break;
                case 'f':
                    objs.add(TextColors.WHITE);
                    objs.add(codes[i].substring(1));
                    break;
                case 'k':
                    objs.add(TextStyles.OBFUSCATED);
                    objs.add(codes[i].substring(1));
                    break;
                case 'l':
                    objs.add(TextStyles.BOLD);
                    objs.add(codes[i].substring(1));
                    break;
                case 'm':
                    objs.add(TextStyles.STRIKETHROUGH);
                    objs.add(codes[i].substring(1));
                    break;
                case 'n':
                    objs.add(TextStyles.UNDERLINE);
                    objs.add(codes[i].substring(1));
                    break;
                case 'o':
                    objs.add(TextStyles.ITALIC);
                    objs.add(codes[i].substring(1));
                    break;
                case 'r':
                    objs.add(TextStyles.RESET);
                    objs.add(TextColors.RESET);
                    objs.add(codes[i].substring(1));
                    break;
                case 'x':
                    objs.add(String.valueOf(new Random().nextInt(1000000000)));
                    objs.add(codes[i].substring(1));
                    break;
                default:
                    objs.add(codes[i]);
                    break;
            }
        }
        return objs.toArray();
    }


}
