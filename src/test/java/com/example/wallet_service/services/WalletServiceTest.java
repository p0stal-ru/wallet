package com.example.wallet_service.services;

import com.example.wallet_service.dto.WalletRequest;
import com.example.wallet_service.enums.OperationType;
import com.example.wallet_service.exceptions.InsufficientFundsException;
import com.example.wallet_service.exceptions.WalletNotFoundException;
import com.example.wallet_service.models.Wallet;
import com.example.wallet_service.repositories.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService underTest;

    private WalletRequest depositRequest;
    private WalletRequest withdrawRequest;
    private Wallet wallet;
    private final UUID uuid = UUID.randomUUID();

    @BeforeEach
    public void setUp() {

        wallet = new Wallet();
        wallet.setId(uuid);
        wallet.setBalance(new BigDecimal("100.00"));

        depositRequest = new WalletRequest();
        depositRequest.setWalletId(uuid.toString());
        depositRequest.setOperationType(OperationType.DEPOSIT.name());
        depositRequest.setAmount("50.00");

        withdrawRequest = new WalletRequest();
        withdrawRequest.setWalletId(uuid.toString());
        withdrawRequest.setOperationType(OperationType.WITHDRAW.name());
        withdrawRequest.setAmount("50.00");
    }

    @Test
    public void testDeposit() {
        when(walletRepository.findById(uuid)).thenReturn(Optional.of(wallet));

        underTest.processTransaction(depositRequest);

        assertEquals(new BigDecimal("150.00"), wallet.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    public void testWithdraw() {
        when(walletRepository.findById(uuid)).thenReturn(Optional.of(wallet));

        underTest.processTransaction(withdrawRequest);

        assertEquals(new BigDecimal("50.00"), wallet.getBalance());
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        withdrawRequest.setAmount("200.00");
        when(walletRepository.findById(uuid)).thenReturn(Optional.of(wallet));

        assertThrows(InsufficientFundsException.class, () ->
                underTest.processTransaction(withdrawRequest));

        assertEquals("100.00", wallet.getBalance().toString());
        verify(walletRepository, never()).save(wallet);
    }


    @Test
    public void testInvalidOperationType() {
        WalletRequest invalidRequest = new WalletRequest();
        invalidRequest.setWalletId(uuid.toString());
        invalidRequest.setOperationType(OperationType.WITHDRAW.name() + OperationType.DEPOSIT.name());
        invalidRequest.setAmount("50.00");

        when(walletRepository.findById(uuid)).thenReturn(Optional.of(wallet));

        assertThrows(IllegalArgumentException.class, () ->
                underTest.processTransaction(invalidRequest));

        assertEquals(new BigDecimal("100.00"), wallet.getBalance());
        verify(walletRepository, never()).save(wallet);
    }

    @Test
    public void testWalletNotFound() {
        when(walletRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () ->
                underTest.processTransaction(depositRequest));

        verify(walletRepository, never()).save(wallet);
    }
}
