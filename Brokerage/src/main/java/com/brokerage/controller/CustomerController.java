package com.brokerage.controller;

import com.brokerage.model.Asset;
import com.brokerage.model.Customer;
import com.brokerage.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<Customer> deposit(@RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.create(customer), HttpStatus.OK);
    }

    @PostMapping("/{customerId}/deposit")
    public ResponseEntity<Customer> deposit(@PathVariable Long customerId, @RequestParam double amount) {
        return new ResponseEntity<>(customerService.depositMoney(customerId, amount), HttpStatus.OK);
    }

    @PostMapping("/{customerId}/withdraw")
    public ResponseEntity<Customer> withdraw(@PathVariable Long customerId, @RequestParam double amount, @RequestParam String iban) {
        return new ResponseEntity<>(customerService.withdrawMoney(customerId, amount, iban), HttpStatus.OK);
    }

    @GetMapping("/{customerId}/assets")
    public ResponseEntity<List<Asset>> listAssets(@PathVariable Long customerId) {
        return new ResponseEntity<>(customerService.listAssets(customerId), HttpStatus.OK);
    }
}
