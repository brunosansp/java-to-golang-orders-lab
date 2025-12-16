package services

import "github.com/brunosansp/java-to-golang-orders-lab/order-api/internal/models"

type OrderService interface {
	CreateOrder(customerID string, items []models.OrderItem) (*models.Order, error)
	GetOrderByID(id int64) (*models.Order, error)
}
