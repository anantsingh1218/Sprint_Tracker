package com.sprint.SprintLite.Product.Controller;

import com.sprint.SprintLite.Product.Service.impl.ProductService;
import com.sprint.SprintLite.dto.CreateProductRequest;
import com.sprint.SprintLite.dto.GetAllResponseDto;
import com.sprint.SprintLite.entity.Product;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductRepository productRepository;

    private final ProductService productService;
    @PostMapping("/add")
//    @PreAuthorize("hasAuthority('PM')")
    public ResponseEntity<?> addProduct(@RequestBody CreateProductRequest request) {
        Product savedProduct = productService.createProduct(request);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<GetAllResponseDto>> getAllProducts(){
        List<Product> productList = productRepository.findAll();
        if (productList.isEmpty()){
            throw new EntityNotFoundException("No Products registered");
        }
        List<GetAllResponseDto> getAllResponseDtoList = new ArrayList<GetAllResponseDto>();
        productList.forEach(product -> {
            GetAllResponseDto getAllResponseDto = new GetAllResponseDto(product.getId(), product.getProductname());
            getAllResponseDtoList.add(getAllResponseDto);
        });
        return ResponseEntity.ok(getAllResponseDtoList);
    }
}
