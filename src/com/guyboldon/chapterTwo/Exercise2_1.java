package com.guyboldon.chapterTwo;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Exercise2_1 {

    /**
     * Exercise 2_2 is implicit here, as I already knew the lambda vs anonymous class implementations
     */
    public static <A, B, C> Function<A, C> combine(Function<A, B> functionOne, Function<B, C> functionTwo) {
//        return functionTwo.compose(functionOne);
        return (A a) -> functionTwo.apply(functionOne.apply(a));
    }

    /**
     * with Integer as the type:
     */
//    public static Function<Integer, Integer> combine(Function<Integer, Integer> functionOne, Function<Integer, Integer> functionTwo) {
//        return x -> functionTwo.apply(functionOne.apply(x));
//    }
}
