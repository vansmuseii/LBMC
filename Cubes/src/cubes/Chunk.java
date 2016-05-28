/**
 * *********************************************************************
 *      file: Chunk.java
 *      author: Frank Lapena, Chun Ho, Van Muse
 *      class: CS 445 - Computer Graphics
 *
 *      assignment: Quarter Project CP#1
 *      date last modified: 05/20/2016
 *
 *      purpose: This class creates the textures of the blocks.
 *
 **********************************************************************
 */
package cubes;

import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Chunk {

    static final int CHUNK_SIZE = 30;
    static final int CUBE_LENGTH = 2;
    private Block[][][] Blocks;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private int VBOTextureHandle;
    private int StartX, StartY, StartZ;
    private Random r;
    private Texture texture;

    public void render() {
        glPushMatrix();
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glColorPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBindTexture(GL_TEXTURE_2D, 1);
        glTexCoordPointer(2, GL_FLOAT, 0, 0L);
        glDrawArrays(GL_QUADS, 0, CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);
        glPopMatrix();
    }

    public void rebuildMesh(float startX, float startY, float startZ) {
        SimplexNoise noise = new SimplexNoise(50, 0.07, r.nextInt());
        int h;
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        for (float x = 0; x < CHUNK_SIZE; x++) {
            for (float z = 0; z < CHUNK_SIZE; z++) {
                h = Math.abs(StartY + (int) (100 * noise.getNoise((int) x, (int) z)) * CUBE_LENGTH);

                for (float y = 0; y <= h; y++) {
                    //Generate Grass on general top
                    if (y == h && y != 0) {
                        Blocks[(int) x][h][(int) z] = new Block(Block.BlockType.BlockType_Grass);
                        Blocks[(int) x][(int) y][(int) z].SetActive(true);
                        
                        
                        //Generate Tree
                        int WoodLeafHeight;
                        if (r.nextFloat() > 0.95f) {
                            WoodLeafHeight = (int)y + r.nextInt(5) + 4;
                            for (int k = (int) y + 1; k < WoodLeafHeight; k++) {
                                Blocks[(int) x][k][(int) z] = new Block(Block.BlockType.BlockType_Wood);

                                Blocks[(int) x][k][(int) z].SetActive(true);
                                VertexPositionData.put(createCube((float) (startX + x * CUBE_LENGTH),
                                        (float) (k * CUBE_LENGTH + (int) (CHUNK_SIZE * .8)),
                                        (float) (startZ + z * CUBE_LENGTH)));
                                VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[(int) x][(int) k][(int) z])));
                                VertexTextureData.put(createTexCube((float) 0, (float) 0, Blocks[(int) x][k][(int) z]));
                            }
                            
                            //Generate Wood leaves
                            Blocks[(int) x][WoodLeafHeight][(int) z] = new Block(Block.BlockType.BlockType_WoodLeave);

                            Blocks[(int) x][WoodLeafHeight][(int) z].SetActive(true);
                            VertexPositionData.put(createCube((float) (startX + x * CUBE_LENGTH),
                                    (float) (WoodLeafHeight * CUBE_LENGTH + (int) (CHUNK_SIZE * .8)),
                                    (float) (startZ + z * CUBE_LENGTH)));
                            VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[(int) x][WoodLeafHeight][(int) z])));
                            VertexTextureData.put(createTexCube((float) 0, (float) 0, Blocks[(int) x][WoodLeafHeight][(int) z]));
                            
                            //Generate Leaves
                            for(int a = (int)x - 1; a <= (int)x + 1; a++){
                                for(int b = WoodLeafHeight - 1; b <= WoodLeafHeight + 1; b++){
                                    for(int c = (int)z - 1; c <= (int)z + 1; c++){
                                        
                                        if(a >= 0 && a < CHUNK_SIZE && c >= 0 && c < CHUNK_SIZE && b < CHUNK_SIZE){
                                            
                                            if (!Blocks[a][b][c].IsActive()) {
                                                Blocks[a][b][c] = new Block(Block.BlockType.BlockType_Leaves);

                                                Blocks[a][b][c].SetActive(true);
                                                VertexPositionData.put(createCube((float) (startX + a * CUBE_LENGTH),
                                                        (float) (b * CUBE_LENGTH + (int) (CHUNK_SIZE * .8)),
                                                        (float) (startZ + c * CUBE_LENGTH)));
                                                VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[a][b][c])));
                                                VertexTextureData.put(createTexCube((float) 0, (float) 0, Blocks[a][b][c]));
                                            }
                                            
                                        }
                                        
                                        
                                        
                                    }
                                }
                            }
                            
                            
                            
                                                   
                            
                            
                        }
                        
                        
                        
                        
                        

                        //Generate Sand
                        if (y == 2) {
                            Blocks[(int) x][2][(int) z] = new Block(Block.BlockType.BlockType_Sand);
                            Blocks[(int) x][(int) y][(int) z].SetActive(true);
                        }
                    }

                    VertexPositionData.put(createCube((float) (startX + x * CUBE_LENGTH),
                            (float) (y * CUBE_LENGTH + (int) (CHUNK_SIZE * .8)),
                            (float) (startZ + z * CUBE_LENGTH)));
                    VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[(int) x][(int) y][(int) z])));
                    VertexTextureData.put(createTexCube((float) 0, (float) 0, Blocks[(int) (x)][(int) (y)][(int) (z)]));
                }

                //Generate water
                if (!Blocks[(int) x][1][(int) z].IsActive()) {

                    for (int i = 1; i < 3; i++) {
                        Blocks[(int) x][i][(int) z] = new Block(Block.BlockType.BlockType_Water);

                        Blocks[(int) x][i][(int) z].SetActive(true);
                        VertexPositionData.put(createCube((float) (startX + x * CUBE_LENGTH),
                                (float) (i * CUBE_LENGTH + (int) (CHUNK_SIZE * .8)),
                                (float) (startZ + z * CUBE_LENGTH)));
                        VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[(int) x][i][(int) z])));
                        VertexTextureData.put(createTexCube((float) 0, (float) 0, Blocks[(int) (x)][i][(int) (z)]));

                    }
                }
            }
        }
        VertexColorData.flip();
        VertexPositionData.flip();
        VertexTextureData.flip();
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private float[] createCubeVertexCol(float[] CubeColorArray) {
        float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
        for (int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = CubeColorArray[i % CubeColorArray.length];
        }
        return cubeColors;
    }

    public static float[] createCube(float x, float y, float z) {
        int offset = CUBE_LENGTH / 2;
        return new float[]{
            // TOP QUAD
            x + offset, y + offset, z,
            x - offset, y + offset, z,
            x - offset, y + offset, z - CUBE_LENGTH,
            x + offset, y + offset, z - CUBE_LENGTH,
            // BOTTOM QUAD
            x + offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z,
            x + offset, y - offset, z,
            // FRONT QUAD
            x + offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            // BACK QUAD
            x + offset, y - offset, z,
            x - offset, y - offset, z,
            x - offset, y + offset, z,
            x + offset, y + offset, z,
            // LEFT QUAD
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z,
            x - offset, y - offset, z,
            x - offset, y - offset, z - CUBE_LENGTH,
            // RIGHT QUAD
            x + offset, y + offset, z,
            x + offset, y + offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z
        };
    }

    private float[] getCubeColor(Block block) {
        return new float[]{1, 1, 1};
    }

    public Chunk(int startX, int startY, int startZ) {
        try {
            texture = TextureLoader.getTexture("PNG",
                    ResourceLoader.getResourceAsStream("terrain.png"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        r = new Random();
        Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {

                    if (y == 0) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Bedrock);
                        Blocks[x][y][z].SetActive(true);
                    } else if (r.nextFloat() > 0.1f) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                    } else if (r.nextFloat() > 0.0f) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Stone);
                    } else {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                    }
                }
            }
        }
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        StartX = startX;
        StartY = startY;
        StartZ = startZ;

        rebuildMesh(startX, startY, startZ);

    }

    public static float[] createTexCube(float x, float y, Block block) {
        float offset = (1024f / 16) / 1024f;

        switch (block.GetID()) {
            case 0: //Grass
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 3, y + offset * 10,
                    x + offset * 2, y + offset * 10,
                    x + offset * 2, y + offset * 9,
                    x + offset * 3, y + offset * 9,
                    // TOP
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // FRONT QUAD
                    x + offset * 3, y + offset * 0,
                    x + offset * 4, y + offset * 0,
                    x + offset * 4, y + offset * 1,
                    x + offset * 3, y + offset * 1,
                    // BACK QUAD
                    x + offset * 4, y + offset * 1,
                    x + offset * 3, y + offset * 1,
                    x + offset * 3, y + offset * 0,
                    x + offset * 4, y + offset * 0,
                    // LEFT QUAD
                    x + offset * 3, y + offset * 0,
                    x + offset * 4, y + offset * 0,
                    x + offset * 4, y + offset * 1,
                    x + offset * 3, y + offset * 1,
                    // RIGHT QUAD
                    x + offset * 3, y + offset * 0,
                    x + offset * 4, y + offset * 0,
                    x + offset * 4, y + offset * 1,
                    x + offset * 3, y + offset * 1};

            case 1: //Sand
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 3, y + offset * 2,
                    x + offset * 2, y + offset * 2,
                    x + offset * 2, y + offset * 1,
                    x + offset * 3, y + offset * 1,
                    // TOP
                    x + offset * 3, y + offset * 2,
                    x + offset * 2, y + offset * 2,
                    x + offset * 2, y + offset * 1,
                    x + offset * 3, y + offset * 1,
                    // FRONT QUAD
                    x + offset * 3, y + offset * 2,
                    x + offset * 2, y + offset * 2,
                    x + offset * 2, y + offset * 1,
                    x + offset * 3, y + offset * 1,
                    // BACK QUAD
                    x + offset * 3, y + offset * 2,
                    x + offset * 2, y + offset * 2,
                    x + offset * 2, y + offset * 1,
                    x + offset * 3, y + offset * 1,
                    // LEFT QUAD
                    x + offset * 3, y + offset * 2,
                    x + offset * 2, y + offset * 2,
                    x + offset * 2, y + offset * 1,
                    x + offset * 3, y + offset * 1,
                    // RIGHT QUAD
                    x + offset * 3, y + offset * 2,
                    x + offset * 2, y + offset * 2,
                    x + offset * 2, y + offset * 1,
                    x + offset * 3, y + offset * 1};

            case 2: //Water
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 15, y + offset * 13,
                    x + offset * 14, y + offset * 13,
                    x + offset * 14, y + offset * 12,
                    x + offset * 15, y + offset * 12,
                    // TOP
                    x + offset * 15, y + offset * 13,
                    x + offset * 14, y + offset * 13,
                    x + offset * 14, y + offset * 12,
                    x + offset * 15, y + offset * 12,
                    // FRONT QUAD
                    x + offset * 15, y + offset * 13,
                    x + offset * 14, y + offset * 13,
                    x + offset * 14, y + offset * 12,
                    x + offset * 15, y + offset * 12,
                    // BACK QUAD
                    x + offset * 15, y + offset * 13,
                    x + offset * 14, y + offset * 13,
                    x + offset * 14, y + offset * 12,
                    x + offset * 15, y + offset * 12,
                    // LEFT QUAD
                    x + offset * 15, y + offset * 13,
                    x + offset * 14, y + offset * 13,
                    x + offset * 14, y + offset * 12,
                    x + offset * 15, y + offset * 12,
                    // RIGHT QUAD
                    x + offset * 15, y + offset * 13,
                    x + offset * 14, y + offset * 13,
                    x + offset * 14, y + offset * 12,
                    x + offset * 15, y + offset * 12};
            case 3: //Dirt
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // TOP
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // FRONT QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // BACK QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // LEFT QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // RIGHT QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0};

            case 4: //Stone
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 2, y + offset * 1,
                    x + offset * 1, y + offset * 1,
                    x + offset * 1, y + offset * 0,
                    x + offset * 2, y + offset * 0,
                    // TOP
                    x + offset * 2, y + offset * 1,
                    x + offset * 1, y + offset * 1,
                    x + offset * 1, y + offset * 0,
                    x + offset * 2, y + offset * 0,
                    // FRONT QUAD
                    x + offset * 2, y + offset * 1,
                    x + offset * 1, y + offset * 1,
                    x + offset * 1, y + offset * 0,
                    x + offset * 2, y + offset * 0,
                    // BACK QUAD
                    x + offset * 2, y + offset * 1,
                    x + offset * 1, y + offset * 1,
                    x + offset * 1, y + offset * 0,
                    x + offset * 2, y + offset * 0,
                    // LEFT QUAD
                    x + offset * 2, y + offset * 1,
                    x + offset * 1, y + offset * 1,
                    x + offset * 1, y + offset * 0,
                    x + offset * 2, y + offset * 0,
                    // RIGHT QUAD
                    x + offset * 2, y + offset * 1,
                    x + offset * 1, y + offset * 1,
                    x + offset * 1, y + offset * 0,
                    x + offset * 2, y + offset * 0};

            case 5: //Bedrock
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    // TOP
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    // FRONT QUAD
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    // BACK QUAD
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    // LEFT QUAD
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    // RIGHT QUAD
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1};
            case 6: //Wood
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 6, y + offset * 2,
                    x + offset * 5, y + offset * 2,
                    x + offset * 5, y + offset * 1,
                    x + offset * 6, y + offset * 1,
                    // TOP
                    x + offset * 6, y + offset * 2,
                    x + offset * 5, y + offset * 2,
                    x + offset * 5, y + offset * 1,
                    x + offset * 6, y + offset * 1,
                    // FRONT QUAD
                    x + offset * 5, y + offset * 2,
                    x + offset * 4, y + offset * 2,
                    x + offset * 4, y + offset * 1,
                    x + offset * 5, y + offset * 1,
                    // BACK QUAD
                    x + offset * 5, y + offset * 2,
                    x + offset * 4, y + offset * 2,
                    x + offset * 4, y + offset * 1,
                    x + offset * 5, y + offset * 1,
                    // LEFT QUAD
                    x + offset * 5, y + offset * 2,
                    x + offset * 4, y + offset * 2,
                    x + offset * 4, y + offset * 1,
                    x + offset * 5, y + offset * 1,
                    // RIGHT QUAD
                    x + offset * 5, y + offset * 2,
                    x + offset * 4, y + offset * 2,
                    x + offset * 4, y + offset * 1,
                    x + offset * 5, y + offset * 1};
              case 7: //Wood + Leaves
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 6, y + offset * 2,
                    x + offset * 5, y + offset * 2,
                    x + offset * 5, y + offset * 1,
                    x + offset * 6, y + offset * 1,
                    // TOP
                    x + offset * 6, y + offset * 2,
                    x + offset * 5, y + offset * 2,
                    x + offset * 5, y + offset * 1,
                    x + offset * 6, y + offset * 1,
                    // FRONT QUAD
                    x + offset * 6, y + offset * 4,
                    x + offset * 5, y + offset * 4,
                    x + offset * 5, y + offset * 3,
                    x + offset * 6, y + offset * 3,
                    // BACK QUAD
                    x + offset * 6, y + offset * 4,
                    x + offset * 5, y + offset * 4,
                    x + offset * 5, y + offset * 3,
                    x + offset * 6, y + offset * 3,
                    // LEFT QUAD
                    x + offset * 6, y + offset * 4,
                    x + offset * 5, y + offset * 4,
                    x + offset * 5, y + offset * 3,
                    x + offset * 6, y + offset * 3,
                    // RIGHT QUAD
                    x + offset * 6, y + offset * 4,
                    x + offset * 5, y + offset * 4,
                    x + offset * 5, y + offset * 3,
                    x + offset * 6, y + offset * 3};
            case 8: //Leaves
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 5, y + offset * 4,
                    x + offset * 4, y + offset * 4,
                    x + offset * 4, y + offset * 3,
                    x + offset * 5, y + offset * 3,
                    // TOP
                    x + offset * 5, y + offset * 4,
                    x + offset * 4, y + offset * 4,
                    x + offset * 4, y + offset * 3,
                    x + offset * 5, y + offset * 3,
                    // FRONT QUAD
                    x + offset * 5, y + offset * 4,
                    x + offset * 4, y + offset * 4,
                    x + offset * 4, y + offset * 3,
                    x + offset * 5, y + offset * 3,
                    // BACK QUAD
                    x + offset * 5, y + offset * 4,
                    x + offset * 4, y + offset * 4,
                    x + offset * 4, y + offset * 3,
                    x + offset * 5, y + offset * 3,
                    // LEFT QUAD
                    x + offset * 5, y + offset * 4,
                    x + offset * 4, y + offset * 4,
                    x + offset * 4, y + offset * 3,
                    x + offset * 5, y + offset * 3,
                    // RIGHT QUAD
                    x + offset * 5, y + offset * 4,
                    x + offset * 4, y + offset * 4,
                    x + offset * 4, y + offset * 3,
                    x + offset * 5, y + offset * 3};
        }
        return null;
    }
}
