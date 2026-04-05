package com.drivaltech.finance.service;

import com.drivaltech.finance.domain.*;
import com.drivaltech.finance.dto.CreateTransactionRequest;
import com.drivaltech.finance.exception.ForbiddenException;
import com.drivaltech.finance.repository.*;
import com.drivaltech.finance.user.Role;
import com.drivaltech.finance.user.User;
import com.drivaltech.finance.user.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Category category;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("valter");
        user.setRole(Role.USER);

        category = new Category();
        category.setId(UUID.randomUUID());
        category.setUser(user);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("valter");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldCreateTransactionSuccessfully() {

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setDescription("Test");
        request.setAmount(BigDecimal.valueOf(100));
        request.setDate(LocalDate.now());
        request.setType("INCOME");
        request.setCategoryId(category.getId());

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(user));

        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.of(category));

        when(transactionRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = transactionService.create(request);

        assertNotNull(response);
        verify(transactionRepository).save(any());
    }

    @Test
    void shouldThrowForbiddenWhenCategoryBelongsToAnotherUser() {

        User anotherUser = new User();
        anotherUser.setId(UUID.randomUUID());

        category.setUser(anotherUser);

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setCategoryId(category.getId());
        request.setType("INCOME");

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(user));

        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.of(category));

        assertThrows(ForbiddenException.class, () -> {
            transactionService.create(request);
        });
    }
    @Test
    void shouldUpdateTransactionSuccessfully() {

        UUID transactionId = UUID.randomUUID();

        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setUser(user);

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setDescription("Updated");
        request.setAmount(BigDecimal.valueOf(200));
        request.setDate(LocalDate.now());
        request.setType("EXPENSE");
        request.setCategoryId(category.getId());

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(user));

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.of(transaction));

        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.of(category));

        when(transactionRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = transactionService.update(transactionId, request);

        assertNotNull(response);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void shouldThrowForbiddenWhenUpdatingTransactionFromAnotherUser() {

        UUID transactionId = UUID.randomUUID();

        User anotherUser = new User();
        anotherUser.setId(UUID.randomUUID());

        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setUser(anotherUser); // dono diferente

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setCategoryId(category.getId());
        request.setType("INCOME");

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(user));

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.of(transaction));

        assertThrows(ForbiddenException.class, () -> {
            transactionService.update(transactionId, request);
        });
    }

    @Test
    void shouldDeleteTransactionSuccessfully() {

        UUID transactionId = UUID.randomUUID();

        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setUser(user);

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(user));

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.of(transaction));

        doNothing().when(transactionRepository).delete(transaction);

        transactionService.delete(transactionId);

        verify(transactionRepository).delete(transaction);
    }

    @Test
    void shouldThrowForbiddenWhenDeletingTransactionFromAnotherUser() {

        UUID transactionId = UUID.randomUUID();

        User anotherUser = new User();
        anotherUser.setId(UUID.randomUUID());

        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setUser(anotherUser);

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(user));

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.of(transaction));

        assertThrows(ForbiddenException.class, () -> {
            transactionService.delete(transactionId);
        });
    }

    @Test
    void shouldReturnTransactionWhenUserIsOwner() {

        UUID transactionId = UUID.randomUUID();

        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setUser(user);

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(user));

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.of(transaction));

        var response = transactionService.findById(transactionId);

        assertNotNull(response);
    }

    @Test
    void shouldThrowForbiddenWhenUserIsNotOwner() {

        UUID transactionId = UUID.randomUUID();

        User anotherUser = new User();
        anotherUser.setId(UUID.randomUUID());

        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setUser(anotherUser);

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(user));

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.of(transaction));

        assertThrows(ForbiddenException.class, () -> {
            transactionService.findById(transactionId);
        });
    }

}
