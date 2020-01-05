package com.guyboldon.chapterThree;

import com.guyboldon.Function;

import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * Something I find typical of function style in java, all the business logic is now contained in this class.
 * it's not offloaded somewhere else or in the Results class, the functions are set here and passed on and all
 * applied here.
 * <p>
 * All variables are final.
 * <p>
 * Value Types (basically wrapping primitives, similar to BigDecimal) in an object so that there can be no
 * confusion with standard types (like using double for both price and weight).
 * Real Value Types, objects handled like primitives(performance), is a feature proposed by Project Valhalla, coming sometime...
 */
public class EmailValidation {

    private static final Pattern emailPattern =
            Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");

    // use a function to check the result. This separates the computation and effects, so that we can easily test the
    //  computation part of the program. In the imperative style, these are harder to separate.
    private static final Function<String, Result<String>> emailChecker =
            s ->
                    // the tertiary operator is naturally a pure function
                    // we can even get rid of these if... else statements by implementing
                    //  Supplier(s) of Boolean and value, to do a single if with a for over
                    //  the various vararg matchers
                    s == null
                            ? Result.failure("email must not be null")
                            : s.isBlank()
                            ? Result.failure("email must not be empty")
                            : emailPattern.matcher(s).matches()
                            ? Result.success(s)
                            : Result.failure("email " + s + " is invalid.");

    // the consumer function interface takes an argument and returns nothing. This is very practical for
    //  dealing with side effects.
    private static final Consumer<String> failure = s ->
            System.err.println("Error message logged: " + s);

    private static final Consumer<String> success = s ->
            System.out.println("Mail sent to " + s);

    // a helper function, just to clean up the code a bit
    public static final Consumer<String> validate = s ->
            emailChecker.apply(s).bind(success, failure);

    public static void main(String... args) {
        validate.accept("this.is@my.email");
        validate.accept(null);
        validate.accept("");
        validate.accept("john.doe@acme.com");
    }
}
