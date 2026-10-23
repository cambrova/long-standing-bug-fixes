# Changelog

## 1.12.0

- MC-213961: the stem right under a big dripleaf's leaf can be swapped for another block without breaking the leaf, like every other stem.
- When Spam Kick Fix is also installed, this mod leaves the lag fix for chat and command spam limits (MC-276819) to it, so the limits don't cool down twice.

## 1.11.0

- MC-302519 (1.21.11 and newer): with Adventure as the default game mode, players respawn on the surface instead of at the bottom of the world.
- MC-298489: gliding ends when you're in water, so an elytra no longer wears out while you sink or swim.
- MC-308361 (26.2 and newer): the small cubes a bucketed sulfur cube splits into no longer despawn.
- MC-262182: mobs that fight in melee still hit a target in reach while another mob rides them.
- MC-99601: `/whitelist`, `/op`, `/ban` and other commands typed by a player or the console look up unknown player names in the background, so the server no longer freezes; the command finishes by itself once the name is known.

## 1.10.0

- MC-276819: spam limits for chat, commands and item drops cool down by real time, so a lagging server no longer kicks players for normal chat.
- MC-262810: wandering traders run from ravagers.
- MC-205843: shulkers can no longer get Levitation.
- MC-95782: iron golems no longer attack hostile mobs on their own team when they bump into them.
- MC-101851 (optional, off by default): snowballs, eggs and ender pearls no longer blow up end crystals.
- MC-163962 and MC-147694 are now optional and off by default on new installs. If you already have `config/long_standing_fixes.properties`, your settings stay as they are.

## 1.9.0

- MC-17876: health above 20 from health-boosting gear is no longer cut back to 20 when you log in or a chunk reloads.
- MC-111801 / MC-253476: item cooldowns (ender pearls, a disabled shield, goat horns) are saved with the player, and your game is told about them again after you change dimension.

## 1.8.0

- MC-164171 / MC-109083: when a wither skull or ghast fireball kills a player, its explosion no longer destroys the items they drop.

## 1.7.0

- MC-248588: with mobGriefing off, a burning mob no longer uses up water or powder snow in a cauldron (it still stops burning).
- MC-310859 (26.2 and newer): spears with Knockback knock players back too, not just mobs.

## 1.6.0

- MC-163962: a villager trade's demand stops at 0 instead of falling forever, so prices still go up when a trade is used a lot.
- MC-264285: lighting a creeper no longer uses up an unbreakable flint and steel.
- MC-179072: a swelling creeper calms down when its player switches to creative or spectator.
- MC-262422: thunderstorm lightning no longer aims at spectators.
- MC-153010: with doMobLoot off, foxes no longer drop what they carry in their mouth.

## 1.5.0

- MC-278572 (1.21.11 and newer): dropped items, TNT and minecarts slide down honey blocks at the 1.21.1 speed again, instead of almost twice as fast.
- MC-279003 (1.21.11 and newer): explosions push wind charges again, including other wind charges.
- MC-147694 (1.21.1): a mob that hits a raised shield is knocked back, like before 1.14.

## 1.4.0

- MC-265635: a cat sitting on the upper of two stacked chests no longer keeps the lower one shut.
- MC-135198: dispensers with flint and steel light a nether portal through any empty spot inside the frame, not only next to it.

## 1.3.0

- MC-210491: sculk sensors now hear a player batting a fireball away, like hitting any other entity.

## 1.2.0

- MC-186909: clicking a creeper that is already lit no longer uses up more flint and steel or fire charges.
- MC-261789: falling blocks that land fast below Y=0 turn into blocks instead of breaking into items.
- MC-269636: taking the wax off a lit copper bulb no longer awards "Lighten Up".

## 1.1.0

- MC-219854: spectators no longer take void damage.
- MC-168976 (1.21.1): a broken entity custom name is logged once, not on every spawner attempt.

## 1.0.0

- First release: MC-166272, MC-159069, MC-217397, MC-280273.
