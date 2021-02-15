package dev.eeasee.test;

import dev.eeasee.gui_hanger.sprites.renderer.InventorySprite;

import java.util.EnumMap;

public class Test1 {
    private static volatile String s = "bbb";

    public static void main(String[] args) {
        try {
            for (int i = 0; i < 10000; i++) {
                //...
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 10000; i++) {
            try {
                //...
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
