package com.sprint.SprintLite.Product.Service;

import com.sprint.SprintLite.dto.CreateProductRequest;
import com.sprint.SprintLite.entity.Product;

public interface IProductService {
    public Product createProduct(CreateProductRequest request);
}
