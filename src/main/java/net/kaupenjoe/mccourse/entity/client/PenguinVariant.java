package net.kaupenjoe.mccourse.entity.client;

import java.util.Arrays;
import java.util.Comparator;

public enum PenguinVariant {
    DEFAULT(0),
    ALBINO(1),
    CLUB(2);

    private static final PenguinVariant[] BY_ID = Arrays.stream(values()).sorted(
            Comparator.comparingInt(PenguinVariant::getId)).toArray(PenguinVariant[]::new);
    private final int id;

    PenguinVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static PenguinVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
