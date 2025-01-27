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

                // Generate random noise for terrain height
                double randomNumber1 = random.nextDouble(); // Random value between 0.0 and 1.0
                double randomNumber2 = random.nextDouble(); // Another random value between 0.0 and 1.0
                double noise = Math.sin(worldX / 8.0) * Math.cos(worldZ / 8.0);

                // Height calculation based on the formula
                double heightFactor = (randomNumber1 * noise / 0.9 - 0.005 + randomNumber2);
                int height = (int) (heightFactor * 70 + 70); // Normalize and scale height

                // Ensure height is within valid bounds
                height = Math.max(1, Math.min(height, maxHeight - 1));

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
