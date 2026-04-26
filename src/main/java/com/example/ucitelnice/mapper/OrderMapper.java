package com.example.ucitelnice.mapper;

import com.example.ucitelnice.dto.order.OrderResponse;
import com.example.ucitelnice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toResponse(Order order);
}
