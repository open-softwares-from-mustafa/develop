package com.brokerage;

import com.brokerage.controller.CustomerController;
import com.brokerage.model.Customer;
import com.brokerage.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    public void testDeposit() throws Exception {
        Customer customer = new Customer();
        when(customerService.depositMoney(1L,20000)).thenReturn(customer);
        StringBuilder path = new StringBuilder();
        path.append("/customers/").append("1").append("/deposit?").append("amount=20000");
        mockMvc.perform(post(path.toString())
                        /*.with(httpBasic("admin","admin@2024"))*/
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(status().isOk());
    }
}
