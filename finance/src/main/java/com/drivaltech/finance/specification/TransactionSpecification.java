package com.drivaltech.finance.specification;

import com.drivaltech.finance.domain.Transaction;
import com.drivaltech.finance.domain.TransactionType;
import com.drivaltech.finance.user.User;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionSpecification {

    public static Specification<Transaction> withFilters(
            TransactionType type,
            LocalDate startDate,
            LocalDate endDate,
            UUID categoryId,
            User user
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("user"), user));

            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }

            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), startDate));
            }

            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), endDate));
            }

            if (categoryId != null) {
                predicates.add(
                        cb.equal(root.get("category").get("id"), categoryId)
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
