package io.cg360.secondmoon.astroitemlib;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import io.cg360.secondmoon.astroitemlib.commands.admin.CommandListCustomItems;
import io.cg360.secondmoon.astroitemlib.commands.admin.CommandListPools;
import io.cg360.secondmoon.astroitemlib.commands.admin.CommandReloadPools;
import io.cg360.secondmoon.astroitemlib.commands.debug.CommandTestItemTag;
import io.cg360.secondmoon.astroitemlib.commands.debug.CommandTestLootpool;
import io.cg360.secondmoon.astroitemlib.commands.debug.CommandWriteDefaults;
import io.cg360.secondmoon.astroitemlib.commands.shop.CommandGiveTemplateItem;
import io.cg360.secondmoon.astroitemlib.data.AstroItemData;
import io.cg360.secondmoon.astroitemlib.data.AstroKeys;
import io.cg360.secondmoon.astroitemlib.data.ImmutableAstroItemData;
import io.cg360.secondmoon.astroitemlib.data.impl.AstroItemDataBuilder;
import io.cg360.secondmoon.astroitemlib.data.impl.AstroItemDataImpl;
import io.cg360.secondmoon.astroitemlib.data.impl.ImmutableAstroItemDataImpl;
import io.cg360.secondmoon.astroitemlib.exceptions.MalformedLootPoolException;
import io.cg360.secondmoon.astroitemlib.items.ItemTemplate;
import io.cg360.secondmoon.astroitemlib.loot.SupplyLoot;
import io.cg360.secondmoon.astroitemlib.managers.AstroItemManager;
import io.cg360.secondmoon.astroitemlib.managers.AstroTagManager;
import io.cg360.secondmoon.astroitemlib.managers.TaskManager;
import io.cg360.secondmoon.astroitemlib.tags.ExecutionTypes;
import io.cg360.secondmoon.astroitemlib.tags.TagPriority;
import io.cg360.secondmoon.astroitemlib.tags.impl.devtest.TagDevCatapult;
import io.cg360.secondmoon.astroitemlib.tags.impl.devtest.TagDevCookie;
import io.cg360.secondmoon.astroitemlib.tags.impl.devtest.TagDevTestInventory;
import io.cg360.secondmoon.astroitemlib.tags.impl.devtest.TagDevTracking;
import io.cg360.secondmoon.astroitemlib.tags.impl.item.TagButterfingers;
import io.cg360.secondmoon.astroitemlib.tags.impl.item.TagUndroppable;
import io.cg360.secondmoon.astroitemlib.tags.impl.world.TagStopBreakBlock;
import io.cg360.secondmoon.astroitemlib.tags.impl.world.TagUnplaceable;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.api.GriefPreventionApi;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Optional;

@Plugin(
        id = "astroitemlib",
        name = "AstroItemLib",
        description = "Implements Custom Item Tags, Templates, and Loot Pools (Loottables for Sponge)",
        authors = {
                "CloudGamer360"
        },
        dependencies = {
                @Dependency(id = "griefprevention", version = "4.0.3", optional = true),
                @Dependency(id = "astrofb", version = "2019.2-1.3.1", optional = false)
        }
)
public class AstroItemLib {

    private static AstroItemLib plg;

    @Inject private Logger logger;
    @Inject private PluginContainer pluginContainer;

    private DataRegistration<AstroItemData, ImmutableAstroItemData> R_ASTRO_ITEM_DATA;

    private AstroItemManager astroItemManager;
    private AstroTagManager astroTagManager;
    private TaskManager taskManager;

    private GriefPreventionApi griefPrevention;

