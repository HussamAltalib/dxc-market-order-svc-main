package com.dxc.orderservice.controllers;

import com.dxc.orderservice.models.Order;
import com.dxc.orderservice.repositories.OrderItemRepository;
import com.dxc.orderservice.responses.OrderResponse;
import com.dxc.orderservice.responses.Status;
import com.dxc.orderservice.services.OrderService;
import com.dxc.orderservice.util.GlobalExceptionHandler;
import com.dxc.orderservice.util.constants.ErrorCodes;
import com.dxc.orderservice.util.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.nullValue;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderItemRepository orderItemRepository;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void validGetOrderById() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        OrderResponse response = new OrderResponse(1, "john", Order.OrderStatus.PENDING, 767.77, now, new ArrayList<>());

        given(orderService.getOrderById(1)).willReturn(response);//telling Mockito to do the following:Whenever the method getOrderById of the orderService mock object is called with the argument 1,
        // Instead of executing the actual method, return the response object.( if the orderService.getOrderById(1) method is called, it will return the response object)

        Status status = new Status("Fetched order successfully");
        // when
        ResultActions result = mockMvc.perform(get("/orders")//simulate an HTTP GET request to the /orders endpoint: The result of this is object, which represents the response returned by the server
                .param("id", String.valueOf(1))
                .contentType(MediaType.APPLICATION_JSON));
        // then
        result.andExpect(jsonPath("$.status.message", is(status.getMessage())))
                .andExpect(jsonPath("$.status.code", is(status.getCode())))
                .andExpect(jsonPath("$.status.type", is(status.getType())))
                .andExpect(jsonPath("$.data.id", is(response.getId())))
                .andExpect(jsonPath("$.data.customerName", is(response.getCustomerName())))
                .andExpect(jsonPath("$.data.status", is(response.getStatus().toString())))
                .andExpect(jsonPath("$.data.totalAmount", is(response.getTotalAmount())))
                .andExpect(jsonPath("$.data.createdAt", not(emptyString())))
                .andExpect(jsonPath("$.data.items", is(new ArrayList<>())));//checks that the "items" list in the JSON response is empty
    }


    @Test
    void invalidGetOrderByIdOrder() throws Exception {
        //given
        int orderId = 99;
        String message = "There is no order with this ID";
        EntityNotFoundException exception = new EntityNotFoundException(message);

        given(orderService.getOrderById(orderId)).willThrow(exception);

        // when
        ResultActions result = mockMvc.perform(get("/orders") // the method getOrderById will execute here then the exception will throw like what said in line 83
                .param("id", String.valueOf(orderId))
                .contentType(MediaType.APPLICATION_JSON));
        // then
        result.andExpect(MockMvcResultMatchers.status().isNotFound()) // select the exception type
                .andExpect(jsonPath("$.status.message", is(message)))
                .andExpect(jsonPath("$.status.code", is(ErrorCodes.ENTITY_NOT_FOUND)))
                .andExpect(jsonPath("$.status.type", is("error")));
//                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void getOrderByIdNonInt() throws Exception {
        //given
        String nonIntOrderId = "abc";
        // when
        ResultActions result = mockMvc.perform(get("/orders")
                .param("id", nonIntOrderId)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())// select the exception type
                .andExpect(jsonPath("$.status.message", is(not(emptyString()))))
                .andExpect(jsonPath("$.status.code", is(ErrorCodes.TYPE_MISMATCH)))
                .andExpect(jsonPath("$.status.type", is("error")));
//                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void validGetOrdersByCustomerId() throws Exception {
        List<OrderResponse> mockResponses = new ArrayList<>();
        mockResponses.add(new OrderResponse(1, "jhon", Order.OrderStatus.PENDING, 767.77, LocalDateTime.now(), new ArrayList<>()));

        given(orderService.getOrdersByCustomerId(1)).willReturn(mockResponses);

        // when
        ResultActions result = mockMvc.perform(get("/orders")
                .param("customerId", "1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(jsonPath("$.status.message", is("Fetched orders successfully")))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].customerName", is("jhon")))
                .andExpect(jsonPath("$.data[0].status", is(Order.OrderStatus.PENDING.toString())))
                .andExpect(jsonPath("$.data[0].totalAmount", is(767.77)))
                .andExpect(jsonPath("$.data[0].items", is(new ArrayList<>())));
    }

    @Test
    void handleInvalidRequestsTest() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/orders")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status.message", is("You must provide either an 'id' or 'customerId' for this endpoint")));
    }

    @Test
    void handleAmbiguousRequestTest() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/orders")
                .param("id", "1")
                .param("customerId", "1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(MockMvcResultMatchers.status().isConflict())  // Changed to isConflict() which expects 409 status
                .andExpect(jsonPath("$.status.message", is("You provided both 'id' and 'customerId' parameters. Please use only one of them. (ex:../orders?id=1)")));
    }


    @Test
    void handleBodyExceptionsTest() throws Exception {
        ResultActions result = mockMvc.perform(post("/orders")
                .content("{\"customerId\": 9,\n" + "    \"totalAmount\": 159.98}")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status.message", not(emptyString())))
                .andExpect(jsonPath("$.status.code", is(ErrorCodes.INVALID_BODY)))
                .andExpect(jsonPath("$.status.type", is("error")));
//                .andExpect(jsonPath("$.data", nullValue()));
    }
    

}
