package com.sprint.SprintLite.Product.Service.impl;

import com.sprint.SprintLite.Product.Service.IProductService;
import com.sprint.SprintLite.dto.CreateProductRequest;
import com.sprint.SprintLite.entity.Product;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.repository.ProductRepository;
import com.sprint.SprintLite.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final UsersRepository userRepository;

    @Override
    //  ProductRepository productRepository;
    // 1. so this is used to retrieve name from context holder
    public Product createProduct(CreateProductRequest request) {
        String currentUsername = Objects.requireNonNull(Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).toString();
        System.out.println("Current User: " + currentUsername);

        // 2. Fetch the complete User entity from DB to set the "owner" relationship
        Users currentuser = userRepository.findByUsername(currentUsername)
                .orElseThrow((() -> new UsernameNotFoundException("Login user not found in database")));

        System.out.println("Fetched Username : " +  currentUsername);
        // 3. Map data from DTO to your real Product Database Entity
//        Product product = Product.builder()
//                .product_name(request.getProductName())
//                .product_description(request.getDescription())
//                .owner(currentuser)
//                .createdBy(currentUsername)
//                .createdAt(LocalDateTime.now())
//                .build();
        Product product = new Product();
        BeanUtils.copyProperties(request,product);
        return productRepository.save(product);

    }
}