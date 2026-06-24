package com.sprint.SprintLite.Product.Service.impl;

import com.sprint.SprintLite.Product.Service.IProductService;
import com.sprint.SprintLite.dto.CreateProductRequest;
import com.sprint.SprintLite.entity.Product;
import com.sprint.SprintLite.entity.UserProductMapping;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.repository.ProductRepository;
import com.sprint.SprintLite.repository.UserProductMappingRepository;
import com.sprint.SprintLite.repository.UsersRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final UsersRepository userRepository;
    private final UserProductMappingRepository userProductMappingRepository;

    @Override
    //  ProductRepository productRepository;
    // 1. so this is used to retrieve name from context holder
    public Product createProduct(CreateProductRequest request) {
        String currentUsername = Objects.requireNonNull(Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).toString();
        System.out.println("Current User: " + currentUsername);

        // Get logged-in username from JWT
        String username = SecurityContextHolder.getContext().getAuthentication().getName();


        // 2. Fetch the complete User entity from DB to set the "owner" relationship
        Users currentuser = userRepository.findByUsername(currentUsername)
                .orElseThrow((() -> new UsernameNotFoundException("Login user not found in database")));

        System.out.println("Fetched Username : " + currentUsername);

        // 3.
        // Dto -> Entity
        Product product = new Product();
        product.setProductname(request.getProductName());
        product.setDescription(request.getDescription());
        product.setOwnerid(currentuser);
        product.setCreatedBy(currentUsername);
        product.setCreatedAt(Instant.now());

        Product savedProduct = productRepository.save(product);


        // Filling the user_Product_mapping table
        UserProductMapping mapping = new UserProductMapping();
        mapping.setUserid(currentuser);
        mapping.setProductid(savedProduct);

        // save mapping
        userProductMappingRepository.save(mapping);
        return savedProduct;


    }
}