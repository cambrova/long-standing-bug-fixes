package io.github.traelmyx.mojirafixpack.test;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;

import java.nio.file.Files;

// Run with -Dsmoketest=true: creates a flat world, puts food on a campfire read by a comparator, takes a screenshot and quits.
public final class ClientSmoke {

    public static final boolean ENABLED = Boolean.getBoolean("smoketest");
    private static final long TIMEOUT_MS = 5 * 60 * 1000;

    private static int stage;
    private static int ticks;

    private ClientSmoke() {
    }

    public static boolean start() {
        if (!ENABLED) {
            return false;
        }
        Thread watchdog = new Thread(() -> {
            try {
                Thread.sleep(TIMEOUT_MS);
                fail("timed out");
            } catch (InterruptedException ignored) {
            }
        }, "smoketest-watchdog");
        watchdog.setDaemon(true);
        watchdog.start();
        return true;
    }

    public static void tick(Minecraft mc) {
        try {
            switch (stage) {
                case 0 -> {
                    Screen screen = SmokeCompat.screen(mc);
                    if (screen != null && SmokeCompat.overlayHidden(mc) && ++ticks >= 20) {
                        if (screen.getClass().getSimpleName().equals("LoadingErrorScreen")) {
                            fail("mod loading errors");
                        }
                        ticks = 0;
                        mc.options.tutorialStep = net.minecraft.client.tutorial.TutorialSteps.NONE;
                        SmokeCompat.createFlatWorld(mc, GameType.CREATIVE);
                        stage = 1;
                    }
                }
                case 1 -> {
                    if (mc.level != null && mc.player != null && SmokeCompat.screen(mc) == null) {
                        stage = 2;
                    }
                }
                case 2 -> {
                    ticks++;
                    if (ticks == 40) {
                        fire(mc);
                    }
                    if (ticks == 90) {
                        SmokeCompat.screenshot(mc, "campfire.png");
                    }
                    if (ticks >= 100) {
                        var server = mc.getSingleplayerServer();
                        // block entities can only be read on the server thread
                        int signal = server.submit(() -> ((ComparatorBlockEntity) server.overworld().getBlockEntity(comparatorPos)).getOutputSignal()).join();
                        if (signal != 8) {
                            fail("comparator next to a campfire with two items gives " + signal + ", expected 8");
                        }
                        Files.writeString(mc.gameDirectory.toPath().resolve("smoke-ok"), "ok\n");
                        stage = 3;
                        mc.stop();
                    }
                }
                default -> {
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            fail(t.toString());
        }
    }

    private static volatile net.minecraft.core.BlockPos comparatorPos;

    // an unlit campfire with two pieces of beef, a comparator reading it and a lamp it lights
    private static void fire(Minecraft mc) {
        var server = mc.getSingleplayerServer();
        server.execute(() -> {
            var player = server.getPlayerList().getPlayers().get(0);
            var level = server.overworld();
            var base = player.blockPosition().offset(0, 0, 3);
            level.setBlockAndUpdate(base, Blocks.CAMPFIRE.defaultBlockState().setValue(CampfireBlock.LIT, false));
            comparatorPos = base.south();
            level.setBlockAndUpdate(comparatorPos, Blocks.COMPARATOR.defaultBlockState().setValue(ComparatorBlock.FACING, Direction.NORTH));
            level.setBlockAndUpdate(comparatorPos.south(), Blocks.REDSTONE_LAMP.defaultBlockState());
            CampfireBlockEntity campfire = (CampfireBlockEntity) level.getBlockEntity(base);
            campfire.getItems().set(0, new ItemStack(Items.BEEF));
            campfire.getItems().set(2, new ItemStack(Items.BEEF));
            campfire.setChanged();
            level.sendBlockUpdated(base, level.getBlockState(base), level.getBlockState(base), 3);
        });
    }

    public static void fail(String why) {
        System.err.println("Smoke test failed: " + why);
        System.out.println("[clientsmoke] FAILED: " + why);
        System.err.flush();
        Runtime.getRuntime().halt(1);
    }
}
