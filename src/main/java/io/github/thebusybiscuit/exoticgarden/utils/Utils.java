package io.github.thebusybiscuit.exoticgarden.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Rotatable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URI;
import java.util.Random;
import java.util.UUID;

public class Utils {
    private Utils() {}

    public static ItemStack fromBase64Hash(String hash) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID(), null);
        PlayerTextures textures = profile.getTextures();


        try {
            textures.setSkin(URI.create("http://textures.minecraft.net/texture/" + hash).toURL());
        } catch (Exception e) {
            // nothing
        }

        profile.setTextures(textures);
        meta.setOwnerProfile(profile);
        head.setItemMeta(meta);

        return head;
    }

    public static void placeRotatedSkull(Block block, String textureHash, BlockFace[] faces, Random random) {
        block.setType(Material.PLAYER_HEAD, false);
        Rotatable rot = (Rotatable) block.getBlockData();
        rot.setRotation(faces[random.nextInt(faces.length)]);
        block.setBlockData(rot);

        try {
            ItemStack head = Utils.fromBase64Hash(textureHash);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            Skull skull = (Skull) block.getState();
            skull.setOwnerProfile(meta.getOwnerProfile());
            skull.update(true, false);
        } catch (Exception ignored) {}
    }

    public static void placeRotatedSkullLater(Plugin plugin, Block block, String textureHash, BlockFace[] faces, Random random) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
                () -> placeRotatedSkull(block, textureHash, faces, random)
        );
    }

    public static void applySkinToExistingSkull(Block block, String textureHash) {
        try {
            ItemStack head = fromBase64Hash(textureHash);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            Skull skull = (Skull) block.getState();
            skull.setOwnerProfile(meta.getOwnerProfile());
            skull.update(true, false);
        } catch (Exception ignored) {}
    }
}
