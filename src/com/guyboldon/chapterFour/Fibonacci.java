package com.guyboldon.chapterFour;

import java.math.BigInteger;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Fibonacci {

    private static final long FIVE_SECONDS = SECONDS.toMillis(5);

    // standard fibonacci implementation
    public static long fibonacci(long number) {
        return (number == 0 || number == 1)
                ? number
                // in the standard method, both recursive calls must return first, before the addition can be done.
                //  aka Java must hold on to the context of each method call and each method calls 2 recursive methods
                : fibonacci(number - 1) + fibonacci(number - 2);
    }

    public static void main(String[] args) throws InterruptedException {
        // test simple fibonacci recursive function:
        var n = 45;
        System.err.println("-----------------------------------------------------------------------------------------");
        System.err.printf("normal recursive function%n fibonacci numbers from 0 to %,d:%n", n);
        System.err.println("pausing...");
        Thread.sleep(FIVE_SECONDS);
        System.err.println("go");
        for (int i = 0; i <= n; i++) {
            System.out.printf("- %d%n", fibonacci(i));
        }

        // test tail recursive version: (still on stack, limited to about 7500 recursive calls)
        //  you can test it and it will do more than that, but that's through a compiler trick and doesn't work
        //  in all circumstances. It's still Stack limited.
        n = 5_000;
        System.err.println("-----------------------------------------------------------------------------------------");
        System.err.printf("tail recursive function%n fibonacci numbers from 0 to %,d:%n", n);
        System.err.println("pausing...");
        Thread.sleep(FIVE_SECONDS);
        System.err.println("go");
        for (int i = 0; i <= n; i++) {
            System.out.printf("- %d%n", fibTail(i));
        }

        // test our heap version:
        System.err.println("-----------------------------------------------------------------------------------------");
        System.err.println("tail recursive Heap function:");
        n = 1_000_000;
        System.err.printf("Fibonacci number for: %,d%n", n);
        System.err.println("go");
        final BigInteger bigNumber = fibTailHeap(n);
        System.out.printf("%,d%n", bigNumber);
        System.err.printf("That's a number with %,d digits!%n", bigNumber.toString().length());
    }

    /**
     * Exercise 4.1
     * <p>
     * Create a tail recursive version of the Fibonacci functional method
     * (tail recursion 101)
     */

    // we need BigIntegers now because the numbers are getting crazy long
    public static BigInteger fibTail(long number) {
        return fibTailRecursive(BigInteger.valueOf(number), BigInteger.ONE, BigInteger.ZERO);
    }

    // we use 2 accumulators because we're always adding the last two fibonacci numbers together.
    // acc1 will become the current number(acc2) and acc2 will become the sum of the previous and current number (acc1 + acc2)
    // thereby passing all needed information to the next recursive call.
    private static BigInteger fibTailRecursive(BigInteger number, BigInteger accumulator1, BigInteger accumulator2) {
        return number.compareTo(BigInteger.ONE) < 0 // OR: number == 0 (but I like to err on the safe side)
                ? BigInteger.ZERO
                : number.compareTo(BigInteger.valueOf(2)) < 0 // OR: number == 1
                ? accumulator1.add(accumulator2)
                : fibTailRecursive(number.subtract(BigInteger.ONE), accumulator2, accumulator1.add(accumulator2));
        // tail call means the recursive call is the last thing this method has to do, therefor the context is no
        //  longer necessary. Of course java doesn't do TCE, but it still can help tremendously with recursive calls,
        //  in this case instead of 2 recursive calls with context saving, we're only doing one
        //  recursive call and storing the results in accumulators
    }

    /**
     * Exercise 4.2
     * <p>
     * Turn this method into a stack-safe recursive one.
     */

    // stack safe simply means using classes(Objects) to store the recursive calls
    // instead of method calls. This makes the JVM use the heap instead of the stack
    // letting us get around the stack limitation.
    public static BigInteger fibTailHeap(long number) {
        return fibTailRecursiveHeap(
                BigInteger.valueOf(number),
                BigInteger.ONE,
                BigInteger.ZERO)
                .evaluate();
    }

    private static TailRecursive<BigInteger> fibTailRecursiveHeap(BigInteger number,
                                                                  BigInteger accumulator1,
                                                                  BigInteger accumulator2) {
        return number.compareTo(BigInteger.ONE) < 0
                ? TailRecursive.retUrn(BigInteger.ZERO)
                : number.compareTo(BigInteger.valueOf(2)) < 0
                ? TailRecursive.retUrn(accumulator1.add(accumulator2))
                :
                TailRecursive.call(
                        () ->
                                fibTailRecursiveHeap(
                                        number.subtract(BigInteger.ONE),
                                        accumulator2,
                                        accumulator1.add(accumulator2)));
    }

    /**
     * This is our TailRecursive class (Functor?) to use Objects to handle recursive calls
     * in the heap.
     *
     * @param <T>
     */
    abstract static class TailRecursive<T> {

        public abstract TailRecursive<T> resume(); // how we continue the recursive function calls

        public abstract T evaluate(); // in essence: get a result

        public abstract boolean isCall(); // a helper method to avoid having to use 'instanceof' to know if we're
        // dealing with a Return- or Call-Class instance.

        private TailRecursive() {
        }

        /**
         * The Return subclass that handles the base case.
         *
         * @param <T>
         */
        private static class Return<T> extends TailRecursive<T> {

            private final T t;

            private Return(T t) {
                this.t = t;
            }

            @Override
            public T evaluate() {
                return t;
            }

            @Override
            public boolean isCall() {
                return false;
            }

            @Override
            public TailRecursive<T> resume() {
                throw new IllegalStateException("Return has no resume");
            }
        }

        /**
         * The Suspend/Call subclass that handles the continuing calculations.
         *
         * @param <T>
         */
        private static class Call<T> extends TailRecursive<T> {

            private final Supplier<TailRecursive<T>> nextStep;

            private Call(Supplier<TailRecursive<T>> nextStep) {
                this.nextStep = nextStep;
            }

            @Override
            public T evaluate() {
                // this is in essence a helper method, to go down the call stack to get the value
                TailRecursive<T> tailRecursive = this; // 'this' could be either a Call- or Return-Class instance.
                while (tailRecursive.isCall()) {
                    tailRecursive = tailRecursive.resume();
                }
                return tailRecursive.evaluate();
            }

            @Override
            public boolean isCall() {
                return true;
            }

            @Override
            public TailRecursive<T> resume() {
                return nextStep.get();
            }
        }

        // static factory methods:
        public static <T> Return<T> retUrn(T t) {
            return new Return<>(t);
        }

        public static <T> Call<T> call(Supplier<TailRecursive<T>> supplier) {
            return new Call<>(supplier);
        }
    }
}
