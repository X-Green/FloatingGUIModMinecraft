package dev.eeasee.gui_hanger.network;

import dev.eeasee.gui_hanger.sprites.renderer.AnvilSprite;
import dev.eeasee.gui_hanger.sprites.renderer.BaseSprite;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GUIHangerServer {

    public final MinecraftServer minecraftServer;
    public final GUIHangerServerNetworkHandler networkHandler;

    private final Int2ObjectMap<BaseSprite> spriteMap = new Int2ObjectOpenHashMap<>();
    private final Map<UUID, IntSet> player2AllocatedID = new HashMap<>();
    private final Int2ObjectMap<UUID> spriteID2PlayerUUIDMap = new Int2ObjectOpenHashMap<>();

    private final AtomicInteger topID = new AtomicInteger(0);

    public GUIHangerServer(MinecraftServer server) {
        this.minecraftServer = server;
        this.networkHandler = new GUIHangerServerNetworkHandler(server, this);
        AnvilSprite anvilSprite = new AnvilSprite(0);
        anvilSprite.setPos(new Vector3f(-60, 82, -216));
        anvilSprite.setItem(0, Items.WOODEN_SWORD);
        anvilSprite.setItem(1, Items.STONE_SWORD);
        anvilSprite.setItem(2, Items.IRON_SWORD);
        anvilSprite.setItem(3, Items.GOLDEN_SWORD);
        anvilSprite.setItem(4, Items.DIAMOND_SWORD);
        anvilSprite.setItem(8, Items.SUGAR);
        anvilSprite.setItem(9, Items.PINK_BED);
        anvilSprite.setItem(18, Items.GRASS_BLOCK);
        anvilSprite.setItem(27, Items.DIAMOND_BLOCK);
        anvilSprite.setItem(36, Items.DIAMOND_HELMET);
        anvilSprite.setItem(37, Items.ELYTRA);
        anvilSprite.setItem(38, Items.DIAMOND_LEGGINGS);
        anvilSprite.setItem(39, Items.DIAMOND_BOOTS);
        anvilSprite.setItem(40, Items.COOKED_BEEF);
        anvilSprite.setItem(41, Items.GUNPOWDER);
        anvilSprite.setCanFixItem(false);

        UUID randomUUID = UUID.randomUUID();
        this.addPlayer(randomUUID);
        this.addSpriteToPlayer(randomUUID, anvilSprite);
    }

    public void tick() {

    }

    public void onServerClosed() {
    }

    public int allocID() {
        return this.topID.getAndIncrement();
    }

    public void addPlayer(UUID uuid) {
        this.player2AllocatedID.put(uuid, new IntOpenHashSet());
    }

    public void addSpriteToPlayer(UUID uuid, BaseSprite sprite) {
        this.spriteMap.put(sprite.getID(), sprite);
        this.spriteID2PlayerUUIDMap.put(sprite.getID(), uuid);
    }

    public void removePlayer(UUID uuid) {
        for (int i : this.player2AllocatedID.get(uuid)) {
            this.spriteMap.remove(i);
        }
        this.player2AllocatedID.remove(uuid);
    }

    public Set<UUID> getAvailablePlayerUUIDs() {
        return this.player2AllocatedID.keySet();
    }

    public boolean containsPlayer(UUID uuid) {
        return this.player2AllocatedID.containsKey(uuid);
    }

    public IntSet getSpriteIDsFromPlayer(UUID uuid) {
        return this.player2AllocatedID.getOrDefault(uuid, IntSets.EMPTY_SET);
    }

    public BaseSprite getSpiritFromID(int id) {
        return this.spriteMap.get(id);
    }

    public UUID getPlayerUUIDBySpriteID(int id) {
        return this.spriteID2PlayerUUIDMap.get(id);
    }

    public Collection<BaseSprite> getAllSpriteObjects() {
        return this.spriteMap.values();
    }
}
