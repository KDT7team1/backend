package com.exam.adminGoods;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
   // Optional<CategoryEntity> findByFirstNameAndSecondName(String firstName, String secondName);
}
