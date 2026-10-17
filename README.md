# Mojira Fix Pack

Fixes for confirmed, still-open bugs from Mojang's bug tracker. Each fix is small, does what the bug report asks for, and has its own switch in `config/mojira_fix_pack.properties`.

| Bug | Fix | Versions |
|---|---|---|
| [MC-166272](https://bugs.mojang.com/browse/MC-166272) | Comparators read campfires like a 4-slot container (4, 8, 11, 15) | all |
| [MC-159069](https://bugs.mojang.com/browse/MC-159069) | Baby villagers walk to an iron golem offering a poppy and take it | all |
| [MC-217397](https://bugs.mojang.com/browse/MC-217397) | Skeletons on fire shoot burning arrows again (1.10 rule) | all |
| [MC-280273](https://bugs.mojang.com/browse/MC-280273) | Wind charges push dropped items again | 1.21.5+ |

Server side (or singleplayer); clients don't need it.

Tests: every fix has a GameTest for the fix and a control test that runs the same setup on vanilla behaviour and checks the bug is still there, on 1.21.1, 1.21.11, 26.2 and 26.3 for NeoForge and Fabric. A client smoke test builds a campfire with a comparator and takes a screenshot.

MIT licensed.
