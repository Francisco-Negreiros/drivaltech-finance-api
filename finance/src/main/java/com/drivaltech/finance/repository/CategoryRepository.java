package com.drivaltech.finance.repository;

import com.drivaltech.finance.domain.Category;
import com.drivaltech.finance.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findByUserAndActiveTrue(User user);
}

