package services

import (
	"fmt"
	"sync"

	"github.com/brunosansp/java-to-golang-orders-lab/order-api/internal/models"
)

type OrderServiceImpl struct {
	store   map[int64]*models.Order
	mu      sync.RWMutex
	idCount int64
}

// NewOrderService cria uma nova instância do serviço
func NewOrderService() OrderService {
	return &OrderServiceImpl{
		store:   make(map[int64]*models.Order),
		idCount: 0,
	}
}

// CreateOrder cria uma nova ordem
func (s *OrderServiceImpl) CreateOrder(customerID string, items []models.OrderItem) (*models.Order, error) {
	// Validar customerID
	if customerID == "" {
		return nil, fmt.Errorf("customerId is required")
	}

	// Validar items
	if len(items) == 0 {
		return nil, fmt.Errorf("order must have at least one item")
	}

	// Lock para evitar race condition
	s.mu.Lock()
	defer s.mu.Unlock()

	s.idCount++
	order := models.NewOrder(s.idCount, customerID, items)

	s.store[order.ID] = order

	return order, nil
}

// GetOrder recupera uma ordem pelo ID
func (s *OrderServiceImpl) GetOrderByID(id int64) (*models.Order, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	order, exists := s.store[id]
	if !exists {
		return nil, fmt.Errorf("order with id %d not found", id)
	}

	return order, nil
}
