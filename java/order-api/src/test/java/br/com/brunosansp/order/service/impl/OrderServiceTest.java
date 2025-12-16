package br.com.brunosansp.order.service.impl;


import br.com.brunosansp.order.dto.OrderDTO;
import br.com.brunosansp.order.dto.OrderItemDTO;
import br.com.brunosansp.order.service.contract.IOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderServiceTest {
    
    private IOrderService orderService;
    
    @BeforeEach
    void setUp() {
        orderService = new OrderService();
    }
    
    @Test
    void shouldCreateOrderSuccessfully() {
        // Arrange
        OrderItemDTO item = new OrderItemDTO(null, "PROD-001", 2);
        OrderDTO orderDTO = new OrderDTO(null, "CUST-123", List.of(item), null);
        
        // Act
        OrderDTO created = orderService.createOrder(orderDTO);
        
        // Assert
        assertNotNull(created.getId());
        assertEquals("CUST-123", created.getCustomerId());
        assertEquals(1, created.getItems().size());
        assertEquals("PROD-001", created.getItems().get(0).getProductId());
    }
    
    @Test
    void shouldThrowExceptionWhenCustomerIdIsNull() {
        // Arrange
        OrderItemDTO item = new OrderItemDTO(null, "PROD-001", 2);
        OrderDTO orderDTO = new OrderDTO(null, null, List.of(item), null);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(orderDTO));
    }
    
    @Test
    void shouldThrowExceptionWhenOrderHasNoItems() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO(null, "CUST-123", List.of(), null);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(orderDTO));
    }
    
    @Test
    void shouldRetrieveOrderById() {
        // Arrange
        OrderItemDTO item = new OrderItemDTO(null, "PROD-001", 2);
        OrderDTO orderDTO = new OrderDTO(null, "CUST-123", List.of(item), null);
        OrderDTO created = orderService.createOrder(orderDTO);
        
        // Act
        OrderDTO retrieved = orderService.getOrder(created.getId());
        
        // Assert
        assertNotNull(retrieved);
        assertEquals(created.getId(), retrieved.getId());
        assertEquals("CUST-123", retrieved.getCustomerId());
    }
    
    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.getOrder(999L));
    }
}