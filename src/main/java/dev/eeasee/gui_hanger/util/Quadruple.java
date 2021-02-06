package dev.eeasee.gui_hanger.util;

import java.util.Objects;

public class Quadruple<A, B, C, D> {
    public final A a;
    public final B b;
    public final C c;
    public final D d;

    public Quadruple(A a, B b, C c, D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c, d);
    }
}
