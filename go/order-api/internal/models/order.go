package models

import "time"

type Order struct {
	ID		 	int64
	CustomerID 	string
	Items    	[]OrderItem
	CreatedAt 	time.Time
}

type OrderItem struct {
	ID			int64
	ProductID 	string
	Quantity  	int
}

func NewOrder(id int64, customerID string, items []OrderItem) *Order {
	return &Order{
		ID:		 id,
		CustomerID: customerID,
		Items:    items,
		CreatedAt: time.Now(),
	}
}
