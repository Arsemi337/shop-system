package pl.sii.shopsystem.order.dto;

import lombok.Builder;
import pl.sii.shopsystem.order.orderProduct.dto.OrderProductOutputDto;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record OrderOutputDto(
        String customerFirstname,
        String customerLastname,
        String customerEmail,
        List<OrderProductOutputDto> orderedProducts,
        BigDecimal totalCost) {
}
