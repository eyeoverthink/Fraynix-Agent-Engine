package components;

import jade.Camera;
import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.DebugDraw;
import util.Settings;

public class GridLines extends Component{
    Camera camera = Window.getScene().camera();


    @Override
    public void update(float dt){
        // todo : create the snap to Grid Function
        Vector2f cameraPos = Window.getScene().camera().position;
        Vector2f projectionSize = Window.getScene().camera().getProjectionSize();
        // todo : create the snap to Grid Function
        int firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_WIDTH;
        int firstY = ((int)(cameraPos.y / Settings.GRID_HEIGHT) - 1 )* Settings.GRID_HEIGHT;

        // todo : create the snap to Grid Function
        int numVtLines = (int)(projectionSize.x / Settings.GRID_WIDTH) + 2;
        int numHzLines = (int)(projectionSize.y / Settings.GRID_HEIGHT) + 2;

        // todo : create the snap to Grid Function
        int width = (int)projectionSize.x + Settings.GRID_WIDTH * 2;
        int height = (int)projectionSize.y + Settings.GRID_HEIGHT * 2;

        // todo : Loop Through background seperate squares


        int maxLines = Math.max(numVtLines, numHzLines);
        Vector3f color = new Vector3f(0.2f, 0.2f, 0.2f);
//        for (int i=0; i < maxLines; i++) {
//            int x = firstX + (Settings.GRID_WIDTH * i);
//            int y = firstY + (Settings.GRID_HEIGHT * i);
//
//            if (i < numVtLines) {
//                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);
//            }
//
//            if (i < numHzLines) {
//                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
//            }
//        }
        for (int i=0; i < maxLines; i++) {
            int x = firstX + (Settings.GRID_WIDTH * i);
            int y = firstY + (Settings.GRID_HEIGHT * i);

            if (i < numVtLines) {
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);
            }

            if (i < numHzLines) {
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
            }
        }
    }
}



