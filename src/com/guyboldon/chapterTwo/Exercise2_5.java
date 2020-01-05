package com.guyboldon.chapterTwo;

import java.util.function.Function;

public class Exercise2_5 {

    public static <A, B, C> Function<A, C> compose(Function<A, B> functionOne, Function<B, C> functionTwo) {
        return (A a) -> functionTwo.apply(functionOne.apply(a));
    }

    public static <A, B, C> Function<Function<A, B>, Function<Function<B, C>, Function<A, C>>> composeFunction() {
        return functionOne -> functionTwo -> (A initialValue) -> functionTwo.apply(functionOne.apply(initialValue));
    }

    public static void main(String[] args) {
        Function<Integer, Long> functionOne = integer -> (long) (integer * 2);
        Function<Long, String> functionTwo = aLong -> Long.toString(aLong);
        String result = compose(functionOne, functionTwo).apply(5);
        System.out.println("result = " + result);
        // wow Java....
        result = Exercise2_5.<Integer, Long, String>composeFunction().apply(functionOne).apply(functionTwo).apply(5);
        System.out.println("result = " + result);

        result = compose(
                (Integer integer) -> (long) integer * 2,
                aLong -> Long.toString(aLong))
                .apply(5);
        System.out.println("result = " + result);
    }
}
