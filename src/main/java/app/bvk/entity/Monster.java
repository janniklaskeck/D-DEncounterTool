package app.bvk.entity;

import com.google.gson.JsonObject;

public class Monster extends Creature {

    public Monster(final String name, final String path) {
        super(name, path);
    }

    public Monster(final JsonObject jo) {
        super(jo);
    }
}
