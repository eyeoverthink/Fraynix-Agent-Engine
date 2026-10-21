package physics2d.rigidbody;

import components.Component;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Rigidbody  {
    private int colliderType = 0;
    private float friction = 0.8f;
    private Vector3f velocity = new Vector3f(0, 0.5f, 0);
    public transient Vector4f tmp = new Vector4f(0, 0, 0, 0);
    private Vector2f position = new Vector2f();
    private float rotation = 0.0f;

    public Vector2f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }


}
