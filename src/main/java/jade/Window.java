package jade;

import components.Jarvis;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import renderer.*;
import scenes.LevelEditorScene;
import scenes.LevelScene;
import scenes.Scene;
import util.AssetPool;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    //todo: //=================================================================//
    //todo: //=================================================================
    //todo: //============Test DeBug Line2d Here in the Main Loop : ===========//
    //todo: //=================================================================
    //todo: //=================================================================


    private int width, height;
    private String title;
    private long glfwWindow;
    private ImGuiLayer imguiLayer;
    private Framebuffer framebuffer;
    private PickingTexture pickingTexture;


    public float r, g, b, a;
    private boolean fadeToBlack = false;

    private static Window window = null;

    private static Scene currentScene;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Fraynix-Agent-Engine";


        r = 1;
        b = 1;
        g = 1;
        a = 1;
    }



    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false : " ERROR!!  Unknown scene '" + newScene + "'";
                break;
        }
        currentScene.load();
        currentScene.init();
        currentScene.start();
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public static Scene getScene() {
        return get().currentScene;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);


        // todo : create our frame buffer for simulation - level testing
        this.framebuffer= new Framebuffer(1940, 1080);
        this.pickingTexture = new PickingTexture(1940, 1080);
        glViewport(0, 0, 1940, 1080);
        this.imguiLayer = new ImGuiLayer(glfwWindow, pickingTexture);
        this.imguiLayer.initImGui();

        Window.changeScene(0);
    }

    public void loop() {
        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;


        Shader defaultShader = AssetPool.getShader("assets/shaders/default.glsl");
        Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");

        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();
            //todo: //=================================================================
            // todo : clear color marks a refresh : We can Initialize Debug Here :
            glDisable(GL_BLEND);
            pickingTexture.enableWriting();

            glViewport(0, 0, 1940, 1080);
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer.bindShader(pickingShader);
            currentScene.render();


            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                int x = (int)MouseListener.getScreenX();
                int y = (int)MouseListener.getScreenY();
                System.out.println(pickingTexture.readPixel(x, y) + " From Window Class ");
            }

            pickingTexture.disableWriting();
            glEnable(GL_BLEND);
            //todo: //=================================================================
            DebugDraw.beginFrame();
            //todo: //=================================================================
            // todo :    Debug Line Test Init : Next, In Update WE DRAW -

            //todo: //=================================================================
            this.framebuffer.bind();
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);
            //this.framebuffer.bind();
            // todo : bind frame buffer


            if (dt >= 0) {
                //todo: //=================================================================
                // todo :  IN UPDATE::    Debug Line DRAW : In Update WE DRAW -
                //todo: //=================================================================
                //todo:  Drawing here puts the LINES BEHIND - zIndex 0
/*                if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                    int x = (int)MouseListener.getScreenX();
                    int y = (int)MouseListener.getScreenY();
                    System.out.println(pickingTexture.readPixel(x, y));
                }*/
                //todo: //=================================================================
                DebugDraw.draw();
                //todo: //=================================================================
                // todo :  IN UPDATE::    Debug Line DRAW : In Update WE DRAW -
                Renderer.bindShader(defaultShader);
                //todo: //=================================================================
                currentScene.update(dt);
                currentScene.render();
            }
            // todo : unbind framebuffer
            this.framebuffer.unbind();

            this.imguiLayer.update(dt, currentScene);
            glfwSwapBuffers(glfwWindow);


            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
        // todo : save the current scene
        currentScene.saveExit();
    }

    public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public static void setWidth(int newWidth) {
        get().width = newWidth;
    }

    public static void setHeight(int newHeight) {
        get().height = newHeight;
    }

    public static Framebuffer getFramebuffer() {
        return get().framebuffer;
    }

    public static float getTargetAspectRatio() {
        return 16.0f / 9.0f;
    }
}
