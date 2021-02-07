# AstroItemLib [ARCHIVED]

An old Sponge API 7 based library written in Java 8 for adding custom item functions to items using data tags. The tags were stored in a string array named "FUNCTION_TAGS" in the item's data. In the latest version of the project, there are 12 tags in total:

- Catapult (Send entities flying upwards)
- Cookie (Give the holder a cookie)
- TestInventory (Some debug text in chat based of ItemTransaction clicks)
- Tracking (Debug Pitch+Yaw when held)
- Butterfingers (A random chance if dropping your item with this tag)
- Doubler (Doubles loot from blockbreak)
- ItemUseCooldown (Tell players that an item's tags are on cooldown; order mattered)
- SilentItemUseCooldown (Same as above but with no message in chat)
- Undroppable (Can't drop this- )
- Smelting (Smelted certain blockdrops)
- StopBreakBlock (Can't break blocks with the item)
- Unplacable (Block-type items can't be placed)

#### Important Note:
This plugin required a companion ***FORGE MOD*** to get around certain elements not being exposed in the API. This is [AstroForgeBridge](https://github.com/CloudG360/AstroForgeBridge)

---

### Reasons NOT to use the plugin:

- Efficiency was not a goal.
- It'll recieve no more updates.
- Truthfully, I never got the code working _perfectly_ so it's buggy.
- The project is somewhat big with the majority of the logic being in one file.
- Not up to my current standards.

Thus, there will be no more updates for this plugin as I wouldn't use this approach again. I don't have any immediate plans for a replacement but there may be elements from this plugin that can be reusued.

Please don't put all your events in one class <3
