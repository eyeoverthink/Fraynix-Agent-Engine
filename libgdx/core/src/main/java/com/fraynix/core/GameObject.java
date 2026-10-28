// ─── GameObject.java ─────────────────────────────────────
package com.fraynix.core;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int uid = -1;
    private String name;
    private Array<Component> components;
    public Transform transform;
    public int zIndex;

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.zIndex = zIndex;
        this.components = new Array<>();
        this.transform = transform;
        this.uid = ID_COUNTER++;
    }

    public GameObject(String name, Transform transform) {
        this(name, transform, 0);
    }

    public GameObject(String name) {
        this(name, new Transform(), 0);
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                return componentClass.cast(c);
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size; i++) {
            if (componentClass.isAssignableFrom(components.get(i).getClass())) {
                components.removeIndex(i);
                return;
            }
        }
    }

    public void addComponent(Component c) {
        c.gameObject = this;
        components.add(c);
        c.setUid(ID_COUNTER++);
    }

    public void start() {
        for (Component c : components) {
            c.start();
        }
    }

    public void update(float dt) {
        for (Component c : components) {
            c.update(dt);
        }
    }

    public void imgui() {
        for (Component c : components) {
            c.imgui();
        }
    }

    public int getUid() { return this.uid; }
    public String getName() { return this.name; }
    public Array<Component> getComponents() { return this.components; }

    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }
}
