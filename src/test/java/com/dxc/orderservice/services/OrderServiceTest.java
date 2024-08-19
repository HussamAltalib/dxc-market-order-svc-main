package com.dxc.orderservice.services;

import com.dxc.orderservice.models.*;
import com.dxc.orderservice.repositories.CustomerRepository;
import com.dxc.orderservice.repositories.OrderItemRepository;
import com.dxc.orderservice.repositories.OrderRepository;
import com.dxc.orderservice.repositories.ProductRepository;
import com.dxc.orderservice.requests.OrderItemRequest;
import com.dxc.orderservice.requests.OrderRequest;
import com.dxc.orderservice.responses.OrderItemResponse;
import com.dxc.orderservice.responses.OrderResponse;
import com.dxc.orderservice.util.exceptions.EntityNotFoundException;
import com.dxc.orderservice.util.exceptions.ExceedsTotalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;


    @InjectMocks
    private OrderService orderService;

    @BeforeEach //Setup method to initialize the mock annotations before each test.
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testGetOrderById_Success() {
        // Given
        int orderId = 1;
        LocalDateTime now = LocalDateTime.now();
        Order mockOrder = new Order(orderId, 1, Order.OrderStatus.PENDING, 456.56, now);

        Customer mockCustomer = new Customer();
        mockCustomer.setCustomerId(1);
        mockCustomer.setName("mockCustomerName");
        when(customerRepository.getCustomerByCustomerId(1)).thenReturn(mockCustomer);
        // Creating mock order items and products
        List<OrderItem> mockOrderItems = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Product mockProduct = new Product();
            mockProduct.setProductId(i);
            mockProduct.setName("Product-" + i);

            OrderItem mockOrderItem = new OrderItem();
            OrderItemCompositeKey mockOrderItemId = new OrderItemCompositeKey();
            mockOrderItemId.setOrderId(orderId);
            mockOrderItemId.setProductId(i);
            mockOrderItem.setId(mockOrderItemId);
            mockOrderItem.setQuantity(i + 1);  // Just to vary quantities
            mockOrderItems.add(mockOrderItem);

            when(productRepository.getProductByProductId(i)).thenReturn(mockProduct);
        }

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(orderItemRepository.findByIdOrderId(orderId)).thenReturn(mockOrderItems);

        // When
        OrderResponse result = orderService.getOrderById(orderId);

        // Then
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(Order.OrderStatus.PENDING, result.getStatus());
        assertEquals(456.56, result.getTotalAmount());
        assertEquals(now, result.getCreatedAt());

        assertEquals(3, result.getItems().size());
        for (int i = 0; i < 3; i++) {
            assertEquals("Product-" + i, result.getItems().get(i).getProductName());
            assertEquals(i + 1, result.getItems().get(i).getQuantity());
        }
    }

    @Test
    void testGetOrderById_OrderNotFound() {
        // Given
        int orderId = 1;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            orderService.getOrderById(orderId);
        });

        // Then
        assertEquals("The Order with ID " + orderId + " does not exists", exception.getMessage());
    }

    @Test
    void testValidGetOrdersByCustomerId() {
        // Given
        int customerId = 1;

        // Mock the customer
        Customer mockCustomer = new Customer();
        mockCustomer.setCustomerId(customerId);
        mockCustomer.setName("Customer-1");

        // Add mock for getCustomerByCustomerId
        when(customerRepository.getCustomerByCustomerId(customerId)).thenReturn(mockCustomer);

        List<Order> mockOrders = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            mockOrders.add(new Order(i, customerId, Order.OrderStatus.PENDING, 456.56 + i, LocalDateTime.now()));
        }

        when(orderRepository.findOrdersByCustomerId(customerId)).thenReturn(mockOrders);
        when(customerRepository.existsById(customerId)).thenReturn(true);

        for (int i = 1; i <= 10; i++) {
            List<OrderItem> mockOrderItems = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                Product mockProduct = new Product();
                mockProduct.setProductId(j);
                mockProduct.setName("Product-" + j);

                OrderItem mockOrderItem = new OrderItem();
                OrderItemCompositeKey mockOrderItemId = new OrderItemCompositeKey();
                mockOrderItemId.setOrderId(i);
                mockOrderItemId.setProductId(j);
                mockOrderItem.setId(mockOrderItemId);
                mockOrderItem.setQuantity(3);
                mockOrderItems.add(mockOrderItem);

                when(productRepository.getProductByProductId(j)).thenReturn(mockProduct);
            }
            when(orderItemRepository.findByIdOrderId(i)).thenReturn(mockOrderItems);
        }

        // When
        List<OrderResponse> results = orderService.getOrdersByCustomerId(customerId);

        // Then
        assertNotNull(results);
        assertEquals(10, results.size());

        // Validate order details and their corresponding order items.
        for (int i = 0; i < 10; i++) {
            OrderResponse orderResponse = results.get(i);
            assertEquals(i + 1, orderResponse.getId());
            assertEquals("Customer-1", orderResponse.getCustomerName());  // Added this line to validate customer name
            assertEquals(Order.OrderStatus.PENDING, orderResponse.getStatus());
            assertEquals(456.56 + i + 1, orderResponse.getTotalAmount());

            assertNotNull(orderResponse.getItems());
            assertEquals(10, orderResponse.getItems().size());

            for (int j = 0; j < 10; j++) {
                OrderItemResponse orderItemResponse = orderResponse.getItems().get(j);
                assertEquals("Product-" + j, orderItemResponse.getProductName());
                assertEquals(3, orderItemResponse.getQuantity());
            }
        }
    }


    @Test
    void testInvalidGetOrdersByCustomerId() {
        // Given
        int customerId = 99;
        when(orderRepository.findOrdersByCustomerId(customerId)).thenReturn(new ArrayList<>());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            orderService.getOrdersByCustomerId(customerId);
        });
    }



    @Test
    void testCreateOrder_invalidCustomer() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId(99); // Invalid customer
        request.setItems(Collections.emptyList());


        // mock to return false for customer existence check
        when(customerRepository.existsById(99)).thenReturn(false);

        // When & Then
        assertThrows(Exception.class, () -> {
            orderService.createOrder(request);
        });
    }

    @Test
    void testCreateOrder_invalidProduct() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId(1);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(99); // Invalid product
        itemRequest.setQuantity(1);
        request.setItems(Arrays.asList(itemRequest));


        // mock to return true for customer existence and false for product existence
        when(customerRepository.existsById(1)).thenReturn(true);
        when(productRepository.existsById(99)).thenReturn(false);

        // When & Then
        assertThrows(Exception.class, () -> {
            orderService.createOrder(request);
        });
    }


    //from here till the end are tests for createOrder

    // 1. Happy path test
    @Test
    public void testCreateOrder_HappyPath() {
        OrderRequest request = createValidOrderRequest();
        when(customerRepository.existsById(anyInt())).thenReturn(true);
        when(productRepository.getAllProductsIds()).thenReturn(Arrays.asList(1, 2, 3));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());
        when(customerRepository.getCustomerByCustomerId(anyInt())).thenReturn(new Customer());


        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
    }

    // 2. Customer doesn't exist
    @Test
    public void testCreateOrder_CustomerDoesNotExist() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId(1);

        when(customerRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            orderService.createOrder(request);
        });
    }

    // 3. Product doesn't exist
    @Test
    public void testCreateOrder_ProductDoesNotExist() {
        OrderRequest request = createValidOrderRequest();
        when(customerRepository.existsById(anyInt())).thenReturn(true);
        when(productRepository.getAllProductsIds()).thenReturn(Arrays.asList(1, 2)); // Product 3 doesn't exist

        assertThrows(EntityNotFoundException.class, () -> {
            orderService.createOrder(request);
        });
    }


    // 4. Exceeds maximum amount
    @Test
    public void testCreateOrder_ExceedsMaxAmount() {
        OrderRequest request = createValidOrderRequest();
        when(customerRepository.existsById(anyInt())).thenReturn(true);
        when(productRepository.getAllProductsIds()).thenReturn(Arrays.asList(1, 2, 3));
        // Return products with huge prices
        when(productRepository.findAll()).thenReturn(createExpensiveProducts());

        assertThrows(ExceedsTotalException.class, () -> {
            orderService.createOrder(request);
        });
    }


    // Helper method to create a valid OrderRequest for testing
    private OrderRequest createValidOrderRequest() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(1); // Set to a valid customer ID

        List<OrderItemRequest> items = new ArrayList<>();
        items.add(createOrderItemRequest(1, 2));
        items.add(createOrderItemRequest(2, 1));
        items.add(createOrderItemRequest(3, 3));

        orderRequest.setItems(items);
        return orderRequest;
    }

    private OrderItemRequest createOrderItemRequest(int productId, int quantity) {
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(productId);
        itemRequest.setQuantity(quantity);
        return itemRequest;
    }

    // Helper method to create a list of products with huge prices
    private List<Product> createExpensiveProducts() {
        List<Product> products = new ArrayList<>();

        Product product1 = new Product();
        product1.setProductId(1);
        product1.setPrice(50000000.00);

        Product product2 = new Product();
        product2.setProductId(2);
        product2.setPrice(50000000.00);

        Product product3 = new Product();
        product3.setProductId(3);
        product3.setPrice(50000000.00);

        products.add(product1);
        products.add(product2);
        products.add(product3);

        return products;
    }


}
