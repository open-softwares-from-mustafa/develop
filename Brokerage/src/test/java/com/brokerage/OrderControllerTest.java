package com.brokerage;

import com.brokerage.controller.OrderController;
import com.brokerage.enums.Asset;
import com.brokerage.enums.Side;
import com.brokerage.enums.Status;
import com.brokerage.model.Order;
import com.brokerage.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void testCreateOrder() throws Exception {
        Order order = new Order();
        order.setCustomerId(1L);
        order.setAssetName("TRY");
        order.setOrderSide(Side.BUY);
        order.setSize(10);
        order.setPrice(150);
        order.setStatus(Status.PENDING);

        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/orders/create")
                        /*.with(httpBasic("admin","admin@2024"))*/
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(order)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testListOrders() throws Exception {

        Long customerId = 1L;
        LocalDateTime start = LocalDateTime.parse("2024-09-29T16:50:31.8764398");
        LocalDateTime end = LocalDateTime.parse("2024-09-30T16:50:31.8764398");

        // Sample orders
        Order order1 = new Order(1L, Asset.TRY.name(), Side.BUY, 10, 150.0, Status.PENDING, start);
        Order order2 = new Order(2L, Asset.TRY.name(), Side.SELL, 5, 2700.0, Status.MATCHED, end);
        List<Order> orders = Arrays.asList(order1, order2);

        // Mock the service call
        when(orderService.listOrders(customerId, start, end)).thenReturn(orders);


        mockMvc.perform(get("/orders/list")
                        /*.with(httpBasic("admin","admin@2024"))*/
                        .param("customerId", "1")
                        .param("start", "2024-09-29T16:50:31.8764398")
                        .param("end", "2024-09-30T16:50:31.8764398")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"assetName\":\"AAPL\",\"orderSide\":\"BUY\",\"size\":10,\"price\":150.0,\"status\":\"PENDING\",\"createDate\":\"2024-09-29T16:50:31.8764398\"}," +
                        "{\"id\":2,\"assetName\":\"GOOGL\",\"orderSide\":\"SELL\",\"size\":5,\"price\":2700.0,\"status\":\"MATCHED\",\"createDate\":\"2024-09-30T16:50:31.8764398\"}]"));
    }
}
