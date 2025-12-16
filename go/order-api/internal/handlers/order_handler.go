package handlers

import (
	"encoding/json"
	"log"
	"net/http"
	"strconv"
	"strings"

	"github.com/brunosansp/java-to-golang-orders-lab/order-api/internal/models"
	"github.com/brunosansp/java-to-golang-orders-lab/order-api/internal/services"
)

type OrderHandler struct {
	service services.OrderService
}

// NewOrderHandler cria um novo handler de pedidos
func NewOrderHandler(service services.OrderService) *OrderHandler {
	return &OrderHandler{service: service}
}

// CreateOrder manipula POST /orders
func (h *OrderHandler) CreateOrder(w http.ResponseWriter, r *http.Request) {
	var req struct {
		CustomerID string                 `json:"customerId"`
		Items      []models.OrderItem `json:"items"`
	}

	// Decodificar JSON do corpo da requisição
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	// Chamar serviço
	order, err := h.service.CreateOrder(req.CustomerID, req.Items)
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	// Responder com 201 Created
	respondJSON(w, http.StatusCreated, order)
}

// GetOrder manipula GET /orders/{id}
func (h *OrderHandler) GetOrder(w http.ResponseWriter, r *http.Request) {
	// Extrair ID da URL
	parts := strings.Split(r.URL.Path, "/")
	if len(parts) < 3 {
		respondError(w, http.StatusBadRequest, "invalid URL")
		return
	}

	idStr := parts[len(parts)-1]
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		respondError(w, http.StatusBadRequest, "invalid order id")
		return
	}

	// Chamar serviço
	order, err := h.service.GetOrderByID(id)
	if err != nil {
		respondError(w, http.StatusNotFound, err.Error())
		return
	}

	// Responder com 200 OK
	respondJSON(w, http.StatusOK, order)
}

// respondJSON escreve uma resposta JSON
func respondJSON(w http.ResponseWriter, statusCode int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		log.Printf("error encoding response: %v", err)
	}
}

// respondError escreve uma resposta de erro em JSON
func respondError(w http.ResponseWriter, statusCode int, message string) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	err := json.NewEncoder(w).Encode(map[string]string{
		"error": message,
	})
	if err != nil {
		log.Printf("error encoding error response: %v", err)
	}
}
