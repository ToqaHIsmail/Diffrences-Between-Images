
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.media.opengl.*;

import java.util.BitSet;
import javax.media.opengl.glu.GLU;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class AnimGLEventListener extends AnimListener implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {

    int maxWidth = 100;
    int maxHeight = 100;
    int xPosition = 0;
    int yPosition = 0;
    int score = 0;
    int timer = 500;
    int state = 0;
    int a = 0;
    boolean isPause = false;
    boolean isMenu = false;
    boolean isLevel = false;
    boolean isGameOver = false;
    boolean isWin = false;
    boolean isLevel1 = false;
    boolean isLevel2 = false;
    boolean isLevel3 = false;
    boolean isHelp = false;
    boolean isExit = false;
    ArrayList arr1 = new ArrayList();
    ArrayList arr2 = new ArrayList();
    ArrayList arr3 = new ArrayList();

    GLCanvas glc;
    GL gl;

    TextRenderer tr = new TextRenderer(Font.decode("PLAIN"));
    String textureNames[] = {"StartMenu.jpg", "Play.png", "Help.png", "Exit.png",
        "LevelsMenu.png", "L1.png", "L2.png", "L3.png",
        "back.png", "FrozenHelp.jpg", "level1.jpg", "level2.jpg",
        "level3.jpg", "circle.png", "win.jpg", "gameover.jpg", "puase.jpg",
         "puaseback.png", "backb.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];
    int playframe = 0;

    public void setGLCanvas(GLCanvas glc) {
        this.glc = glc;
    }

    public void init(GLAutoDrawable gld) {

        GLU glu = new GLU();

        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black

        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);
        gl.glViewport(0, 0, 500, 500);
        glu.gluOrtho2D(-50.0, 50, -50, 50);
        gl.glLoadIdentity();

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
        
        try {
            FileInputStream music = new FileInputStream(new File("OpeningWindow.wav"));
            AudioStream audios = new AudioStream(music);
            AudioPlayer.player.start(audios);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
    }

    public void display(GLAutoDrawable gld) {

        gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer

        switch (state) {
            case 0:
                StartMenu(gl);
                isMenu = true;
                break;
            case 2:
                LevelsMenu();
                break;
            case 3:
                Level1();
                timer--;
                break;
            case 4:
                Level2();
                timer--;
                break;
            case 5:
                Level3();
                timer--;
                break;
            case 6:
                EndGame();
                break;
            case 7:
                DrawHelp();
                break;
            case 8:
                pause();
                score = score;
                break;
            case 9:
                GameOver();
                break;
            case 10:
                YouWin();
                break;
        }
    }

    public void StartMenu(GL gl) {
        DrawBackground(gl, 0);

        DrawSprite(gl, 45, 60, 1, (float) 1);
        DrawSprite(gl, 45, 40, 2, (float) 1);
        DrawSprite(gl, 45, 20, 3, (float) 1);

    }

    public void LevelsMenu() {
        DrawBackground(gl, 4);

        DrawSprite(gl, 45, 80, 5, (float) 1);
        DrawSprite(gl, 45, 60, 6, (float) 1);
        DrawSprite(gl, 45, 40, 7, (float) 1);
        DrawSprite(gl, 45, 20, 8, (float) 1);
        isLevel = true;
    }

    public void Level1() {
        isLevel1 = true;
        if (isGameOver) {
            score = 0;
        }
        DrawBackground(gl, 10);
        Drawpuase(gl, 45, 85, 16, 1);
        Drawpuase(gl, 90, 2, 18, 1);
        DrawScore();

        if (arr1.contains(1)) {
            DrawSprite(gl, 44, 85, 13, 1);
            DrawSprite(gl, 95, 85, 13, 1);
        }

        if (arr1.contains(2)) {
            DrawSprite(gl, 42, 68, 13, 1);
            DrawSprite(gl, 93, 68, 13, 1);
        }
        if (arr1.contains(3)) {
            DrawSprite(gl, 12, 13, 13, 1);
            DrawSprite(gl, 60, 20, 13, 1);
        }
        if (arr1.contains(4)) {
            DrawSprite(gl, 45, 77, 13, 1);
            DrawSprite(gl, 95, 77, 13, 1);
        }
        IsDead();
        IsWin();

        if (isKeyPressed(KeyEvent.VK_X)) {
            timer = timer;
            score = score;
            state = 8;
        }
    }

    public void Level2() {
        isLevel2 = true;
        if (isGameOver) {
            score = 0;
        }
        DrawBackground(gl, 11);
        Drawpuase(gl, 45, 85, 16, 1);
        Drawpuase(gl, 90, 2, 18, 1);
        DrawScore();

        if (arr2.contains(1)) {
               DrawSprite(gl, 30, 43, 13, 1);
               DrawSprite(gl, 81, 43, 13,1);
        }

        if (arr2.contains(2)) {
                DrawSprite(gl, 7, 53, 13, 1);
                DrawSprite(gl, 58,53, 13, 1);
        }
        if (arr2.contains(3)) {
               DrawSprite(gl, 30, 47, 13, 1);
               DrawSprite(gl, 81, 47, 13, 1);
        }
        if (arr2.contains(4)) {
                DrawSprite(gl, 44, 12, 13, 1);
                DrawSprite(gl, 94, 12, 13, 1);
        }
        if (arr2.contains(5)) {
               DrawSprite(gl, 30, 55, 13, 1);
               DrawSprite(gl, 81, 55, 13,1);
        }

        if (arr2.contains(6)) {
                DrawSprite(gl, 22, 28, 13, 1);
                DrawSprite(gl, 73, 28, 13, 1);
        }
        if (arr2.contains(7)) {
                DrawSprite(gl, 45, 45, 13, 1);
                DrawSprite(gl, 96, 45, 13, 1);
        }
        if (arr2.contains(8)) {
                DrawSprite(gl, 9, 0, 13, 1);
                DrawSprite(gl, 62,0, 13, 1);
        }
        if (arr2.contains(9)) {
               DrawSprite(gl, 33, 75, 13, 1);
               DrawSprite(gl, 84, 75, 13,1);
        }

        IsDead();
        IsWin();
        if (isKeyPressed(KeyEvent.VK_X)) {
            timer = timer;
            score = score;
            state = 8;
        }
    }

    public void Level3() {
        isLevel3 = true;
        if (isGameOver) {
            score = 0;
        }
        DrawBackground(gl, 12);
        Drawpuase(gl, 45, 85, 16, 1);
        Drawpuase(gl, 90, 2, 18, 1);
        DrawScore();

        if (arr3.contains(1)) {
               DrawSprite(gl, 54, 0, 13, 1);
               DrawSprite(gl, 54, 51, 13,1);
        }

        if (arr3.contains(2)) {
               DrawSprite(gl, 18, 22, 13, 1);
               DrawSprite(gl, 18, 74, 13,1);
        }
        if (arr3.contains(3)) {
                DrawSprite(gl, 20, 1, 13, 1);
                DrawSprite(gl, 20, 53, 13, 1);
        }
        if (arr3.contains(4)) {
                DrawSprite(gl, 22, 23, 13, 1);
                DrawSprite(gl, 22, 75, 13, 1);
        }
        if (arr3.contains(5)) {
               DrawSprite(gl, 76, 18, 13, 1);
               DrawSprite(gl, 79, 69, 13,1);
        }

        if (arr3.contains(6)) {
                DrawSprite(gl, 68, 34, 13, 1);
                DrawSprite(gl, 68, 87, 13, 1);
        }
        if (arr3.contains(7)) {
               DrawSprite(gl, 44, 4, 13, 1);
               DrawSprite(gl, 48, 55, 13,1);
        }
        if (arr3.contains(8)) {
               DrawSprite(gl, 98, 24, 13, 1);
               DrawSprite(gl, 98, 76, 13,1);
        }
        if (arr3.contains(9)) {
               DrawSprite(gl, 88, 15, 13, 1);
               DrawSprite(gl, 88, 67, 13,1);
        }
        if (arr3.contains(10)) {
               DrawSprite(gl, 25, 0, 13, 1);
               DrawSprite(gl, 25, 51, 13,1);
        }

        IsDead();
        IsWin();
        if (isKeyPressed(KeyEvent.VK_X)) {
            timer = timer;
            score = score;
            state = 8;
        }
    }

    public void DrawScore() {
        tr.beginRendering(300, 300);
        tr.setColor(Color.MAGENTA);
        tr.draw("Score : " + score, 8, 280);
        tr.draw("Timer : " + timer, 8, 260);
        tr.setColor(Color.WHITE);
        tr.endRendering();
    }

    public void pause() {
        DrawBackground(gl, 17);
        tr.beginRendering(300, 300);
        tr.setColor(Color.PINK);
        tr.draw("Click ' R ' To Resume", 95, 60);
        tr.setColor(Color.white);
        tr.endRendering();
        isPause = true;
        if (isKeyPressed(KeyEvent.VK_R) && isLevel2) {
            timer = timer;
            score = score;
            state = 4;
        }
        if (isKeyPressed(KeyEvent.VK_R) && isLevel1) {
            timer = timer;
            score = score;
            state = 3;
        }
        if (isKeyPressed(KeyEvent.VK_R) && isLevel3) {
            timer = timer;
            score = score;
            state = 5;
        }

    }

    public void GameOver() {
        DrawBackground(gl, 15);
        tr.beginRendering(300, 300);
        tr.setColor(Color.MAGENTA);
        tr.draw("Your score : " + score, 80, 200);
        tr.setColor(Color.WHITE);
        tr.endRendering();

        Drawpuase(gl, 5, 5, 18, 1);
        // Drawpuase(gl, 85, 5, 19, 1);
        if (isKeyPressed(KeyEvent.VK_BACK_SPACE)) {
            timer = 700;
            score = 0;
            state = 0;
        }
        isGameOver = true;
    }

    public void YouWin() {
        DrawBackground(gl, 14);
        Drawpuase(gl, 5, 5, 18, 1);
        tr.beginRendering(300, 300);
        tr.setColor(Color.BLACK);
        tr.draw("Your score : " + score, 120, 120);
        tr.setColor(Color.WHITE);
        tr.endRendering();
        isWin=true;
    }

    public void EndGame() {
        System.exit(0);
        isExit = true;
    }

    public void DrawHelp() {
        DrawBackground(gl, 9);
        Drawpuase(gl, 5, 5, 18, 1);
        isHelp = true;
    }

    public void IsWin() {
        if (state == 3) {
            if (score == 400) {
                state = 10;
                score = score;
            }
        } else if (state == 4) {
            if (score == 900) {
                state = 10;
                score = score;
            }
        } else if (state == 5) {
            if (score == 1000) {
                state = 10;
                score = score;
            }
        }

    }

    public void IsDead() {
        if (timer <= 0) {
            state = 9;
        }
    }

    //int click = 0;
    public void createcircle1(int k) {
        a = k;
        state = 3;
        score += 100;
    }

    public void createcircle2(int k) {
        a = k;
        state = 4;
        score += 100;
    }

    public void createcircle3(int k) {
        a = k;
        state = 5;
        score += 100;
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void DrawSprite(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.5 * scale, 0.2 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackground(GL gl, int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void Drawpuase(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void handleKeyPress() {

    }

    public BitSet keyBits = new BitSet(256);

    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
    }

    @Override
    public void keyTyped(KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        Component c = e.getComponent();
        double width = c.getWidth();
        double height = c.getHeight();
        xPosition = (int) ((x / width) * 100);
        yPosition = ((int) ((y / height) * 100));

        yPosition = 100 - yPosition;
        System.out.println(xPosition + " " + yPosition);

        if (isMenu) {
            if (xPosition < 74 && xPosition > 27 && yPosition < 73 && yPosition > 58) {                state = 2;
                state = 2;
                isMenu = false;
            }
            if (xPosition < 74 && xPosition > 27 && yPosition < 52 && yPosition > 38) {
                state = 7;
            }
            if (xPosition < 74 && xPosition > 27 && yPosition < 32 && yPosition > 17) {
                state = 6;   //exit state????
            }
        }
        if (isLevel) {
            isLevel = false;
            if (xPosition < 74 && xPosition > 27 && yPosition < 92 && yPosition > 78) {
          
                state = 3;
            }
            if (xPosition < 74 && xPosition > 27 && yPosition < 72 && yPosition > 58) {
                
                state = 4;  /// to click on level 2
                
            }
            if (xPosition < 74 && xPosition > 27 && yPosition < 52 && yPosition > 38) {
                
                state = 5; ////to click  on Level 3
            }
            if (xPosition < 74 && xPosition > 27 && yPosition < 33 && yPosition > 17) {

                state = 0; ////to click  on back
            }
        }
        if (isLevel1) {
            if (xPosition > 46 && xPosition < 53 && yPosition > 87 && yPosition < 94) {

                state = 8; ///puase background
            } else if (((xPosition > 38 && xPosition < 40) || (xPosition > 88 && xPosition < 91))
                    && (yPosition > 89 && yPosition < 98)) {

                if (arr1.isEmpty()) {
                    arr1.add(1);
                    createcircle1(1);
                } else {
                    for (int z = 0; z < arr1.size(); z++) {
                        if (!arr1.contains(1)) {
                            arr1.add(1);
                            createcircle1(1);
                        }
                    }
                }

            } else if (((xPosition > 36 && xPosition < 38) || (xPosition > 86 && xPosition < 89))
                    && (yPosition > 63 && yPosition < 80)) {

                if (arr1.isEmpty()) {
                    arr1.add(2);
                    createcircle1(2);
                } else {
                    for (int z = 0; z < arr1.size(); z++) {
                        if (!arr1.contains(2)) {
                            arr1.add(2);
                            createcircle1(2);
                        }
                    }
                }
            } else if (((xPosition > 0 && xPosition < 9) || (xPosition > 54 && xPosition < 61))
                    && ((yPosition > 12 && yPosition < 25) || (yPosition > 22 && yPosition < 37))) {
                if (arr1.isEmpty()) {
                    arr1.add(3);
                    createcircle1(3);
                } else {
                    for (int z = 0; z < arr1.size(); z++) {
                        if (!arr1.contains(3)) {
                            arr1.add(3);
                            createcircle1(3);
                        }
                    }
                }
            } else if (((xPosition > 34 && xPosition < 40) || (xPosition > 85 && xPosition < 91))
                    && ((yPosition > 82 && yPosition < 91))) {
                if (arr1.isEmpty()) {
                    arr1.add(4);
                    createcircle1(4);
                } else {
                    for (int z = 0; z < arr1.size(); z++) {
                        if (!arr1.contains(4)) {
                            arr1.add(4);
                            createcircle1(4);
                        }
                    }
                }
            } else {
                timer -= 20;
            }
        }
        if (isLevel2) {
            if (xPosition > 46 && xPosition < 53 && yPosition > 87 && yPosition < 94) {

                state = 8; ///puase background
            } else if (((xPosition > 74 && xPosition < 78) || (xPosition > 23 && xPosition < 27))
                    && (yPosition < 53 && yPosition > 49)) {
                if (arr2.isEmpty()) {  //ice on animal
                    arr2.add(1);
                    createcircle2(1);
                } else {
                    for (int z = 0; z < arr2.size(); z++) {
                        if (!arr2.contains(1)) {
                            arr2.add(1);
                            createcircle2(1);
                        }
                    }
                }

            } else if (((xPosition > 51 && xPosition < 53) || (xPosition > 0 && xPosition < 3))
                    && (yPosition > 56 && yPosition < 64)) {
                if (arr2.isEmpty()) {  // star on elsa hand
                    arr2.add(2);
                    createcircle2(2);
                } else {
                    for (int z = 0; z < arr2.size(); z++) {
                        if (!arr2.contains(2)) {
                            arr2.add(2);
                            createcircle2(2);
                        }
                    }
                }

            } else if (((xPosition > 23 && xPosition < 27) || (xPosition > 73 && xPosition < 77))
                    && (yPosition > 53 && yPosition < 57)) {
                if (arr2.isEmpty()) { //the eye of the animal
                    arr2.add(3);
                    createcircle2(3);
                } else {
                    for (int z = 0; z < arr2.size(); z++) {
                        if (!arr2.contains(3)) {
                            arr2.add(3);
                            createcircle2(3);
                        }
                    }
                }

            } else if (((xPosition > 87 && xPosition < 91) || (xPosition > 37 && xPosition < 40))
                    && (yPosition > 17 && yPosition < 22)) {
                if (arr2.isEmpty()) {   //flower on the dress on=f right girl
                    arr2.add(4);
                    createcircle2(4);
                } else {
                    for (int z = 0; z < arr2.size(); z++) {
                        if (!arr2.contains(4)) {
                            arr2.add(4);
                            createcircle2(4);
                        }
                    }
                }

            } else if (((xPosition > 74 && xPosition < 77) || (xPosition > 24 && xPosition < 26))
                    && (yPosition > 62 && yPosition < 64)) {
                if (arr2.isEmpty()) { //button on iceman
                    arr2.add(5);
                    createcircle2(5);
                } else {
                    for (int z = 0; z < arr2.size(); z++) {
                        if (!arr2.contains(5)) {
                            arr2.add(5);
                            createcircle2(5);
                        }
                    }
                }

            } else if (((xPosition > 16 && xPosition < 18) || (xPosition > 67 && xPosition < 69))
                    && (yPosition > 32 && yPosition < 42)) {
                if (arr2.isEmpty()) { //the red thing on the left man
                    arr2.add(6);
                    createcircle2(6);
                } else {
                    for (int z = 0; z < arr2.size(); z++) {
                        if (!arr2.contains(6)) {
                            arr2.add(6);
                            createcircle2(6);
                        }
                    }
                }

            } else if (((xPosition > 39 && xPosition < 42) || (xPosition > 89 && xPosition < 92))
                    && (yPosition > 47 && yPosition < 56)) {
                if (arr2.isEmpty()) { //points in the t-shirt of right man
                    arr2.add(7);
                    createcircle2(7);
                } else {
                    for (int z = 0; z < arr2.size(); z++) {
                        if (!arr2.contains(7)) {
                            arr2.add(7);
                            createcircle2(7);
                        }
                    }
                }
            } else if (((xPosition > 3 && xPosition < 5) || (xPosition > 54 && xPosition < 56))
                    && (yPosition > 7 && yPosition < 11)) {
                if (arr2.isEmpty()) {
                    arr2.add(8);
                    createcircle2(8);
                } else {
                    for (int z = 0; z < arr2.size(); z++) {
                        if (!arr2.contains(8)) {
                            arr2.add(8);
                            createcircle2(8);
                        }
                    }
                }

            } else if (((xPosition > 26 && xPosition < 30) || (xPosition > 76 && xPosition < 79))
                    && (yPosition > 82 && yPosition < 89)) {
                if (arr2.isEmpty()) { //hair of iceman
                    arr2.add(9);
                    createcircle2(9);
                } else {
                    for (int z = 0; z < arr2.size(); z++) {
                        if (!arr2.contains(9)) {
                            arr2.add(9);
                            createcircle2(9);
                        }
                    }
                }
            } else {
                timer -= 20;
            }
        }

        if (isLevel3) {
            if (xPosition > 46 && xPosition < 53 && yPosition > 87 && yPosition < 94) {

                state = 8; ///puase background
            } else if (((yPosition > 5 && yPosition < 8) || (yPosition > 58 && yPosition < 61))
                    && (xPosition > 47 && xPosition < 50)) {
                if (arr3.isEmpty()) {
                    arr3.add(1);
                    createcircle3(1);
                } else {
                    for (int z = 0; z < arr3.size(); z++) {
                        if (!arr3.contains(1)) {
                            arr3.add(1);
                            createcircle3(1);
                        }
                    }
                }
            } else if (((yPosition > 81 && yPosition < 85) || (yPosition > 28 && yPosition < 31))
                    && (xPosition > 11 && xPosition < 13)) {
                if (arr3.isEmpty()) {
                    arr3.add(2);
                    createcircle3(2);
                } else {
                    for (int z = 0; z < arr3.size(); z++) {
                        if (!arr3.contains(2)) {
                            arr3.add(2);
                            createcircle3(2);
                        }
                    }
                }
            } else if (((yPosition > 7 && yPosition < 12) || (yPosition > 59 && yPosition < 63))
                    && (xPosition > 12 && xPosition < 19)) {
                if (arr3.isEmpty()) {
                    arr3.add(3);
                    createcircle3(3);
                } else {
                    for (int z = 0; z < arr3.size(); z++) {
                        if (!arr3.contains(3)) {
                            arr3.add(3);
                            createcircle3(3);
                        }
                    }
                }
            } else if (((yPosition > 30 && yPosition < 33) || (yPosition > 83 && yPosition < 86))
                    && (xPosition > 15 && xPosition < 19)) {
                if (arr3.isEmpty()) {
                    arr3.add(4);
                    createcircle3(4);
                } else {
                    for (int z = 0; z < arr3.size(); z++) {
                        if (!arr3.contains(4)) {
                            arr3.add(4);
                            createcircle3(4);
                        }
                    }
                }
            } else if (((yPosition > 23 && yPosition < 33) || (yPosition > 76 && yPosition < 85))
                    && (xPosition > 64 && xPosition < 78)) {
                if (arr3.isEmpty()) {
                    arr3.add(5);
                    createcircle3(5);
                } else {
                    for (int z = 0; z < arr3.size(); z++) {
                        if (!arr3.contains(5)) {
                            arr3.add(5);
                            createcircle3(5);
                        }
                    }
                }
            } else if (((yPosition > 94 && yPosition < 96) || (yPosition > 41 && yPosition < 44))
                    && (xPosition > 62 && xPosition < 64)) {
                if (arr3.isEmpty()) {
                    arr3.add(6);
                    createcircle3(6);
                } else {
                    for (int z = 0; z < arr3.size(); z++) {
                        if (!arr3.contains(6)) {
                            arr3.add(1);
                            createcircle3(6);
                        }
                    }
                }
            } else if (((yPosition > 7 && yPosition < 16) || (yPosition > 60 && yPosition < 68))
                    && (xPosition > 37 && xPosition < 46)) {
                if (arr3.isEmpty()) {
                    arr3.add(7);
                    createcircle3(7);
                } else {
                    for (int z = 0; z < arr3.size(); z++) {
                        if (!arr3.contains(7)) {
                            arr3.add(7);
                            createcircle3(7);
                        }
                    }
                }
            } else if (((yPosition > 29 && yPosition < 37) || (yPosition > 82 && yPosition < 89))
                    && (xPosition > 91 && xPosition < 94)) {// point ice in animal side
                if (arr3.isEmpty()) {
                    arr3.add(8);
                    createcircle3(8);
                } else {
                    for (int z = 0; z < arr3.size(); z++) {
                        if (!arr3.contains(8)) {
                            arr3.add(8);
                            createcircle3(8);
                        }
                    }
                }
            } else if (((yPosition > 22 && yPosition < 24) || (yPosition > 74 && yPosition < 76))
                    && (xPosition > 80 && xPosition < 85)) {
                if (arr3.isEmpty()) {
                    arr3.add(9);
                    createcircle3(9);
                } else {
                    for (int z = 0; z < arr3.size(); z++) {
                        if (!arr3.contains(9)) {
                            arr3.add(9);
                            createcircle3(9);
                        }
                    }
                }
            } else if (((yPosition > 2 && yPosition < 8) || (yPosition > 55 && yPosition < 61))
                    && (xPosition > 19 && xPosition < 27)) {
                if (arr3.isEmpty()) {
                    arr3.add(10);
                    createcircle3(10);
                } else {
                    for (int z = 0; z < arr3.size(); z++) {
                        if (!arr3.contains(10)) {
                            arr3.add(10);
                            createcircle3(10);
                        }
                    }
                }
            } else {
                timer -= 20;
            }
        }
        if ((isHelp || !isLevel || isGameOver || isWin)
                && (xPosition < 13 && xPosition > 6) && (yPosition < 16 && yPosition > 5)) {
            state = 0;
            timer=700;
            score=0;
            arr1.clear();
            arr2.clear();
            arr3.clear();
            isLevel1=false;
            isLevel2=false;
            isLevel3=false;
        }
        if((isLevel1 || isLevel2 || isLevel3|| !isLevel)&&((xPosition < 97 && xPosition > 91) && (yPosition < 12 && yPosition > 2))){
            state = 2;
            timer=700;
            score=0;
            arr1.clear();
            arr2.clear();
            arr3.clear();
            isLevel1=false;
            isLevel2=false;
            isLevel3=false;
    }

    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mouseDragged(MouseEvent me) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}
