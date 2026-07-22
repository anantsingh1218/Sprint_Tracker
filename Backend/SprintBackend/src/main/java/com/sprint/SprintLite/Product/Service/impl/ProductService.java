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

    public Product createProduct(CreateProductRequest request) {
        String currentUsername = Objects.requireNonNull(Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).toString();
        System.out.println("Current User: " + currentUsername);


        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Users currentuser = userRepository.findByUsername(currentUsername)
                .orElseThrow((() -> new UsernameNotFoundException("Login user not found in database")));

        System.out.println("Fetched Username : " + currentUsername);

        Product product = new Product();
        product.setProductname(request.getProductName());
        product.setDescription(request.getDescription());
        product.setOwnerid(currentuser);
        product.setCreatedBy(currentUsername);
        product.setCreatedAt(Instant.now());

        Product savedProduct = productRepository.save(product);


        UserProductMapping mapping = new UserProductMapping();
        mapping.setUserid(currentuser);
        mapping.setProductid(savedProduct);

        userProductMappingRepository.save(mapping);
        return savedProduct;


    }
}