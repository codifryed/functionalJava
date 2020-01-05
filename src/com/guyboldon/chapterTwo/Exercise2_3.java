package com.guyboldon.chapterTwo;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Exercise2_3 {

    /**
     * Write a function to combine two Integers
     */
    public static Function<Integer, Function<Integer, Integer>> add() {
        return x -> y -> x + y;
    }
}
