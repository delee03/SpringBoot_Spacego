package com.hutech.lab02.service;
import com.hutech.lab02.model.ProductImage;
import com.hutech.lab02.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    public List<ProductImage> getAllProductImages() {
        return productImageRepository.findAll();
    }

    public void addProductImage(ProductImage productImage) {
        productImageRepository.save(productImage);
    }
}
