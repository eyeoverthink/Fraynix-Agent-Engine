package renderer;

import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.AssetPool;
import util.JMath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;


//todo: //==================TEST IN WINDOW CLASS ========================

//todo: //==================== : LINE 2D DEBUG : ===========================

// todo : If you want to test, call it in the window wherre it handles changing scenes

//todo: //===============================================================




public class DebugDraw {
    //=================================================================
    // todo : this method will help us figure out line intersection.
    // todo : This is the core class -- Line2D works hand in hand


//todo: //=================================================================
    // todo : Debug Gravity, Intersection and collision

//todo: //=================================================================

    private static int MAX_LINES = 500;

    private static List<Line2D> lines = new ArrayList<>();
    // todo : we will have 6 floats per vertex
    public static float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static Shader shader = AssetPool.getShader("assets/shaders/debugLine2D.glsl");

    private static int vaoID;
    private static int vboID;
    // todo : our gpu start indicator

    private static boolean started = false;

    public static void start(){
        // todo : generate the vao
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // todo : create the bvo and buffer memory
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // todo : enable the vertex attribute pointer   - r - g - b only - no alpha
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // todo : enable the second pointer to the vertex attributes - r - g - b only - no alpha
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // todo : set the line width here
        glLineWidth(3.0f);
    }

    public static void beginFrame(){
        // todo : make sure it's started, if not start it :
        if (!started){
            start();
            started = true;
        }

        // todo : clear all the existing lines
        for (int i = 0; i < lines.size(); i++){
            if (lines.get(i).beginFrame() < 0){
                // todo : this implies that the lifetime has expired :
                lines.remove(i);
                // todo : decrement i by one to shift for the line removed : [ ,  ,  ,   <-- ,  ]
                i--;
            }
        }
    }

    public static void draw() {
        //=================================================================
        // todo : this method is the same as our other graphic prep-classes
        // todo : we need to create and bind buffers -
        // todo : and attach shaders

//todo: //===============================================================

//todo: //==================== : START INIT : ===========================


//todo: //===============================================================


        if (lines.size() <= 0)
            return;
        int index = 0;
        // todo this loop will produce a from the first position , the first time around , then the to location
        //  in java this loop :--> "example" ' val = i == 0 ? resultOne : resultTwo : means the below 'end' -->
        // todo : this loop says " If i is zero , get the LINE and FROM position : other wise, get the LINE and TO ... "
        for (Line2D line : lines) {
            for (int i = 0; i < 2; i++) {
                Vector2f position = i == 0 ? line.getEnd() : line.getStart();
                Vector3f color = line.getColor();

//todo:              // Load position

                vertexArray[index] = position.x;
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = -10.0f;


//todo:               // Load color
                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;
                index += 6;
            }
        }

        // todo : bind vbo
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2));

        // todo : Use our Shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());

        // todo :  Bind the vao
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // todo :  Draw the batch
        glDrawArrays(GL_LINES, 0, lines.size() * 6 * 2);

        // todo :  Disable the appointed location for both pointers bind it to nothing
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        // todo : bind it to nothing
        glBindVertexArray(0);
        //todo: detach the shader / unbind :
        shader.detach();
    }

//todo: //=================================================================
        // todo : END INIT :


//todo: //=================================================================
//todo        LINE  : DRAW LINE 2D Method

//todo: //=================================================================

    public static void addLine2D(Vector2f from, Vector2f to){
        addLine2D(from, to, new Vector3f(0, 1, 0), 1);
    }

    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color){
        addLine2D(from, to, color, 1);
    }


// todo : a constants class holding screen, sprite, lifetime, color etc..
//  -- or handler would fit - we can reference variables and have
//todo : access to change them easier.. implement further - 05-6-22


    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifeTime){
        if (lines.size() >= MAX_LINES) return;
        DebugDraw.lines.add(new Line2D(from, to, color, lifeTime));
    }


    //todo: //=================================================================
    // todo 1 ::: Circle -- DRAW 2D CIRCLE -- Method

    //todo: //=================================================================
    public static void addCircle(Vector2f center, float radius) {
        // TODO: ADD CONSTANTS FOR COMMON COLORS
        addCircle(center, radius, new Vector3f(0, 1, 0), 1);
    }

    public static void addCircle(Vector2f center, float radius, Vector3f color) {
        addCircle(center, radius, color, 1);
    }

    public static void addCircle(Vector2f center, float radius, Vector3f color, int lifetime) {
        Vector2f[] points = new Vector2f[20];
        int increment = 360 / points.length;
        int currentAngle = 0;

        for (int i=0; i < points.length; i++) {
            Vector2f tmp = new Vector2f(0, radius);
            JMath.rotate(tmp, currentAngle, new Vector2f());
            points[i] = new Vector2f(tmp).add(center);

            if (i > 0) {
                addLine2D(points[i - 1], points[i], color, lifetime);
            }
            currentAngle += increment;
        }

        addLine2D(points[points.length - 1], points[0], color, lifetime);
    }


    //todo: //=================================================================
    // todo 1   ::  DRAW :  BOX 2D  -   Method

//todo: //=====================================================================

    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation) {
        // TODO: ADD CONSTANTS FOR COMMON COLORS
        addBox2D(center, dimensions, rotation, new Vector3f(0, 1, 0), 1);
    }

    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation, Vector3f color) {
        addBox2D(center, dimensions, rotation, color, 1);
    }

    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation,
                                Vector3f color, int lifetime) {
        Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).mul(0.5f));
        Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).mul(0.5f));

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
                new Vector2f(max.x, max.y), new Vector2f(max.x, min.y)
        };

        if (rotation != 0.0f) {
            for (Vector2f vert : vertices) {
                JMath.rotate(vert, rotation, center);
            }
        }

        addLine2D(vertices[0], vertices[1], color, lifetime);
        addLine2D(vertices[0], vertices[3], color, lifetime);
        addLine2D(vertices[1], vertices[2], color, lifetime);
        addLine2D(vertices[2], vertices[3], color, lifetime);
    }


    //todo: //=================================================================
    // todo 1 ::: Collider 2D Method

//todo: //=================================================================

    public void createCircle(float x, float y){
        Vector2f from = new Vector2f();
        Vector2f to = new Vector2f();
        float radius = 32.0f;
        from.x = 325;
        from.y = 325;

        addCircle(new Vector2f(x, y), radius);
    }

    public void createSquare(float from_x, float from_y, int width, int height){
        addBox2D(new Vector2f(from_x, from_y), new Vector2f(width, height),
                30,new Vector3f(0, 1, 0), 1 );
    }

    public void createLine(float from_x, float from_y, float to_x, float to_y){
        Vector2f from = new Vector2f();
        Vector2f to = new Vector2f();
        from.x = from_x;
        from.y = from_y;
        to.x = to_x;
        to.y = to_y;

        addLine2D(from, to, new Vector3f(0, 1, 0), 1);
    }




}



