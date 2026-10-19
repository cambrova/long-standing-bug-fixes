# Long-Standing Bug Fixes

Fixes for confirmed, still-open bugs from Mojang's bug tracker. Each fix is small, does what the bug report asks for, and has its own switch in `config/long_standing_fixes.properties`.

| Bug | Fix | Versions |
|---|---|---|
| [MC-166272](https://bugs.mojang.com/browse/MC-166272) | Comparators read campfires like a 4-slot container (4, 8, 11, 15) | all |
| [MC-159069](https://bugs.mojang.com/browse/MC-159069) | Baby villagers walk to an iron golem offering a poppy and take it | all |
| [MC-217397](https://bugs.mojang.com/browse/MC-217397) | Skeletons on fire shoot burning arrows again (1.10 rule) | all |
| [MC-280273](https://bugs.mojang.com/browse/MC-280273) | Wind charges push dropped items again | 1.21.5+ |
| [MC-219854](https://bugs.mojang.com/browse/MC-219854) | Spectators take no void damage | all |
| [MC-168976](https://bugs.mojang.com/browse/MC-168976) | A broken entity custom name is logged once, not on every spawner attempt | 1.21.1 |
| [MC-186909](https://bugs.mojang.com/browse/MC-186909) | Clicking a lit creeper doesn't use up more flint and steel or fire charges | all |
| [MC-261789](https://bugs.mojang.com/browse/MC-261789) | Falling blocks that land fast below Y=0 turn into blocks instead of breaking | all |
| [MC-269636](https://bugs.mojang.com/browse/MC-269636) | Taking the wax off a lit copper bulb doesn't award "Lighten Up" | all |
| [MC-210491](https://bugs.mojang.com/browse/MC-210491) | Sculk sensors hear a player batting a fireball away | all |
| [MC-265635](https://bugs.mojang.com/browse/MC-265635) | A cat sitting on a chest only keeps that chest shut, not the one below | all |
| [MC-135198](https://bugs.mojang.com/browse/MC-135198) | Dispensers with flint and steel light a portal through any empty spot inside the frame | all |
| [MC-278572](https://bugs.mojang.com/browse/MC-278572) | Items, TNT and minecarts slide down honey at the 1.21.1 speed | 1.21.2+ |
| [MC-279003](https://bugs.mojang.com/browse/MC-279003) | Explosions push wind charges | 1.21.4+ |
| [MC-147694](https://bugs.mojang.com/browse/MC-147694) | Mobs that hit a raised shield are knocked back | 1.21.1 |
| [MC-163962](https://bugs.mojang.com/browse/MC-163962) | Villager trade demand stops at 0 instead of falling forever | all |
| [MC-264285](https://bugs.mojang.com/browse/MC-264285) | Lighting a creeper doesn't use up an unbreakable flint and steel | all |
| [MC-179072](https://bugs.mojang.com/browse/MC-179072) | Creepers calm down when their player switches to creative or spectator | all |
| [MC-262422](https://bugs.mojang.com/browse/MC-262422) | Thunderstorm lightning doesn't aim at spectators | all |
| [MC-153010](https://bugs.mojang.com/browse/MC-153010) | Foxes follow doMobLoot | all |
| [MC-248588](https://bugs.mojang.com/browse/MC-248588) | Burning mobs don't use up cauldrons with mobGriefing off | all |
| [MC-310859](https://bugs.mojang.com/browse/MC-310859) | Spears with Knockback knock players back | 26.1+ |

Server side (or singleplayer); clients don't need it.

Tests: every fix has a GameTest for the fix and a control test that runs the same setup on vanilla behaviour and checks the bug is still there, on 1.21.1, 1.21.11, 26.2 and 26.3 for NeoForge and Fabric. A client smoke test builds a campfire with a comparator and takes a screenshot.

MIT licensed.
