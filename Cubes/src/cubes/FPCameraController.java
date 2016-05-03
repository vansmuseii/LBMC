/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubes;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;

/**
 *
 * @author francis
 */
public class FPCameraController {
private Vector3f position = null;
private Vector3f lPosition = null;
//the rotation around the Y axis of the camera
private float yaw = 0.0f;
//the rotation around the X axis of the camera
private float pitch = 0.0f;
private Vector3Float me;

public FPCameraController(float x, float y, float z)
{
//instantiate position Vector3f to the x y z params.
position = new Vector3f(x, y, z);
lPosition = new Vector3f(x,y,z);
lPosition.x = 0f;
lPosition.y = 15f;
lPosition.z = 0f;
}

public void yaw(float amount)
{
//increment the yaw by the amount param
yaw += amount;
}
//increment the camera's current yaw rotation
public void pitch(float amount)
{
//increment the pitch by the amount param
pitch -= amount;
}

public void walkForward(float distance)
{
float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
position.x -= xOffset;
position.z += zOffset;
}

public void walkBackwards(float distance)
{
float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
position.x += xOffset;
position.z -= zOffset;
}

public void strafeLeft(float distance)
{
float xOffset = distance * (float)Math.sin(Math.toRadians(yaw-90));
float zOffset = distance * (float)Math.cos(Math.toRadians(yaw-90));
position.x -= xOffset;
position.z += zOffset;
}

public void strafeRight(float distance)
{
float xOffset = distance * (float)Math.sin(Math.toRadians(yaw+90));
float zOffset = distance * (float)Math.cos(Math.toRadians(yaw+90));
position.x -= xOffset;
position.z += zOffset;
}

public void moveUp(float distance)
{
position.y -= distance;
}
//moves the camera down
public void moveDown(float distance)
{
position.y += distance;
}

public void lookThrough()
{
//roatate the pitch around the X axis
glRotatef(pitch, 1.0f, 0.0f, 0.0f);
//roatate the yaw around the Y axis
glRotatef(yaw, 0.0f, 1.0f, 0.0f);
//translate to the position vector's location
glTranslatef(position.x, position.y, position.z);
}

public void gameLoop()
{
FPCameraController camera = new FPCameraController(0, 0, 0);
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
while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
{
time = Sys.getTime();
lastTime = time;
//distance in mouse movement
//from the last getDX() call.
dx = Mouse.getDX();
//distance in mouse movement
//from the last getDY() call.
dy = Mouse.getDY();
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
if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
camera.moveDown(movementSpeed);
}

   //set the modelview matrix back to the identity
glLoadIdentity();
//look through the camera before you draw anything
camera.lookThrough();
glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//you would draw your scene here.
render();
//draw the buffer to the screen
Display.update();
Display.sync(60);
}
Display.destroy();
}

private void render() {
try{
  glBegin(GL_QUADS);
       for(int i = 0; i<6; i++)
        side(2.f,i);
        glEnd();
        
        glBegin(GL_LINE_LOOP);
//Top
glColor3f(0.0f,0.0f,0.0f);
glVertex3f( 1.0f, 1.0f,-1.0f);
glVertex3f(-1.0f, 1.0f,-1.0f);
glVertex3f(-1.0f, 1.0f, 1.0f);
glVertex3f( 1.0f, 1.0f, 1.0f);
glEnd();
glBegin(GL_LINE_LOOP);
//Bottom
glVertex3f( 1.0f,-1.0f, 1.0f);
glVertex3f(-1.0f,-1.0f, 1.0f);
glVertex3f(-1.0f,-1.0f,-1.0f);
glVertex3f( 1.0f,-1.0f,-1.0f);
glEnd();
glBegin(GL_LINE_LOOP);
//Front
glVertex3f( 1.0f, 1.0f, 1.0f);
glVertex3f(-1.0f, 1.0f, 1.0f);
glVertex3f(-1.0f,-1.0f, 1.0f);
glVertex3f( 1.0f,-1.0f, 1.0f);
glEnd();
glBegin(GL_LINE_LOOP);
//Back
glVertex3f( 1.0f,-1.0f,-1.0f);
glVertex3f(-1.0f,-1.0f,-1.0f);
glVertex3f(-1.0f, 1.0f,-1.0f);
glVertex3f( 1.0f, 1.0f,-1.0f);
glEnd();
glBegin(GL_LINE_LOOP);
//Left
glVertex3f(-1.0f, 1.0f, 1.0f);
glVertex3f(-1.0f, 1.0f,-1.0f);
glVertex3f(-1.0f,-1.0f,-1.0f);
glVertex3f(-1.0f,-1.0f, 1.0f);
glEnd();
glBegin(GL_LINE_LOOP);
//Right
glVertex3f( 1.0f, 1.0f,-1.0f);
glVertex3f( 1.0f, 1.0f, 1.0f);
glVertex3f( 1.0f,-1.0f, 1.0f);
glVertex3f( 1.0f,-1.0f,-1.0f);
glEnd();

}catch(Exception e){
}
}
 public void side(float lenght, int side) {
   switch(side){
       //top
        case 0:
    glColor3f(1.0f,1.0f,1.0f);
        
    
    glVertex3f( lenght/2,lenght/2, -lenght/2);
    glVertex3f(-lenght/2,lenght/2, -lenght/2);
    glVertex3f(-lenght/2,lenght/2, lenght/2);
    glVertex3f( lenght/2,lenght/2, lenght/2); 
   
    
    
    
    break;
    //bottom
     case 1:
    glColor3f(0.0f,0.0f,1.0f);
  
    
    glVertex3f( lenght/2,-lenght/2, lenght/2);
    glVertex3f(-lenght/2,-lenght/2, lenght/2);
    glVertex3f(-lenght/2,-lenght/2, -lenght/2);
    glVertex3f( lenght/2,-lenght/2, -lenght/2);

    
   
    break;
    //front
    case 2:
    glColor3f(0.0f,1.0f,0.0f);
        
    
 glVertex3f( lenght/2,lenght/2, lenght/2);
glVertex3f(-lenght/2,lenght/2, lenght/2);
glVertex3f(-lenght/2,-lenght/2, lenght/2);
glVertex3f( lenght/2,-lenght/2, lenght/2);

    break;
     // back   
        case 3:
   glColor3f(0.0f,1.0f,1.0f);
   
    
    glVertex3f(lenght/2,-lenght/2, -lenght/2);
glVertex3f(-lenght/2,-lenght/2, -lenght/2);
glVertex3f(-lenght/2,lenght/2, -lenght/2);
glVertex3f( lenght/2,lenght/2, -lenght/2);
    
    
    break;
    
    //left
        case 4:
    glColor3f(1.0f,0.0f,0.0f);
       
    
   glVertex3f(-lenght/2,lenght/2, lenght/2);
glVertex3f(-lenght/2,lenght/2, -lenght/2);
glVertex3f(-lenght/2,-lenght/2, -lenght/2);
glVertex3f(-lenght/2,-lenght/2, lenght/2);

    
 
    break;
    //rigth
     case 5:
    glColor3f(1.0f,0.0f,1.0f);
   
    glVertex3f( lenght/2,lenght/2, -lenght/2);
    glVertex3f( lenght/2,lenght/2, lenght/2);
    glVertex3f( lenght/2,-lenght/2, lenght/2);
    glVertex3f( lenght/2,-lenght/2, -lenght/2);
    
    
    break;
    
    
    
    
    }
 }

}

