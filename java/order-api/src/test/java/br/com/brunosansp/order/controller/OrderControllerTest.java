package br.com.brunosansp.order.controller;

import br.com.brunosansp.order.dto.OrderDTO;
import br.com.brunosansp.order.dto.OrderItemDTO;
import br.com.brunosansp.order.service.contract.IOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private IOrderService orderService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void shouldCreateOrderAndReturnCreatedStatus() throws Exception {
        // Arrange
        OrderItemDTO item = new OrderItemDTO(null, "PROD-001", 2);
        OrderDTO requestDTO = new OrderDTO(null, "CUST-123", List.of(item), null);
        OrderDTO responseDTO = new OrderDTO(1L, "CUST-123", List.of(new OrderItemDTO(null, "PROD-001", 2)), LocalDateTime.now());
        
        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(responseDTO);
        
        // Act & Assert
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.customerId").value("CUST-123"));
    }
    
    @Test
    void shouldRetrieveOrderById() throws Exception {
        // Arrange
        OrderItemDTO item = new OrderItemDTO(null, "PROD-001", 2);
        OrderDTO responseDTO = new OrderDTO(1L, "CUST-123", List.of(item), LocalDateTime.now());
        
        when(orderService.getOrder(1L)).thenReturn(responseDTO);
        
        // Act & Assert
        mockMvc.perform(get("/orders/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.customerId").value("CUST-123"));
    }
    
    @Test
    void shouldReturnBadRequestWhenOrderNotFound() throws Exception {
        // Arrange
        when(orderService.getOrder(999L)).thenThrow(new IllegalArgumentException("Ordem não encontrada"));
        
        // Act & Assert
        mockMvc.perform(get("/orders/999")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}
