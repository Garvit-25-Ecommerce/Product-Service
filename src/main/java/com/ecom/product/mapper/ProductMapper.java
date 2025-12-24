package com.ecom.product.mapper;

import com.ecom.product.dto.ProductDto;
import com.ecom.product.dto.ProductRequest;
import com.ecom.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);
    Product toEntity(ProductDto productDto);
    @Mapping(target = "id", ignore = true)
    @Mapping(target="reviews",ignore = true)
    @Mapping(target="categoryId", source = "category")
    Product toEntityFromRequest(ProductRequest productRequest);
    List<ProductDto> toDtoList(List<Product> products);
    default Page<ProductDto> toDtoPage(Page<Product> products) {
        return products.map(this::toDto);
    }
}
