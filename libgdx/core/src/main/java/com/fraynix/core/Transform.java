// ─── Transform.java ──────────────────────────────────────
package com.fraynix.core;

import com.badlogic.gdx.math.Vector2;

public class Transform {
    public Vector2 position;
    public Vector2 scale;
    public float rotation = 0;

    public Transform() {
        this.position = new Vector2(0, 0);
        this.scale = new Vector2(1, 1);
    }

    public Transform(Vector2 position, Vector2 scale) {
        this.position = new Vector2(position);
        this.scale = new Vector2(scale);
    }

    public Transform copy() {
        return new Transform(this.position, this.scale);
    }

    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Transform)) return false;
        Transform t = (Transform) obj;
        return t.position.equals(this.position) && t.scale.equals(this.scale);
    }
}
