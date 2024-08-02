package com.example.wallet_service.exceptions;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException() {
        super("Кошелька с таким UUID не существует");
    }
}