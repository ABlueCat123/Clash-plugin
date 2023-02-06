package com.github.abluecat123.clash.util;

import org.jetbrains.annotations.NotNull;

public class Pair<A, B> {

    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A first()
    {
        return this.first;
    }

    public B second()
    {
        return this.second;
    }

    public static <A, B> @NotNull Pair<A, B> makePair(@NotNull A first, @NotNull B second) {
        return new Pair<>(first, second);
    }
}
