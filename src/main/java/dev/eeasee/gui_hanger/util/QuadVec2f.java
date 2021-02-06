package dev.eeasee.gui_hanger.util;

import net.minecraft.util.math.Vec2f;

import java.util.Objects;

public class QuadVec2f {
    public final Vec2f[] vectors = new Vec2f[4];

    public QuadVec2f(Vec2f vectorA, Vec2f vectorB, Vec2f vectorC, Vec2f vectorD) {
        this.vectors[0] = vectorA;
        this.vectors[1] = vectorB;
        this.vectors[2] = vectorC;
        this.vectors[3] = vectorD;
    }

    public QuadVec2f(float a1, float a2, float b1, float b2, float c1, float c2, float d1, float d2) {
        this(
                new Vec2f(a1, a2),
                new Vec2f(b1, b2),
                new Vec2f(c1, c2),
                new Vec2f(d1, d2)
        );
    }


    @Override
    public int hashCode() {
        return Objects.hash((Object[]) vectors);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof QuadVec2f)) {
            return false;
        }
        Vec2f[] targets = ((QuadVec2f) o).vectors;
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
