package pl.artur.shopsystem.order;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import pl.artur.shopsystem.customer.model.Customer;
import pl.artur.shopsystem.customer.repository.CustomerRepository;
import pl.artur.shopsystem.order.model.Order;
import pl.artur.shopsystem.order.repository.OrderRepository;
import pl.artur.shopsystem.product.model.Product;
import pl.artur.shopsystem.product.repository.ProductRepository;
import pl.artur.shopsystem.order.orderProduct.model.OrderProduct;
import product.model.Genre;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderIntegrationTest {

    @LocalServerPort
    int port;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Making order happy path")
    void makingOrderHappyPath() {
        Assertions.assertThat(orderRepository.findAll()).isEmpty();
        Customer customer = addCustomerToDatabase();
        Product product = addProductToDatabase();
        int quantity = 10;

        given().accept(JSON).body(String.format("""
                                {
                                    "customerId": "%s",
                                    "orderProducts": [
                                        {
                                            "productId": "%s",
                                            "quantity": 10
                                        }
                                    ]
                                }
                                """, customer.getId().toString(),
                        product.getId().toString()))
                .contentType(JSON)
                .when().post("/api/v1/orders")
                .then().statusCode(HttpStatus.SC_OK)
                .body("customerFirstname", equalTo(customer.getFirstname()))
                .body("customerLastname", equalTo(customer.getLastname()))
                .body("customerEmail", equalTo(customer.getEmail()))
                .body("orderedProducts.size()", equalTo(1))
                .body("orderedProducts[0].name", equalTo(product.getName()))
                .body("orderedProducts[0].type", equalTo(product.getType().toString()))
                .body("orderedProducts[0].manufacturer", equalTo(product.getManufacturer()))
                .body("orderedProducts[0].price", equalTo(product.getPrice().floatValue()))
                .body("orderedProducts[0].quantity", equalTo(quantity))
                .body("totalCost", equalTo(
                        product.getPrice().multiply(new BigDecimal(quantity)).floatValue()));

        Assertions.assertThat(orderRepository.findAll()).hasSize(1);
        List<Order> orders = orderRepository.findAll();
        List<OrderProduct> orderProducts = new ArrayList<>(orders.get(0).getOrderProducts());
        assertThat(orders).isNotEmpty();
        org.junit.jupiter.api.Assertions.assertEquals(orders.get(0).getCustomer().getFirstname(), customer.getFirstname());
        org.junit.jupiter.api.Assertions.assertEquals(orders.get(0).getCustomer().getLastname(), customer.getLastname());
        org.junit.jupiter.api.Assertions.assertEquals(orders.get(0).getCustomer().getEmail(), customer.getEmail());
        assertEquals(orderProducts.size(), 1);
        assertEquals(orderProducts.get(0).getProduct(), product);
        assertEquals(orderProducts.get(0).getQuantity(), quantity);
        assertEquals(orders.get(0).getTotalCost(), product.getPrice().multiply(new BigDecimal(quantity)));
    }

    private Customer addCustomerToDatabase() {
        Customer customer = Customer.builder()
                .firstname("John")
                .lastname("Doe")
                .email("jdoe@email.pl")
                .build();
        return customerRepository.save(customer);
    }

    private Product addProductToDatabase() {
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("Potop")
                .type(Genre.HISTORICAL_FICTION)
                .manufacturer("Greg")
                .price(new BigDecimal("9.99"))
                .build();
        return productRepository.save(product);
    }
}