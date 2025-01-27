package com.nzst.highnoiseworlds;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;

import java.util.ArrayList;
import java.util.List;
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
                double noise = generatePerlinNoise(worldX, worldZ, random); // Perlin noise
                double mountainNoise = generateMountainNoise(worldX, worldZ, random); // Mountain noise

                // Add mountain influence
                double heightFactor = noise + mountainNoise * 0.5; // Amplify mountain contribution
                heightFactor = Math.max(0, Math.min(heightFactor, 1.0)); // Clamp between 0 and 1
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

    // Generate Perlin noise for smooth terrain
    private double generatePerlinNoise(int x, int z, Random random) {
        // Simulate Perlin noise generation (replace with actual implementation as needed)
        return random.nextDouble();
    }

    // Generate additional noise for mountainous regions
    private double generateMountainNoise(int x, int z, Random random) {
        // Add noise with lower frequency for mountains
        return random.nextDouble() * 0.7; // Mountain regions are slightly elevated
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        List<BlockPopulator> populators = new ArrayList<>();
        populators.add(new TreePopulator()); // Add trees
        populators.add(new MobSpawner());   // Add mobs
        return populators;
    }

    // TreePopulator to add trees
    private static class TreePopulator extends BlockPopulator {

        @Override
        public void populate(World world, Random random, Chunk chunk) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    int worldX = chunk.getX() * 16 + x;
                    int worldZ = chunk.getZ() * 16 + z;
                    int highestY = world.getHighestBlockYAt(worldX, worldZ);

                    // Chance to spawn a tree
                    if (random.nextInt(10) == 0) { // 1 in 10 chance
                        generateTree(world, random, worldX, highestY, worldZ);
                    }
                }
            }
        }

        private void generateTree(World world, Random random, int x, int y, int z) {
            int treeHeight = 4 + random.nextInt(3); // Random height between 4 and 6 blocks

            // Generate trunk
            for (int i = 0; i < treeHeight; i++) {
                if (y + i < 256) { // Ensure the trunk doesn't exceed the world height limit
                    world.getBlockAt(x, y + i, z).setType(Material.OAK_LOG);
                }
            }

            // Generate leaves
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    for (int dy = treeHeight - 2; dy <= treeHeight; dy++) {
                        // Make leaves appear around the top of the trunk
                        if (Math.abs(dx) + Math.abs(dz) + Math.abs(dy - treeHeight) <= 3) {
                            if (y + dy < 256) { // Ensure leaves don't exceed the height limit
                                world.getBlockAt(x + dx, y + dy, z + dz).setType(Material.OAK_LEAVES);
                            }
                        }
                    }
                }
            }
        }
    }

    // MobSpawner to add mobs
    private static class MobSpawner extends BlockPopulator {

        @Override
        public void populate(World world, Random random, Chunk chunk) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    int worldX = chunk.getX() * 16 + x;
                    int worldZ = chunk.getZ() * 16 + z;
                    int highestY = world.getHighestBlockYAt(worldX, worldZ);

                    // Spawn mobs randomly on the surface
                    if (random.nextInt(20) == 0) { // 1 in 20 chance
                        Location spawnLocation = new Location(world, worldX, highestY + 1, worldZ);
                        EntityType[] mobs = {EntityType.ZOMBIE, EntityType.SKELETON, EntityType.COW, EntityType.SHEEP};
                        EntityType mobType = mobs[random.nextInt(mobs.length)];
                        world.spawnEntity(spawnLocation, mobType);
                    }
                }
            }
        }
    }
}
