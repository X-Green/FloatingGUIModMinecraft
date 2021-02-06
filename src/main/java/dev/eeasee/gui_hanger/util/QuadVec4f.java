package dev.eeasee.gui_hanger.util;

import net.minecraft.client.util.math.Vector4f;

import java.util.Objects;

public class QuadVec4f {
    public final Vector4f[] vectors = new Vector4f[4];

    public QuadVec4f(Vector4f vectorA, Vector4f vectorB, Vector4f vectorC, Vector4f vectorD) {
        this.vectors[0] = vectorA;
        this.vectors[1] = vectorB;
        this.vectors[2] = vectorC;
        this.vectors[3] = vectorD;
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
