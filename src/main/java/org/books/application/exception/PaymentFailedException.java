package org.books.application.exception;

public class PaymentFailedException extends BookstoreException {

    private final Code code;

    public PaymentFailedException(Code code) {

        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    private static class Code {

    }
}
