package com.drivaltech.finance.controller;

import com.drivaltech.finance.domain.Category;
import com.drivaltech.finance.domain.Transaction;
import com.drivaltech.finance.domain.TransactionType;
import com.drivaltech.finance.dto.CreateTransactionRequest;
import com.drivaltech.finance.dto.PaginationResponse;
import com.drivaltech.finance.dto.TransactionResponse;
import org.springframework.data.domain.Pageable;
import com.drivaltech.finance.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(
            @Valid @RequestBody CreateTransactionRequest request) {

        Transaction transaction = new Transaction();
        transaction.setDescription(request.getDescription());
        transaction.setAmount(request.getAmount());
        transaction.setDate(request.getDate());
        transaction.setType(TransactionType.valueOf(request.getType()));

        Category category = new Category();
        category.setId(request.getCategoryId());
        transaction.setCategory(category);

        Transaction saved = transactionService.create(transaction);

        TransactionResponse response = TransactionResponse.fromEntity(saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public PaginationResponse<TransactionResponse> list(Pageable pageable) {

        var page = transactionService.findAll(pageable);

        return new PaginationResponse<>(page);
    }

}
