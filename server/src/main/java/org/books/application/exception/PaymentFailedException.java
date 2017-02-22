package org.books.application.exception;

public class PaymentFailedException extends BookstoreException {

    private final Code code;

    public PaymentFailedException(PaymentFailedException.Code code) {

        this.code = code;
    }

    public PaymentFailedException.Code getCode() {
        return code;
    }

    public enum Code {

        CREDIT_CARD_EXPIRED,
        INVALID_CREDIT_CARD,
        PAYMENT_LIMIT_EXCEEDED

    }
}
