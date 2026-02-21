package com.drivaltech.finance;

import com.drivaltech.finance.domain.Category;
import com.drivaltech.finance.domain.Transaction;
import com.drivaltech.finance.domain.TransactionType;
import com.drivaltech.finance.repository.CategoryRepository;
import com.drivaltech.finance.repository.TransactionRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class DrivaltechFinanceApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(DrivaltechFinanceApiApplication.class, args);

	}

	@Bean
	CommandLineRunner runner(CategoryRepository categoryRepository,
							 TransactionRepository transactionRepository) {

		return args -> {

			// Criar categoria
			Category category = new Category();
			category.setName("Food");
			category.setDescription("Food expenses");

			category = categoryRepository.save(category);

			// Criar transação
			Transaction transaction = new Transaction();
			transaction.setDescription("Pizza Friday");
			transaction.setAmount(new BigDecimal("79.90"));
			transaction.setDate(LocalDate.now());
			transaction.setType(TransactionType.EXPENSE);
			transaction.setCategory(category);

			transactionRepository.save(transaction);

			System.out.println("Transação salva com sucesso!");
		};
	}
}
