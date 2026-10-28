// ─── Component.java ──────────────────────────────────────
package com.fraynix.core;

public abstract class Component {
    public GameObject gameObject;
    private int uid = -1;

    public void start() {}
    public void update(float dt) {}
    public void imgui() {}

    public void setUid(int uid) { this.uid = uid; }
    public int getUid() { return this.uid; }
}
