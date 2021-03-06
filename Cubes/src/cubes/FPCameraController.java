/**
 * *********************************************************************
 *      file: FPCameraController.java
 *      author: Frank Lapena, Chun Ho, Van Muse
 *      class: CS 445 - Computer Graphics
 *
 *      assignment: Final Project
 *      date last modified: 06/1/2016
 *
 *      purpose: This program is to demonstrate how to use the LWGJL
 *              to move a create a Minecraft game.
 *              Added turn on day and night (0) as well as turn off day
 *              and night (9).
 *
 **********************************************************************
 */
package cubes;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;

public class FPCameraController {

    private Vector3f position = null;
    private Vector3f lPosition = null;
    //the rotation around the Y axis of the camera
    private float yaw = 0.0f;
    //the rotation around the X axis of the camera
    private float pitch = 0.0f;
    private Vector3Float me;
    FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
    public FPCameraController(float x, float y, float z) {
        //instantiate position Vector3f to the x y z params.
        position = new Vector3f(x, y, z);
        lPosition = new Vector3f(0f,16f,16f);
    }

    public void yaw(float amount) {
        //increment the yaw by the amount param
        yaw += amount;
    }
    //increment the camera's current yaw rotation

    public void pitch(float amount) {
        //increment the pitch by the amount param
        pitch -= amount;
    }

    public void walkForward(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw));
        position.x -= xOffset;
        position.z += zOffset;

        lightPosition.put(lPosition.x -= xOffset).put(
                lPosition.y).put(lPosition.z += zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    public void walkBackwards(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw));
        position.x += xOffset;
        position.z -= zOffset;

        lightPosition.put(lPosition.x += xOffset).put(
                lPosition.y).put(lPosition.z -= zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    public void strafeLeft(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw - 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw - 90));
        position.x -= xOffset;
        position.z += zOffset;
        lightPosition.put(lPosition.x -= xOffset).put(
                lPosition.y).put(lPosition.z += zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    public void strafeRight(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw + 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw + 90));
        position.x -= xOffset;
        position.z += zOffset;
        lightPosition.put(lPosition.x -= xOffset).put(
                lPosition.y).put(lPosition.z += zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    public void moveUp(float distance) {
        position.y -= distance;
    }
    //moves the camera down

    public void moveDown(float distance) {
        position.y += distance;
    }

    public void DayNight(float x, float y){
        lPosition = new Vector3f(x, (float)(90 * Math.sin(Math.toRadians(y))), 150f );
    }
    
    
    public void lookThrough() {
        //roatate the pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //roatate the yaw around the Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        glTranslatef(position.x, position.y, position.z);
        lightPosition.put(lPosition.x).put(
                lPosition.y).put(lPosition.z).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);

    }

    public void gameLoop() {
        FPCameraController camera = new FPCameraController(0.0f, -50.0f, -50.0f);

        Chunk c = new Chunk(0, 0, 0);

        boolean cycle = false;
        float counter = 0;
        float xval = 0;
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f; //length of frame
        float lastTime = 0.0f; // when the last frame was
        long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;
        //hide the mouse
        Mouse.setGrabbed(true);

        // keep looping till the display window is closed the ESC key is down
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            time = Sys.getTime();
            lastTime = time;
            //distance in mouse movement
            //from the last getDX() call.
            dx = Mouse.getDX();
            //distance in mouse movement
            //from the last getDY() call.
            dy = Mouse.getDY();
            //control camera yaw from x movement fromt the mouse 
            camera.yaw(dx * mouseSensitivity);
            //controll camera pitch from y movement fromt the mouse 
            camera.pitch(dy * mouseSensitivity);
            //when passing in the distance to move
            //we times the movementSpeed with dt this is a time scale
            //so if its a slow frame u move more then a fast frame
            //so on a slow computer you move just as fast as on a fast computer
            if (Keyboard.isKeyDown(Keyboard.KEY_W))//move forward
            {
                camera.walkForward(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S))//move backwards
            {
                camera.walkBackwards(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A))//strafe left
            {
                camera.strafeLeft(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D))//strafe right
            {
                camera.strafeRight(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))//move up
            {
                camera.moveUp(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                camera.moveDown(movementSpeed);
            }
            // Press 0 to activate Day and Night Rotation
            if(Keyboard.isKeyDown(Keyboard.KEY_0)){
                cycle = true;
            }
            
            // This will implement the cycle by going around a circle
            if (cycle) {
                counter = (counter + 0.1f) % 360;
                if (xval >= 500) {
                    xval = -500;
                }
                xval++;
                camera.DayNight(xval, counter);
            }
            // Press 9 to turn off Day and Night
            if(Keyboard.isKeyDown(Keyboard.KEY_9)){
                cycle = false;
                camera.lPosition= new Vector3f(0f, 16f, 16f);
            }
            //set the modelview matrix back to the identity
            glLoadIdentity();
            //look through the camera before you draw anything
            camera.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            //you would draw your scene here.
            //render();
            c.render();
            //
            //draw the buffer to the screen
            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }

    //Render simple 3D cube w/ six different colors
    private void render() {
        try {
            glBegin(GL_QUADS);
            //Top
            glColor3f(0.0f, 0.0f, 1.0f); //Color
            glVertex3f(1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(1.0f, 1.0f, 1.0f);

            //Bottom
            glColor3f(0.0f, 1.0f, 0.0f); //Color
            glVertex3f(1.0f, -1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(1.0f, -1.0f, -1.0f);

            //Front
            glColor3f(1.0f, 0.0f, 1.0f); //Color
            glVertex3f(1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, 1.0f);

            //Back
            glColor3f(1.0f, 0.0f, 0.0f); //Color
            glVertex3f(1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(1.0f, 1.0f, -1.0f);

            //Left
            glColor3f(1.0f, 1.0f, 0.0f); //Color
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);

            //Right
            glColor3f(0.0f, 1.0f, 1.0f); //Color
            glVertex3f(1.0f, 1.0f, -1.0f);
            glVertex3f(1.0f, 1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, -1.0f);
            glEnd();

            glBegin(GL_LINE_LOOP);
            //Top
            glColor3f(0.0f, 0.0f, 0.0f); //Color
            glVertex3f(1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(1.0f, 1.0f, 1.0f);
            glEnd();

            glBegin(GL_LINE_LOOP);
            //Bottom
            glVertex3f(1.0f, -1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(1.0f, -1.0f, -1.0f);
            glEnd();

            glBegin(GL_LINE_LOOP);
            //Front
            glVertex3f(1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, 1.0f);
            glEnd();

            glBegin(GL_LINE_LOOP);
            //Back
            glVertex3f(1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(1.0f, 1.0f, -1.0f);
            glEnd();

            glBegin(GL_LINE_LOOP);
            //Left
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);
            glEnd();

            glBegin(GL_LINE_LOOP);
            //Right
            glVertex3f(1.0f, 1.0f, -1.0f);
            glVertex3f(1.0f, 1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, -1.0f);
            glEnd();

        } catch (Exception e) {

        }
    }
}
