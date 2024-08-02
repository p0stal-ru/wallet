package com.example.wallet_service.exceptions;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Недостаточно средств");
    }
}
