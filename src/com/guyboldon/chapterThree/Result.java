package com.guyboldon.chapterThree;

import java.util.function.Consumer;

public interface Result<T> {

    // bind has to bind both options so that is can easily be implemented without knowing the result
    void bind(Consumer<T> success, Consumer<String> failure);

    // static interface factory methods, like helper methods to return proper instance of proper type
    static <T> Result<T> failure(String message) {
        return new Failure<>(message);
    }

    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    // we can put the implementations right here, quite handy and can use private constructors
    class Success<T> implements Result<T> {

        private final T value;

        private Success(T t) {
            value = t;
        }

        @Override
        public void bind(Consumer<T> success, Consumer<String> failure) {
            // we only want to run the success function in case of success
            success.accept(value);
        }
    }

    // Failure always is type of String
    class Failure<T> implements Result<T> {

        private final String errorMessage;

        private Failure(String s) {
            errorMessage = s;
        }

        @Override
        public void bind(Consumer<T> success, Consumer<String> failure) {
            failure.accept(errorMessage);
        }

        public String getMessage() {
            return errorMessage;
        }
    }
}
