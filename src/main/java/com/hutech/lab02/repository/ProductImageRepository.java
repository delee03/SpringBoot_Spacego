package com.hutech.lab02.repository;
import com.hutech.lab02.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository
        extends JpaRepository<ProductImage, Long> {
}
