package com.nzst.highnoiseworlds;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class HighNoiseGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunkData = createChunkData(world);
        int maxHeight = 256; // Minecraft height limit

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX * 16 + x;
                int worldZ = chunkZ * 16 + z;

                // Noise value increases or decreases every 8 blocks
                double noiseValue = Math.sin((worldX / 8.0) * Math.PI) * Math.cos((worldZ / 8.0) * Math.PI) * 20;
                int height = (int) (noiseValue + 70); // Base height + noise adjustment

                // Fill blocks up to the calculated height
                for (int y = 0; y <= height; y++) {
                    if (y == height) {
                        chunkData.setBlock(x, y, z, Material.GRASS_BLOCK); // Topsoil layer
                    } else if (y > height - 5) {
                        chunkData.setBlock(x, y, z, Material.DIRT); // Sub-layer
                    } else {
                        chunkData.setBlock(x, y, z, Material.STONE); // Base layer
                    }
                }

                // Add bedrock at the bottom
                chunkData.setBlock(x, 0, z, Material.BEDROCK);
            }
        }

        return chunkData;
    }
}
