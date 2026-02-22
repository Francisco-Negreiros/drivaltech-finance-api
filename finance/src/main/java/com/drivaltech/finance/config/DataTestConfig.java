package com.drivaltech.finance.config;

import com.drivaltech.finance.domain.Category;
import com.drivaltech.finance.domain.Transaction;
import com.drivaltech.finance.domain.TransactionType;
import com.drivaltech.finance.repository.CategoryRepository;
import com.drivaltech.finance.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataTestConfig implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public DataTestConfig(CategoryRepository categoryRepository,
                          TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) {

        // Criando categoria
        Category category = new Category();
        category.setName("Food");
        categoryRepository.save(category);

        // Criando transação
        Transaction transaction = new Transaction();
        transaction.setDescription("Pizza Friday");
        transaction.setAmount(new BigDecimal("79.90"));
        transaction.setDate(LocalDate.now());
        transaction.setType(TransactionType.EXPENSE);
        transaction.setCategory(category);

        transactionRepository.save(transaction);

    }
}