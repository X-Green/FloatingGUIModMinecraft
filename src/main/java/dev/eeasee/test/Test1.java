package dev.eeasee.test;

public class Test1 {
    private static volatile String s = "bbb";

    public static void main(String[] args) {
        printVarInt(Integer.MAX_VALUE);
    }

    public static void printVarInt(int i) {
        while((i & -128) != 0) {
            System.out.println(i & 127 | 128);
            i >>>= 7;
        }

        System.out.println(i);
    }

}