    @Listener
    public void preServerInit(GamePreInitializationEvent event){
        // -- COMMAND REGISTERING --

        // Admin
        CommandSpec adminReloadAstro = CommandSpec.builder()
                .description(Text.of("Reloads all pools. Can be used to update pools without resetting the server."))
                .permission("astro.admin.reload")
                .executor(new CommandReloadPools())
                .build();
        CommandSpec adminListPools = CommandSpec.builder()
                .description(Text.of("Lists all active pools"))
                .permission("astro.admin.list.pools")
                .executor(new CommandListPools())
                .build();
        CommandSpec adminListItems = CommandSpec.builder()
                .description(Text.of("Lists all active custom items"))
                .permission("astro.admin.list.items")
                .executor(new CommandListCustomItems())
                .build();

        //Debug
        CommandSpec testItemTags = CommandSpec.builder()
                .description(Text.of("Debug command which shows an items AstroTags."))
                .permission("astro.debug.tags")
                .executor(new CommandTestItemTag())
                .build();
        CommandSpec testSupplyLoot = CommandSpec.builder()
                .description(Text.of("Debug command which shows loot table."))
                .arguments(
                        GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.string(Text.of("Loot Pool"))))
                )
                .permission("astro.debug.loottest")
                .executor(new CommandTestLootpool())
                .build();
        CommandSpec testJsonExport = CommandSpec.builder()
                .description(Text.of("Debug command which exports the default json loottable + item."))
                .permission("astro.debug.writedefaultjson")
                .executor(new CommandWriteDefaults())
                .build();

        // Shop
        CommandSpec shopGiveItem = CommandSpec.builder()
                .description(Text.of("Gives a player a custom item (Designed for a key) "))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("player"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("item template"))),
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("quantity")))
                )
                .permission("astro.shop.items")
                .executor(new CommandGiveTemplateItem())
                .build();

        // - Registering -
        Sponge.getCommandManager().register(this, adminReloadAstro, "reloadastro");
        Sponge.getCommandManager().register(this, adminListPools, "listpools");
        Sponge.getCommandManager().register(this, adminListItems, "listitems");

        Sponge.getCommandManager().register(this, testItemTags, "testitemtags");
        Sponge.getCommandManager().register(this, testSupplyLoot, "testlootpool");
        Sponge.getCommandManager().register(this, testJsonExport, "exportjsondefaults");

        Sponge.getCommandManager().register(this, shopGiveItem, "givecustomitem");
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        plg = this;
        astroItemManager = new AstroItemManager();
        astroTagManager = new AstroTagManager();
        taskManager = new TaskManager();

        taskManager.startTaskManager();

        resetPools();
        resetItemTemplates();

        try {
            this.griefPrevention = GriefPrevention.getApi();
        } catch (Exception err){
            this.griefPrevention = null;
        }

        // -- TAG REGISTERING --

        getAstroTagManager()
                .registerTag(new TagDevCookie("dev_cookie", TagPriority.NORMAL, ExecutionTypes.ITEM_USED))
                .registerTag(new TagDevCatapult("dev_catapult", TagPriority.HIGH, ExecutionTypes.ENTITY_HIT))
                .registerTag(new TagDevTestInventory("dev_test_click", TagPriority.NORMAL, ExecutionTypes.ITEM_CLICKED))
                .registerTag(new TagDevTracking("dev_tracking", TagPriority.NORMAL, ExecutionTypes.ITEM_HOLDING))

                //TODO: Add ability to ignore tag-blocking statements for stuff which should be ran last. "Append Tags" or something
                .registerTag(new TagUnplaceable("unplaceable", TagPriority.LOWEST, ExecutionTypes.BLOCK_CHANGE))
                .registerTag(new TagStopBreakBlock("stopbreakblock", TagPriority.LOWEST, ExecutionTypes.BLOCK_CHANGE))

                .registerTag(new TagButterfingers("butterfingers", TagPriority.HIGHEST, ExecutionTypes.ITEM_HOLD))
                .registerTag(new TagUndroppable("undroppable", TagPriority.HIGH, ExecutionTypes.ITEM_DROPPED));

        Sponge.getEventManager().registerListeners(this, astroTagManager);
    }

    @Listener
    public void onKeyRegistration(GameRegistryEvent.Register<Key<?>> event) {
        logger.info("<Items> Registering AstroBackbone Data Keys");
        AstroKeys.FUNCTION_TAGS = Key.builder()
                .type(new TypeToken<ListValue<String>>() {})
                .id("f_tags")
                .name("FTags")
                .query(DataQuery.of("ftags"))
                .build();
        logger.info("<Items> Keys Registered");
    }

    @Listener
    public void onDataRegistration(GameRegistryEvent.Register<DataRegistration<?, ?>> event){
        logger.info("<Items> Registering AstroBackbone Data");

        this.R_ASTRO_ITEM_DATA = DataRegistration.builder()
                .dataClass(AstroItemData.class)
                .immutableClass(ImmutableAstroItemData.class)
                .dataImplementation(AstroItemDataImpl.class)
                .immutableImplementation(ImmutableAstroItemDataImpl.class)
                .builder(new AstroItemDataBuilder())
                .dataName("AstroItem Tags")
                .manipulatorId("astroitemlib:f_tags")
                .buildAndRegister(this.pluginContainer);
        logger.info("<Items> Registered AstroBackbone Data");
    }

    public void resetPools(){
        getAstroItemManager().clearPoolDatabase();
        File lootDirectory = new File("./lootpools");
        if (!lootDirectory.isDirectory()) lootDirectory.mkdirs();

        File[] pools = lootDirectory.listFiles();

        if(!(pools == null)) {
            for (File pool : pools) {
                String json = "";
                // Stage: Read
                try {
                    FileReader reader = new FileReader(pool);
                    BufferedReader r = new BufferedReader(reader);
                    Iterator<String> i = r.lines().iterator();

                    while(i.hasNext()){
                        String next = i.next();
                        json = json.concat(next);
                    }
                    r.close();
                } catch (Exception err){
                    getLogger().info("An error occured while opening the loottable at "+pool.getAbsolutePath());
                    err.printStackTrace();
                    continue;
                }
                // Stage: Parse

                Gson gson = new Gson();
                SupplyLoot loot;
                try {
                    loot = gson.fromJson(json, SupplyLoot.class);
                    if(loot == null) throw new MalformedLootPoolException("The pool was empty? Actually write something in the json file.");
                } catch(JsonSyntaxException err){
                    getLogger().info("Malformed json in loottable at "+pool.getAbsolutePath());
                    continue;
                } catch (Exception err){
                    getLogger().info("An error occured while processing the loottable at "+pool.getAbsolutePath());
                    continue;
                }

                // Stage: Validate & Register

                try {
                    getAstroItemManager().registerLootPool(loot.getId(), loot);
                } catch (Exception err) {
                    err.printStackTrace();
                }

                //TODO: Validate rolls on register rather than on roll

            }
        }
    }

    public void resetItemTemplates(){
        getAstroItemManager().clearItemDatabase();
        File lootDirectory = new File("./itemtemplates");
        if (!lootDirectory.isDirectory()) lootDirectory.mkdirs();

        File[] pools = lootDirectory.listFiles();

        if(!(pools == null)) {
            for (File pool : pools) {
                String json = "";
                // Stage: Read
                try {
                    FileReader reader = new FileReader(pool);
                    BufferedReader r = new BufferedReader(reader);
                    Iterator<String> i = r.lines().iterator();

                    while(i.hasNext()){
                        String next = i.next();
                        json = json.concat(next);
                    }
                    r.close();
                } catch (Exception err){
                    getLogger().info("An error occured while opening the custom item at "+pool.getAbsolutePath());
                    err.printStackTrace();
                    continue;
                }
                // Stage: Parse

                Gson gson = new Gson();
                ItemTemplate loot;
                try {
                    loot = gson.fromJson(json, ItemTemplate.class);
                    if(loot == null) throw new MalformedLootPoolException("The custom item was empty? Actually write something in the json file.");
                } catch(JsonSyntaxException err){
                    getLogger().info("Malformed json in custom item at "+pool.getAbsolutePath());
                    continue;
                } catch (Exception err){
                    getLogger().info("An error occured while processing the custom item at "+pool.getAbsolutePath());
                    continue;
                }

                // Stage: Validate & Register

                try {
                    getAstroItemManager().registerCustomItem(loot.getUniqueID(), loot);
                } catch (Exception err) {
                    err.printStackTrace();
                }

            }
        }
    }

    public static AstroItemLib get() { return plg; }
    public static Logger getLogger() { return plg.getPlgLogger(); }
    public static PluginContainer getContainer() { return plg.getPlgContainer(); }
    public static AstroItemManager getAstroManager() { return plg.getAstroItemManager(); }
    public static AstroTagManager getTagManager() { return plg.getAstroTagManager(); }
    public static TaskManager getTaskManager() { return plg.getAstroTaskManager(); }
    public static Optional<GriefPreventionApi> getGriefPrevention() { return plg.getGriefPreventionApi(); }

    public Logger getPlgLogger() { return logger; }
    public PluginContainer getPlgContainer() { return pluginContainer; }
    public AstroItemManager getAstroItemManager() { return astroItemManager; }
    public AstroTagManager getAstroTagManager() { return astroTagManager; }
    public TaskManager getAstroTaskManager() { return taskManager; }
    public Optional<GriefPreventionApi> getGriefPreventionApi() { return Optional.ofNullable(griefPrevention); }
}
