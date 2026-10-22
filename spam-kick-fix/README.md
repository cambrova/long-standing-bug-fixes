# Spam Kick Fix

Server-side mod for Fabric and NeoForge (1.21.1, 1.21.11, 26.2, 26.3).

- **No more "Kicked for spamming" on a lagging server.** The chat and command limits cool down once per server tick, so when the server runs slow they cool down slower in real time and normal chat gets players kicked ([MC-276819](https://bugs.mojang.com/browse/MC-276819)). Here they cool down by the real time that passed.
- **1.21.1 and 1.21.11:** commands get their own limit instead of sharing chat's, like in 26.2, and both limits can be set in `config/spam_kick_fix.properties` (`chat-spam-threshold-seconds`, `command-spam-threshold-seconds`; 0 = never kick; vanilla is 10).
- **26.2 and newer:** the limits are already in `server.properties` under the same names.

Ops are never kicked for spam, in vanilla or with this mod.

If Long-Standing Bug Fixes is installed too, only one of the two does the real-time cool-down.

MIT licence. Source lives in this subfolder of the Long-Standing Bug Fixes repository.
