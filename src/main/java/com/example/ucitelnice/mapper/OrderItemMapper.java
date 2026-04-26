package com.example.ucitelnice.mapper;

import com.example.ucitelnice.dto.orderItem.OrderItemResponse;
import com.example.ucitelnice.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.title", target = "productTitle")
    OrderItemResponse toResponse(OrderItem orderItem);
}
