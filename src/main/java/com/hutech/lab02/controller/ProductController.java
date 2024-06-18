package com.hutech.lab02.controller;

import com.hutech.lab02.model.Category;
import com.hutech.lab02.model.Product;
import com.hutech.lab02.model.ProductImage;
import com.hutech.lab02.service.CategoryService;
import com.hutech.lab02.service.ProductImageService;
import com.hutech.lab02.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.UUID;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;



@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;  // Đảm bảo bạn đã inject CategoryService
    @Autowired
    private ProductImageService productImageService;
    // Display a list of all products
    @GetMapping
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "/products/product-list";
    }

    // For adding a new product
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());

        model.addAttribute("categories", categoryService.getAllCategories());  // Load categories
        return "/products/add-product";
    }

    // Process the form for adding a new product
    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult result, @RequestParam("mainImage") MultipartFile mainImage,   @RequestParam("productimages") MultipartFile[] imageList) throws IOException {

        if (!mainImage.isEmpty()) {
            try {
                String imageName = saveImageStatic(mainImage);
                product.setMainImage("/img/" + imageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        productService.addProduct(product);
        for (MultipartFile image : imageList) {
            if (!image.isEmpty()) {
                try {
                    String imageUrl = saveImageStatic(image);
                    ProductImage productImage = new ProductImage();
                    productImage.setImagePath("/img/" +imageUrl);
                    productImage.setProduct(product);
                    product.getImages().add(productImage);
                    productImageService.addProductImage(productImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "redirect:/products";
    }
    private String saveImageStatic(MultipartFile image) throws IOException {
//        File saveFile = new ClassPathResource("static/img").getFile();
//        String fileName = UUID.randomUUID()+ "." + StringUtils.getFilenameExtension(image.getOriginalFilename());
//        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + fileName);
//        Files.copy(image.getInputStream(), path);
//        return fileName;
        Path dirImages = Paths.get("target/classes/static/img");
        if (!Files.exists(dirImages)) {
            Files.createDirectories(dirImages);
        }

        String newFileName = UUID.randomUUID()+ "." + StringUtils.getFilenameExtension(image.getOriginalFilename());

        Path pathFileUpload = dirImages.resolve(newFileName);
        Files.copy(image.getInputStream(), pathFileUpload,
                StandardCopyOption.REPLACE_EXISTING);
        return newFileName;
    }


    @GetMapping("/product-detail/{id}")
    public String getProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.findProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products/product-detail"; // Đường dẫn tới tệp product-detail.html trong thư mục templates/products
    }

    // For editing a product
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());  // Load categories
        return "products/update-product";
    }
    // Process the form for updating a product
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @Valid Product product, BindingResult result, @RequestParam("mainImage") MultipartFile mainImage) throws IOException {
        if (result.hasErrors()) {
            product.setId(id); // set id to keep it in the form in case of errors
            return "products/update-product";
        }

        productService.updateProduct(product);
        return "redirect:/products";
    }
    // Handle request to delete a product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));

        productService.deleteProductById(id);
        model.addAttribute("products", productService.getAllProducts());
        return "redirect:/products";
    }
}
