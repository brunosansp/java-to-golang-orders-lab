package br.com.brunosansp.order.service.impl;

import br.com.brunosansp.order.dto.OrderDTO;
import br.com.brunosansp.order.dto.OrderItemDTO;
import br.com.brunosansp.order.model.Order;
import br.com.brunosansp.order.model.OrderItem;
import br.com.brunosansp.order.service.contract.IOrderService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
    
    /**
     * Armazenamento em memória será substituído posteriormente
     */
    private final Map<Long, Order> orderStore = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();
    
    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        if (orderDTO.getCustomerId() == null || orderDTO.getCustomerId().isBlank())
            throw new IllegalArgumentException("customerId é obrigatório");
        if (orderDTO.getItems() == null || orderDTO.getItems().isEmpty())
            throw new IllegalArgumentException("ordem deve ter pelo menos um item");
        Long id = idCounter.getAndIncrement();
        List<OrderItem> items = orderDTO.getItems()
            .stream()
            .map(itemDto -> new OrderItem(
                    null,
                    itemDto.getProductId(),
                    itemDto.getQuantity()
                )
            ).collect(Collectors.toList());
        Order order = new Order(id, orderDTO.getCustomerId(), items);
        orderStore.put(id, order);
        return mapToDTO(order);
    }
    
    @Override
    public OrderDTO getOrder(Long id) {
        Order order = orderStore.get(id);
        if (order == null)
            throw new IllegalArgumentException("Ordem com id: " + id + " não encontrada");
        return mapToDTO(order);
    }
    
    private OrderDTO mapToDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems()
            .stream()
            .map(
                item -> new OrderItemDTO(
                    item.getId(),
                    item.getProductId(),
                    item.getQuantity()
                )
            ).collect(Collectors.toList());
        return new OrderDTO(
            order.getId(),
            order.getCustomerId(),
            itemDTOs,
            order.getCreatedAt()
        );
    }
}
