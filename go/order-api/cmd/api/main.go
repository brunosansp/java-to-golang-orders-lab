package main

import (
	"fmt"
	"log"
	"net/http"

	"github.com/brunosansp/java-to-golang-orders-lab/order-api/internal/handlers"
	"github.com/brunosansp/java-to-golang-orders-lab/order-api/internal/services"
)

func main() {
	// Criar serviço
	orderService := services.NewOrderService()

	// Criar handler
	orderHandler := handlers.NewOrderHandler(orderService)

	// Registrar rotas
	http.HandleFunc("POST /orders", orderHandler.CreateOrder)
	http.HandleFunc("GET /orders/{id}", orderHandler.GetOrder)

	// Iniciar servidor
	port := ":8081"
	fmt.Printf("Starting server on %s\n", port)

	if err := http.ListenAndServe(port, nil); err != nil {
		log.Fatalf("server error: %v", err)
	}
}
