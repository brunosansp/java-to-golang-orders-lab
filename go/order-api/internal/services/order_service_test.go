package services

import (
	"testing"

	"github.com/brunosansp/java-to-golang-orders-lab/order-api/internal/models"
)

func TestCreateOrderSuccess(t *testing.T) {
	service := NewOrderService()
	items := []models.OrderItem{
		{ID: 0, ProductID: "PROD-001", Quantity: 2},
	}

	order, err := service.CreateOrder("CUST-123", items)

	if err != nil {
		t.Fatalf("expected no error, got %v", err)
	}

	if order.ID != 1 {
		t.Errorf("expected ID 1, got %d", order.ID)
	}

	if order.CustomerID != "CUST-123" {
		t.Errorf("expected customerID CUST-123, got %s", order.CustomerID)
	}

	if len(order.Items) != 1 {
		t.Errorf("expected 1 item, got %d", len(order.Items))
	}
}

func TestCreateOrderMissingCustomerID(t *testing.T) {
	service := NewOrderService()
	items := []models.OrderItem{
		{ID: 0, ProductID: "PROD-001", Quantity: 2},
	}

	_, err := service.CreateOrder("", items)

	if err == nil {
		t.Fatal("expected error for missing customerID, got nil")
	}
}

func TestCreateOrderNoItems(t *testing.T) {
	service := NewOrderService()

	_, err := service.CreateOrder("CUST-123", []models.OrderItem{})

	if err == nil {
		t.Fatal("expected error for no items, got nil")
	}
}

func TestGetOrderSuccess(t *testing.T) {
	service := NewOrderService()
	items := []models.OrderItem{
		{ID: 0, ProductID: "PROD-001", Quantity: 2},
	}

	// Criar pedido primeiro
	created, _ := service.CreateOrder("CUST-123", items)

	// Recuperar
	retrieved, err := service.GetOrderByID(created.ID)

	if err != nil {
		t.Fatalf("expected no error, got %v", err)
	}

	if retrieved.ID != created.ID {
		t.Errorf("expected ID %d, got %d", created.ID, retrieved.ID)
	}
}

func TestGetOrderNotFound(t *testing.T) {
	service := NewOrderService()

	_, err := service.GetOrderByID(999)

	if err == nil {
		t.Fatal("expected error for order not found, got nil")
	}
}
