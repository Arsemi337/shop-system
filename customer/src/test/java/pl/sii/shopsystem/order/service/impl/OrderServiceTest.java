package pl.sii.shopsystem.order.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sii.shopsystem.customer.model.Customer;
import pl.sii.shopsystem.customer.repository.CustomerRepository;
import pl.sii.shopsystem.order.dto.OrderInputDto;
import pl.sii.shopsystem.order.dto.OrderOutputDto;
import pl.sii.shopsystem.order.model.Order;
import pl.sii.shopsystem.order.orderProduct.dto.OrderProductInputDto;
import pl.sii.shopsystem.order.orderProduct.dto.OrderProductOutputDto;
import pl.sii.shopsystem.order.orderProduct.model.OrderProduct;
import pl.sii.shopsystem.order.orderProduct.repository.OrderProductRepository;
import pl.sii.shopsystem.order.repository.OrderRepository;
import pl.sii.shopsystem.order.service.OrderValidator;
import pl.sii.shopsystem.product.model.Product;
import pl.sii.shopsystem.product.repository.ProductRepository;
import supplier.TimeSupplier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    OrderServiceImpl underTest;
    @Mock
    TimeSupplier timeSupplier;
    @Mock
    OrderValidator validator;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderProductRepository orderProductRepository;

    @Test
    @DisplayName("when valid OrderInputDto is passed, an OrderOutputDto object should be returned")
    void makeOrder_should_makeOrder_when_validOrderInputDtoIsPassed() {
        // given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2010, 1, 1, 1, 10, 30));
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Customer customer = Customer.builder()
                .id(customerId)
                .firstname("John")
                .lastname("Doe")
                .email("jdoe@sii.pl")
                .build();
        Product product = Product.builder()
                .id(productId)
                .creationTime(timeSupplier.get())
                .type("Smartphone")
                .title("Pixel 6")
                .publishingHouse("Google")
                .price(new BigDecimal("479.99"))
                .build();
        List<OrderProductInputDto> orderProducts = new ArrayList<>();
        orderProducts.add(OrderProductInputDto.builder()
                .productId(productId.toString())
                .quantity("10")
                .build());
        OrderInputDto input = OrderInputDto.builder()
                .customerId(customerId.toString())
                .orderProducts(orderProducts)
                .build();
        List<OrderProductOutputDto> orderProductOutputDtoList = new ArrayList<>();
        orderProductOutputDtoList.add(OrderProductOutputDto.builder()
                .title(product.getTitle())
                .type(product.getType())
                .publishingHouse(product.getPublishingHouse())
                .price(product.getPrice())
                .quantity(10)
                .build());
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(answer -> answer.getArgument(0));
        when(orderProductRepository.save(any(OrderProduct.class))).thenAnswer(answer -> answer.getArgument(0));

        // when
        OrderOutputDto output = underTest.makeOrder(input);

        // then
        assertEquals(output.customerFirstname(), customer.getFirstname());
        assertEquals(output.customerLastname(), customer.getLastname());
        assertEquals(output.customerEmail(), customer.getEmail());
        assertEquals(output.orderedProducts(), orderProductOutputDtoList);
        assertEquals(output.totalCost(), product.getPrice().multiply(new BigDecimal(10)));
        verify(customerRepository).findById(customerId);
        verify(productRepository).findById(productId);
        verify(orderRepository).save(any(Order.class));
        verify(orderProductRepository).save(any(OrderProduct.class));
    }
}
