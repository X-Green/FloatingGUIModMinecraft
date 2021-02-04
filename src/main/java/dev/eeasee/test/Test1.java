package dev.eeasee.test;

import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.Quaternion;

public class Test1 {
    public static void main(String[] args) {

        Vector4f vector = new Vector4f(10, 20, 30, 1);

        vector.transform(Matrix4f.translate(5, 5, 5));

        System.out.println(vector);
    }
}
