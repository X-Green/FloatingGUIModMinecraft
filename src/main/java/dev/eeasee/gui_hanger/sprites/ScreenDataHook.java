package dev.eeasee.gui_hanger.sprites;

import net.minecraft.client.gui.screen.ingame.ContainerScreen;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class ScreenDataHook {
    private final static AtomicInteger HOOK_COUNT = new AtomicInteger();

    private final int id;

    private final SpriteType spriteType;

    private final ContainerScreen host;

    private HookPhase phase;

    private final Queue<SpriteEvent> eventQueue = new LinkedList<>();

    public ScreenDataHook(SpriteType spriteType, ContainerScreen host) {
        this.id = HOOK_COUNT.getAndIncrement();
        this.spriteType = spriteType;
        this.host = host;
        this.phase = HookPhase.UNCHECKED;
    }

    public int getId() {
        return this.id;
    }

    public void abandon() {

    }

    private enum HookPhase {
        UNATTACHED,
        UNCHECKED,
        ACTIVE,
        DEAD
    }
}
