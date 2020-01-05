package com.guyboldon.chapterTwo;

public class Pair<A, B> {
    private final A a;
    private final B b;

    public Pair(final A a, final B b) {
        this.a = a;
        this.b = b;
    }

    public static <C, D> Pair<C, D> of(C a, D b) {
        return new Pair<>(a, b);
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }
}
