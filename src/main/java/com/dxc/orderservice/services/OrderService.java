package com.dxc.orderservice.services;

import com.dxc.orderservice.models.*;
import com.dxc.orderservice.repositories.CustomerRepository;
import com.dxc.orderservice.repositories.ProductRepository;
import com.dxc.orderservice.requests.OrderItemRequest;
import com.dxc.orderservice.requests.OrderRequest;
import com.dxc.orderservice.responses.OrderItemResponse;
import com.dxc.orderservice.responses.OrderResponse;
import com.dxc.orderservice.repositories.OrderItemRepository;
import com.dxc.orderservice.repositories.OrderRepository;
import com.dxc.orderservice.util.exceptions.EntityNotFoundException;
import com.dxc.orderservice.util.exceptions.ExceedsTotalException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;


/**
 * Service class to manage Orders and related operations.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    private final CustomerRepository customerRepository;

    /**
     * Constructor to initialize repositories.
     *
     * @param orderRepository Repository for Order operations.
     * @param orderItemRepository Repository for OrderItem operations.
     * @param productRepository Repository for Product operations.
     * @param customerRepository Repository for Customer operations.
     */
    public OrderService(final OrderRepository orderRepository,
                        final OrderItemRepository orderItemRepository,
                        final ProductRepository productRepository,
                        final CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }


    /**
     * Fetches an order by its ID.
     *
     * @param orderId ID of the order to fetch.
     * @return Response object containing order details.
     * @throws EntityNotFoundException If the order is not found.
     */
    public OrderResponse getOrderById(final int orderId) {

        Optional<Order> result = orderRepository.findById(orderId);

        if (result.isPresent()) {
            OrderResponse response = new OrderResponse();
            Order data = result.get();
            response.setId(data.getOrderId());

            int customerId = data.getCustomerId();
            // Get the customer name
            String customerName = customerRepository.getCustomerByCustomerId(customerId).getName();

            response.setCustomerName(customerName);
            response.setStatus(data.getStatus());
            response.setTotalAmount(data.getTotalAmount());
            response.setCreatedAt(data.getCreatedAt());

            Iterable<OrderItem> items =
                    orderItemRepository.findByIdOrderId(orderId);
            List<OrderItemResponse> orderItemResList = new ArrayList<>();

            for (OrderItem item : items) {
                OrderItemResponse orderItemRes = new OrderItemResponse();
                orderItemRes.setProductName(productRepository.getProductByProductId(item.getId().getProductId()).getName());
                orderItemRes.setQuantity(item.getQuantity());
                orderItemResList.add(orderItemRes);
            }

            response.setItems(orderItemResList);

            return response;
        } else {
            throw new EntityNotFoundException(
                    "The Order with ID " + orderId + " does not exists");
        }

    }

    /**
     * Fetches all orders associated with a given customer ID.
     *
     * @param customerId ID of the customer.
     * @return List of response objects
     * containing order details for the given customer.
     * @throws EntityNotFoundException If no
     * orders are found for the given customer.
     */
    public List<OrderResponse> getOrdersByCustomerId(final int customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new EntityNotFoundException("The customer with ID: "
                    + customerId + " does not exist ");
        }

        // Retrieve a list of orders for the given customer ID from the database
        List<Order> orders = orderRepository.findOrdersByCustomerId(customerId);
        Customer customer = customerRepository.getCustomerByCustomerId(customerId);

        // Create a list to store the order responses
        List<OrderResponse> orderResponses = new ArrayList<>();

        // Iterate through the retrieved orders and create OrderResponse objects
        for (Order order : orders) {
            OrderResponse response = new OrderResponse();
            response.setId(order.getOrderId());
            response.setCustomerName(customer.getName());
            response.setStatus(order.getStatus());
            response.setTotalAmount(order.getTotalAmount());
            response.setCreatedAt(order.getCreatedAt());

            // Fetch order items associated with the order
            // and convert them into OrderItemResponse objects
            List<OrderItem> orderItems =
                    orderItemRepository.findByIdOrderId(order.getOrderId());
            List<OrderItemResponse> orderItemResponses = new ArrayList<>();//name

            for (OrderItem orderItem : orderItems) {
                OrderItemResponse orderItemResponse = new OrderItemResponse();
                orderItemResponse.setProductName((
                        productRepository.getProductByProductId(orderItem.getId().getProductId()).getName()));
                orderItemResponse.setQuantity(
                        orderItem.getQuantity());
                orderItemResponses.add(
                        orderItemResponse);
            }

            response.setItems(orderItemResponses);

            // Add the OrderResponse to the list
            orderResponses.add(response);
        }

        return orderResponses;
    }

    //from here till the end for createOrder method

    public OrderResponse createOrder(final OrderRequest orderRequest) {
        validateOrderRequest(orderRequest);

        Order savedOrder = saveOrder(orderRequest);
        List<OrderItem> savedOrderItems = saveOrderItems(orderRequest, savedOrder);

        return buildOrderResponse(savedOrder, savedOrderItems);
    }

    private void validateOrderRequest(OrderRequest orderRequest) {
        if (!customerRepository.existsById(orderRequest.getCustomerId())) {
            throw new EntityNotFoundException("The customer with ID: "
                    + orderRequest.getCustomerId() + " does not exist");
        }

        Set<Integer> productsIdsSet = new HashSet<>(productRepository.getAllProductsIds());
        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            if (!productsIdsSet.contains(itemRequest.getProductId())) {
                throw new EntityNotFoundException("The product with ID: "
                        + itemRequest.getProductId() + " does not exist");
            }
        }
    }

    private Order saveOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setCustomerId(orderRequest.getCustomerId());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    private List<OrderItem> saveOrderItems(OrderRequest orderRequest, Order savedOrder) {
        Map<Integer, OrderItemRequest> map = mergeOrderItems(orderRequest);

        // Fetch prices for all product in one go
        List<Product> allProducts = productRepository.findAll();
        Map<Integer, Product> requestedProducts = new HashMap<>();
        Map<Integer, Integer> productQuantities = new HashMap<>();  // This will store the quantities for each product


        for (Product product : allProducts) {
            if (map.containsKey(product.getProductId())) {
                OrderItemRequest orderItemRequest = map.get(product.getProductId());
                requestedProducts.put(orderItemRequest.getProductId(), product);
                productQuantities.put(orderItemRequest.getProductId(), orderItemRequest.getQuantity());
            }
        }

        double totalAmount = 0.0;
        final double max = 99999999.99;

        List<OrderItem> orderItemsToSave = new ArrayList<>();
        for (Product product : requestedProducts.values()) {
            totalAmount += product.getPrice() * productQuantities.get(product.getProductId());

            if (totalAmount > max) {
                throw new ExceedsTotalException("Total amount exceeds the 99,999,999.99");
            }

            OrderItem orderItem = new OrderItem();
            OrderItemCompositeKey orderItemCompositeKey = new OrderItemCompositeKey();
            orderItemCompositeKey.setOrderId(savedOrder.getOrderId());
            orderItemCompositeKey.setProductId(product.getProductId());

            orderItem.setId(orderItemCompositeKey);
            orderItem.setQuantity(product.getProductId());
            orderItemsToSave.add(orderItem);
        }

        // Save all order items in one go
        List<OrderItem> savedItems = new ArrayList<>();
        orderItemRepository.saveAll(orderItemsToSave).forEach(savedItems::add);

        savedOrder.setTotalAmount(totalAmount);
        return savedItems;
    }

    private Map<Integer, OrderItemRequest> mergeOrderItems(OrderRequest orderRequest) {
        Map<Integer, OrderItemRequest> map = new HashMap<>();
        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            map.merge(itemRequest.getProductId(), itemRequest, (existing, current) -> {
                existing.setQuantity(existing.getQuantity() + current.getQuantity());
                return existing;
            });
        }
        return map;
    }

    private OrderResponse buildOrderResponse(Order savedOrder, List<OrderItem> savedOrderItems) {
        OrderResponse response = new OrderResponse();
        response.setId(savedOrder.getOrderId());

        // Fetch customer name
        Customer customer = customerRepository.getCustomerByCustomerId(savedOrder.getCustomerId());
        response.setCustomerName(customer.getName());

        response.setStatus(savedOrder.getStatus());
        response.setCreatedAt(savedOrder.getCreatedAt());
        response.setTotalAmount(savedOrder.getTotalAmount());

        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        for (OrderItem savedOrderItem : savedOrderItems) {
            OrderItemResponse itemResponse = new OrderItemResponse();

            // Fetch product name
            itemResponse.setProductName(productRepository.getProductByProductId(savedOrderItem.getId().getProductId()).getName());
            itemResponse.setQuantity(savedOrderItem.getQuantity());
            orderItemResponses.add(itemResponse);

        }

        response.setItems(orderItemResponses);

        return response;
    }

}
