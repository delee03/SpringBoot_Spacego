package com.hutech.lab02.repository;
import org.springframework.web.multipart.MultipartFile;
import com.hutech.lab02.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
