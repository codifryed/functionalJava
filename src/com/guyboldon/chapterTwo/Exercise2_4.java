package com.guyboldon.chapterTwo;

import java.util.function.Function;

public class Exercise2_4 <A, B, C>{

    /**
     * Higher Oder Function HOF
     */
    public static void main(String[] args) {
        Function<Function<Integer, Function<Integer, Integer>>, Function<Integer, Integer>> composeFunction =
                x -> y -> y * 3;
        Function<Function<Integer, Integer>, Function<Function<Integer, Integer>, Function<Integer, Integer>>> composeFunctionTwo =
              x -> y -> z -> x.apply(y.apply(z));
    }
}
