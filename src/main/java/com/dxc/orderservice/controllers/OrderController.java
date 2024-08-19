package com.dxc.orderservice.controllers;

import com.dxc.orderservice.requests.OrderRequest;
import com.dxc.orderservice.responses.OrderResponse;
import com.dxc.orderservice.responses.Status;
import com.dxc.orderservice.responses.ResponseWrapper;
import com.dxc.orderservice.services.OrderService;
import com.dxc.orderservice.util.exceptions.AmbiguousRequestException;
import com.dxc.orderservice.util.exceptions.EntityNotFoundException;
import com.dxc.orderservice.util.exceptions.MissingParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


/**
 * Controller class for handling order related endpoints.
 */
@RestController
@RequestMapping("/orders")
public final class OrderController {

    /**
     * Service responsible for handling order-related business logic.
     */
    private final OrderService orderService;

    /**
     * Logger instance used to log details and errors associated with
     * the operations of the OrderController.
     */
    private final Logger logger = LoggerFactory.getLogger(
            OrderController.class
    );

    /**
     * Constructor to initialize the OrderService.
     * @param orderService The order service instance to be used.
     */
    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Endpoint to handle ambiguous requests.
     * @throws AmbiguousRequestException
     * when both 'id' and 'customerId' parameters are provided.
     */
    @GetMapping(params = {"id", "customerId"})
    public void handleAmbiguousRequest() throws AmbiguousRequestException {
        String loggingMessage = "GET /orders?id&customerId";
        logger.info(loggingMessage);
        throw new AmbiguousRequestException(
                "You provided both 'id' and 'customerId' parameters. "
                        + "Please use only one of them. (ex:../orders?id=1)"
        );
    }


    /**
     * Fetch order by its ID.
     * @param id The order ID.
     * @return ResponseWrapper containing the fetched order details.
     * @throws EntityNotFoundException when an error occurs.
     */
    @GetMapping(params = "id")
    public ResponseWrapper<OrderResponse> getOrderById(
            @RequestParam final int id) throws EntityNotFoundException {
        logger.info("GET /orders?id=" + id);

        OrderResponse res = orderService.getOrderById(id);

        Status status = new Status("Fetched order successfully");

        return new ResponseWrapper<>(status, res);
    }


    /**
     * Fetch orders by the customer ID.
     * @param customerId The customer's ID.
     * @return ResponseWrapper containing a list of fetched order details.
     * @throws EntityNotFoundException when an error occurs.
     */
    @GetMapping(params = "customerId")
    public ResponseWrapper<List<OrderResponse>> getOrdersByCustomerId(
            @RequestParam final int customerId) throws EntityNotFoundException {
        logger.info("GET /orders?customerId=" + customerId);
        List<OrderResponse> orderResponses =
                orderService.getOrdersByCustomerId(customerId);

        Status status = new Status("Fetched orders successfully");

        return new ResponseWrapper<>(status, orderResponses);

    }


    /**
     * Handle request with no parameters (Invalid request).
     * @param request HttpRequest to get parameters.
     */
    @GetMapping
    public void handleMissingParameterException(
            final HttpServletRequest request) {
        String loggingMessage = "GET /orders";

        if (request.getQueryString() != null) {
            loggingMessage += "?" + request.getQueryString();
        }

        logger.info(loggingMessage);
        throw new MissingParameterException(
                "You must provide either"
                        + " an 'id' or 'customerId' for this endpoint"
        );
    }



    /**
     * Create a new order.
     * @param orderRequest The request containing order details.
     * @return ResponseWrapper containing details of the created order.
     */
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public ResponseWrapper<OrderResponse> createOrder(
            @Valid @RequestBody final OrderRequest orderRequest) {
        // Call the service method to create the order
        logger.info("POST /orders");
        OrderResponse createdOrder = orderService.createOrder(orderRequest);
        Status status = new Status("Created order successfully");
        return new ResponseWrapper<>(status, createdOrder);
    }

}
