package com.example.productapi.mapper;

import com.example.productapi.dto.request.CreateProductRequest;
import com.example.productapi.dto.request.UpdateProductRequest;
import com.example.productapi.dto.response.ProductResponse;
import com.example.productapi.entities.CategoryEntity;
import com.example.productapi.entities.ProductEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T20:00:13+0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.0.0.jar, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductResponse toResponse(ProductEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ProductResponse productResponse = new ProductResponse();

        productResponse.setCategoryName( entityCategoryName( entity ) );
        productResponse.setId( entity.getId() );
        productResponse.setName( entity.getName() );
        productResponse.setDescription( entity.getDescription() );
        productResponse.setPrice( entity.getPrice() );
        productResponse.setQuantity( entity.getQuantity() );
        productResponse.setActive( entity.getActive() );
        productResponse.setCreatedAt( entity.getCreatedAt() );

        productResponse.setTotalPrice( calculateTotalPrice(entity) );

        return productResponse;
    }

    @Override
    public List<ProductResponse> toResponseList(List<ProductEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ProductResponse> list = new ArrayList<ProductResponse>( entities.size() );
        for ( ProductEntity productEntity : entities ) {
            list.add( toResponse( productEntity ) );
        }

        return list;
    }

    @Override
    public ProductEntity toEntity(CreateProductRequest request) {
        if ( request == null ) {
            return null;
        }

        ProductEntity productEntity = new ProductEntity();

        productEntity.setName( request.getName() );
        productEntity.setDescription( request.getDescription() );
        productEntity.setPrice( request.getPrice() );
        productEntity.setQuantity( request.getQuantity() );
        productEntity.setActive( request.getActive() );

        return productEntity;
    }

    @Override
    public void updateEntityFromRequest(UpdateProductRequest request, ProductEntity entity) {
        if ( request == null ) {
            return;
        }

        entity.setName( request.getName() );
        entity.setDescription( request.getDescription() );
        entity.setPrice( request.getPrice() );
        entity.setQuantity( request.getQuantity() );
        entity.setActive( request.getActive() );
    }

    private String entityCategoryName(ProductEntity productEntity) {
        CategoryEntity category = productEntity.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getName();
    }
}
