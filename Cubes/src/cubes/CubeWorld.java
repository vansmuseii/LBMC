/**
 * *********************************************************************
 *      file: CubeWorld.java
 *      author: Frank Lapena, Chun Ho, Van Muse
 *      class: CS 445 - Computer Graphics
 *
 *      assignment: Program 2
 *      date last modified: 05/04/2016
 *
 *      purpose: This program is to demonstrate how to use the LWGJL
 *              to move a cube.
 *
 **********************************************************************
 */
package cubes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

public class CubeWorld implements Cubes {

    float roty = 0.0f;
    float rotx = 0.0f;
    float rotz = 0.0f;

    @Override
    public void start() {
        try {
            createWindow();
            initGL();
            render();

        } catch (Exception e) {
        }
    }

    private void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-320, 320, -240, 240, -320, 320);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }

    private void createWindow() {
        try {
            Display.setFullscreen(false);
            Display.setDisplayMode(new DisplayMode(640, 480));
            Display.setTitle("Shapes from a File");
            Display.create();
        } catch (Exception e) {

        }
    }

    private void render() {

        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {

            try {

                draw();

            } catch (Exception e) {

            }

            Display.update();
            Display.sync(60);
        }

        Display.destroy();
    }

    @Override
    public void draw() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glLoadIdentity();
        glPointSize(1);

        cameraMotion();

        // loop 6 times and transform each side
        glBegin(GL_QUADS);
        side(60.f, 0);
        side(60.0f, 1);
        side(60.0f, 2);
        side(60.0f, 3);
        side(60.0f, 4);
        side(60.0f, 5);
        glEnd();
    }

    // the side needs to take an arg
    @Override
    public void side(float lenght, int side) {
        switch (side) {
            // 3   
            case 3:
                glColor3f(0.0f, 1.0f, 1.0f);

                glVertex3f(-lenght / 2, lenght / 2, 0); //top left
                glVertex3f(-lenght / 2, -lenght / 2, 0.0f); //bottom left
                glVertex3f(lenght / 2, -lenght / 2, 0.0f);	//bottom right
                glVertex3f(lenght / 2, lenght / 2, 0.0f);	//top right

                break;

            //4
            case 4:
                glColor3f(1.0f, 0.0f, 0.0f);

                glVertex3f(-lenght / 2, lenght / 2, 0); //top left
                glVertex3f(-lenght / 2, -lenght / 2, 0.0f); //bottom left
                glVertex3f(-lenght / 2, -lenght / 2, lenght);	//bottom right
                glVertex3f(-lenght / 2, lenght / 2, lenght);	//top right

                break;
            //0
            case 0:
                glColor3f(1.0f, 1.0f, 1.0f);

                glVertex3f(-lenght / 2, lenght / 2, 0); //top left
                glVertex3f(lenght / 2, lenght / 2, 0);	//top right
                glVertex3f(lenght / 2, lenght / 2, lenght);	//bottom right
                glVertex3f(-lenght / 2, lenght / 2, lenght); //bottom left

                break;
            case 5:
                glColor3f(1.0f, 0.0f, 1.0f);

                glVertex3f(lenght / 2, lenght / 2, 0); //top left
                glVertex3f(lenght / 2, lenght / 2, lenght); //bottom left
                glVertex3f(lenght / 2, -lenght / 2, lenght);	//bottom right
                glVertex3f(lenght / 2, -lenght / 2, 0);	//top right

                break;
            //2
            case 2:
                glColor3f(0.0f, 1.0f, 0.0f);

                glVertex3f(-lenght / 2, lenght / 2, lenght); //top left
                glVertex3f(-lenght / 2, -lenght / 2, lenght); //bottom left
                glVertex3f(lenght / 2, -lenght / 2, lenght);	//bottom right
                glVertex3f(lenght / 2, lenght / 2, lenght);	//top right

                break;

            //1
            case 1:
                glColor3f(0.0f, 0.0f, 1.0f);

                glVertex3f(-lenght / 2, -lenght / 2, 0); //top left
                glVertex3f(-lenght / 2, -lenght / 2, lenght); //bottom left
                glVertex3f(lenght / 2, -lenght / 2, lenght);	//bottom right
                glVertex3f(lenght / 2, -lenght / 2, 0);	//top right

                break;

        }
    }

    private void cameraMotion() {
        Mouse.setGrabbed(false);

        if (Mouse.isButtonDown(0)) {
            roty += 1.0f;

        }
        if (Mouse.isButtonDown(1)) {

            rotz += 1.0f;
        }

//     if(Keyboard.isKeyDown(Keyboard.KEY_S))
//                 {
//                     rotz += 1.0f;
//                
//                 }
//        if(Keyboard.isKeyDown(Keyboard.KEY_W))
//                 {
//                     roty -= 1.0f;
//                 
//                 }
//         if(Keyboard.isKeyDown(Keyboard.KEY_A))
//                 {
//                     rotz += 1.0f;
//                
//                 }
//        if(Keyboard.isKeyDown(Keyboard.KEY_D))
//                 {
//                     rotz -= 1.0f;
//                 
//                 }
//         
        glRotatef(roty, 1.0f, 0.0f, 0.0f);
        glRotatef(rotz, 0.0f, 1.0f, 0.0f);
    }

    public static void main(String[] args) {
        CubeWorld c = new CubeWorld();
        c.start();
    }

}
