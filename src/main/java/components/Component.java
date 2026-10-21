package components;

import imgui.ImGui;
import jade.GameObject;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Component {

    private static int ID_COUNTER = 0;
    private int uid = -1;
    public transient GameObject gameObject = null;

    public void start() {

    }

    public void update(float dt) {

    }
    public void destroy() {

    }


    // todo : this is the main ImGui function that the rest can override
    //  to add window functionality to class variables

    public void imgui() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields){
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if (isTransient){
                    continue;
                }
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if(isPrivate){
                    field.setAccessible(true);
                }

                // todo : our field perameters
                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                // todo : declaring
                if (type == int.class) {
                    int val = (int) value;
                    int[] imInt = {val};
                    if (ImGui.dragInt(name + " : ", imInt)) {
                        field.set(this, imInt[0]);
                    }
                } else if (type == float.class){
                   float val = (float)value;
                   float[] imFloat = {val};
                   if (ImGui.dragFloat(name + " : ",  imFloat)){
                       field.set(this, imFloat[0]);
                   }
                } else if (type == boolean.class){
                    boolean val = (boolean)value;
                    boolean[] imBool = {val};
                    if (ImGui.checkbox( name + " : ", val)){
                       field.set(this, !val);
                    }
                } else if (type == Vector3f.class) {
                    Vector3f val = (Vector3f)value;
                    float[] imVec = {val.x, val.y, val.z};
                    if (ImGui.dragFloat3(name + " : ", imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2]);
                    }
                } else if (type == Vector4f.class) {
                    Vector4f val = (Vector4f) value;
                    float[] imVec = {val.x, val.y, val.z, val.w};
                    if (ImGui.dragFloat4(name + " : ", imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                    }
                }

                if (isPrivate){
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    // todo : adding UID variables for game object identification

    public void generateId() {
        if (this.uid == -1) {
            this.uid = ID_COUNTER++;
        }
    }

    private <T extends Enum<T>> String[] getEnumValues(Class<T> enumType) {
        String[] enumValues = new String[enumType.getEnumConstants().length];
        int i = 0;
        for (T enumIntegerValue : enumType.getEnumConstants()) {
            enumValues[i] = enumIntegerValue.name();
            i++;
        }
        return enumValues;
    }

    private int indexOf(String str, String[] arr) {
        for (int i=0; i < arr.length; i++) {
            if (str.equals(arr[i])) {
                return i;
            }
        }

        return -1;
    }

    public int getUid(){
        return this.uid;
    }

    public static void init(int maxId){
        ID_COUNTER = maxId;
    }


}
