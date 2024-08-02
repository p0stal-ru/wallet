package com.example.wallet_service.controllers;

import com.example.wallet_service.dto.WalletRequest;
import com.example.wallet_service.dto.WalletResponse;
import com.example.wallet_service.services.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> makeADeposit(@Valid @RequestBody WalletRequest walletRequest) {
        try {
            walletService.processTransaction(walletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @GetMapping("/{walletId}")
//    @ResponseStatus(HttpStatus.OK)
//    public WalletResponse getBalance(@PathVariable(name = "walletId") UUID walletId) {
//        BigDecimal balance = walletService.getBalance(walletId);
//        return new WalletResponse(walletId, balance);
//    }]

    @GetMapping("/{walletId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity <WalletResponse> getBalance(@PathVariable(name = "walletId") UUID walletId) {
        try {
            BigDecimal balance = walletService.getBalance(walletId);
            return ResponseEntity.ok()
                    .body(new WalletResponse(walletId, balance));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
