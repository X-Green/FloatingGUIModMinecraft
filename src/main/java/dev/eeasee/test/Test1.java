package dev.eeasee.test;

import dev.eeasee.gui_hanger.sprites.renderer.InventorySprite;

import java.util.EnumMap;

public class Test1 {
    private static volatile String s = "bbb";

    public static void main(String[] args) {
        // System.out.println(new InventorySprite(0).getItemCoordinate(34));
        System.out.println(System.currentTimeMillis());

        System.out.println(System.currentTimeMillis());
        System.out.println(System.currentTimeMillis());
    }

    public static void printVarInt(int i) {
        while ((i & -128) != 0) {
            System.out.println(i & 127 | 128);
            i >>>= 7;
        }

        System.out.println(i);
    }

    private enum Alphabet {
        A,
        B,
        C,
        D,
        E,
        F,
        G,
        H,
        I,
        J
    }

}
