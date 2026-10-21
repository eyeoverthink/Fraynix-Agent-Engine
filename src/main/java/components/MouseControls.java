package components;

import jade.GameObject;
import jade.MouseListener;
import jade.Window;
import util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {

    // todo : this i9s our mouse click helper
    GameObject holdingObject = null;

    public void pickupObject(GameObject go){
       // todo : set the game object picked up to the holding object
        this.holdingObject = go;
        // todo : add the game object to scene -- our save method is engaged from the change scene function
        Window.getScene().addGameObjectToScene(go);
    }

    public void place(){
        // todo : this is our method to place the holding object - if true -
        //  holding is now null - releasing the held object
        this.holdingObject = null;

    }

    @Override
    public void update(float dt){

        if (holdingObject != null){
            // todo : was - 16 at each variable -- works great as is :
            holdingObject.transform.position.x = MouseListener.getOrthoX();
            holdingObject.transform.position.y = MouseListener.getOrthoY();
            // todo : was - 16 at each variable -- works great as is :
            holdingObject.transform.position.x = (int)(holdingObject.transform.position.x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH;
            holdingObject.transform.position.y = (int)(holdingObject.transform.position.y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;
        // todo : if we are dragging with the left click button - we have an object and want to place it
            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                place();
            }
        }
    }


}
