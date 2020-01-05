package com.guyboldon.chapterTwo;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Exercise_2_7 {

    // function currying
    // in comparison to using BiFunction or Tuples and Triples...

    // multiple parameters by functions helped to hold to Pure Functions, which allow for more readable programs -
    //  as it's easier to read and follow due to no -side effects, or influences to functions/methods that come from
    //  somewhere not in the explicit or implicit paramenters(implicit final).


    // BiFunction: (multiple parameters)
    public static BiFunction<Double, Double, Double> howLongToGetThere = (distance, speed) -> distance / speed;

    public static Double runBiFunction(Double distance, Double speed) {
        return howLongToGetThere.apply(distance, speed);
    }


    // currying:
    public static Function<Double, Function<Double, Double>> howLongToGetThere_curry = distance -> speed -> distance / speed;

    public static Double runCurryingFunction(Double distance, Double speed) {
        return howLongToGetThere_curry.apply(distance).apply(speed);
    }

    // 2.7: write a functional method to partially apply a function with 2 arguments to it's first argument
    public static Function<Double, Double> setDistance(Double distance) {
        return howLongToGetThere_curry.apply(distance);
    }

    // generic version:
    public static <A, B, C> Function<B, C> setFirst(A parameter, Function<A, Function<B, C>> function) {
        return function.apply(parameter);
    }

    // 2.8: write functional method to the second argument / not clear for me what he meant
//    public static <B, C> C applySecond(B parameter, Function<B, C> function) {
//        return function.apply(parameter);
//    } // NOPE!
    // partial apply in this sence means setting a parameter fest, and letting the other parameters change and simplifiying the resulting function
    public static <A, B, C> Function<A, C> setSecond(B parameter, Function<A, Function<B, C>> curryFunction) {
        return a -> curryFunction.apply(a).apply(parameter);
    }


    public static void main(String[] args) {
        setFirst(2.5, howLongToGetThere_curry);//.apply(speed) // need this function defined so that the compiler knows what the type of the 2nd parameter is.
        setSecond(5.2, howLongToGetThere_curry);//.apply(distance)
    }


    // 2.9 Convert following method into a curried function:
    public static <A, B, C, D> String format(A a, B b, C c, D d) {
        return String.format("%s, %s, %s, %s", a, b, c, d);
    }

    public static <A, B, C, D> Function<A, Function<B, Function<C, Function<D, String>>>> quadCurryFunction() {
        return a -> b -> c -> d -> String.format("%s, %s, %s, %s", a, b, c, d);
    }

    // 2.10 write a function that currys: function<Tuple<A, B>, C>
    public static <A, B, C> Function<A, Function<B, C>> curryTupleFunction(Function<Pair<A, B>, C> tupleFunction) {
        return a -> b -> tupleFunction.apply(Pair.of(a, b));
    }

    // 2.11 swap curried arguments of existing curried function:
    public static <A, B, C> Function<B, Function<A, C>> swapArguments(Function<A, Function<B, C>> curriedFunction) {
        return b -> a -> curriedFunction.apply(a).apply(b);
    }


    // 2.12 Recursive FUNCTION:
    public static final Function<Integer, Integer> factorialFunction =
            number -> number <= 1 ? 1 : number * Exercise_2_7.factorialFunction.apply(number - 1);
    public final Function<Integer, Integer> factorialFunctionInstance =
            number -> number <= 1 ? 1 : number * this.factorialFunctionInstance.apply(number - 1);

//    public static Function<Integer, Integer> facFunction;
//    static { // the dirty/manual way, not as clean as above
//        facFunction =
//                number -> number <= 1 ? 1 : number * facFunction.apply(number - 1);
//    }

}
