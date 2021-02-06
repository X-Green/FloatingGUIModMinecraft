package dev.eeasee.gui_hanger.util;

import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.Quaternion;

import java.util.Objects;

public class QuadVec4f {
    public final Vector4f[] vectors = new Vector4f[4];

    public QuadVec4f(Vector4f vectorA, Vector4f vectorB, Vector4f vectorC, Vector4f vectorD) {
        this.vectors[0] = vectorA;
        this.vectors[1] = vectorB;
        this.vectors[2] = vectorC;
        this.vectors[3] = vectorD;
    }

    public QuadVec4f(float a1, float a2, float a3, float a4, float b1, float b2, float b3, float b4, float c1, float c2, float c3, float c4, float d1, float d2, float d3, float d4) {
        this(
                new Vector4f(a1, a2, a3, a4),
                new Vector4f(b1, b2, b3, b4),
                new Vector4f(c1, c2, c3, c4),
                new Vector4f(d1, d2, d3, d4)
        );
    }

    public QuadVec4f(float a1, float a2, float a3, float b1, float b2, float b3, float c1, float c2, float c3, float d1, float d2, float d3) {
        this(
                new Vector4f(a1, a2, a3, 1.0f),
                new Vector4f(b1, b2, b3, 1.0f),
                new Vector4f(c1, c2, c3, 1.0f),
                new Vector4f(d1, d2, d3, 1.0f)
        );
    }

    public QuadVec4f(float a1, float a2, float b1, float b2, float c1, float c2, float d1, float d2) {
        this(
                new Vector4f(a1, a2, 0.0f, 1.0f),
                new Vector4f(b1, b2, 0.0f, 1.0f),
                new Vector4f(c1, c2, 0.0f, 1.0f),
                new Vector4f(d1, d2, 0.0f, 1.0f)
        );
    }

    public void transform(Matrix4f matrix) {
        for (int i = 0; i < 4; i++) {
            this.vectors[i].transform(matrix);
        }
    }


    public void rotate(Quaternion rotation) {
        for (int i = 0; i < 4; i++) {
            this.vectors[i].rotate(rotation);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash((Object[]) vectors);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof QuadVec4f)) {
            return false;
        }
        Vector4f[] targets = ((QuadVec4f) o).vectors;
        if (targets.length != 4) {
            return false;
        }
        boolean flag = true;
        for (int i = 0; i < 4; i++) {
            flag &= (this.vectors[i].equals(targets[i]));
        }
        return flag;
    }
}
