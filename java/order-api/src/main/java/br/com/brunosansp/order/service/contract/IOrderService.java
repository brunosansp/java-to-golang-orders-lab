package br.com.brunosansp.order.service.contract;

import br.com.brunosansp.order.dto.OrderDTO;

public interface IOrderService {
    OrderDTO createOrder(OrderDTO orderDTO);
    
    OrderDTO getOrder(Long id);
}
