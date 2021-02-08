package dev.eeasee.test;

public class Test1 {
    private static volatile String s = "bbb";

    public static void main(String[] args) throws InterruptedException {

        for (; ; ) {
            System.out.println(s);
            Thread.sleep(1000);
        }
    }
}
