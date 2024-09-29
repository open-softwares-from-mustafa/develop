package com.brokerage.service;

import com.brokerage.fault.AssetNotFoundException;
import com.brokerage.fault.CustomerNotFoundException;
import com.brokerage.model.Asset;
import com.brokerage.model.Customer;
import com.brokerage.repository.AssetRepository;
import com.brokerage.repository.CustomerRepository;
import com.brokerage.tool.MoneyTool;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Transactional
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer depositMoney(Long customerId, double amount) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException(String.valueOf(customerId)));
        customer.setTryBalance(customer.getTryBalance() + amount);
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer withdrawMoney(Long customerId, double amount, String iban) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException(String.valueOf(customerId)));
        if (customer.getTryBalance() >= amount) {
            customer.setTryBalance(customer.getTryBalance() - amount);
            if(!MoneyTool.isIbanValid(iban)) {
                throw new AssetNotFoundException("insufficent iban");
            }
            return customerRepository.save(customer);
        }
        throw new AssetNotFoundException("Insufficient balance, deposit money for customer in advance");
    }

    public List<Asset> listAssets(Long customerId) {
        return assetRepository.findByCustomerId(customerId);
    }
}