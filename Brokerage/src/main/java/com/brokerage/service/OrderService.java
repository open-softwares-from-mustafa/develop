package com.brokerage.service;

import com.brokerage.constant.OrderConstant;
import com.brokerage.enums.Side;
import com.brokerage.enums.Status;
import com.brokerage.fault.OrderNotFoundException;
import com.brokerage.model.Asset;
import com.brokerage.model.Order;
import com.brokerage.repository.AssetRepository;
import com.brokerage.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Transactional
    public Order createOrder(Order order) {
        if(!com.brokerage.enums.Asset.TRY.name().equals(order.getAssetName()))
        {
            throw new OrderNotFoundException("Only TRY asset available for sell or buy");
        }
        Optional<Asset> tryAsset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName());

        if (tryAsset.isPresent() && tryAsset.get().getUsableSize() >= order.getPrice() * order.getSize()) {
            // Deduct usable TRY size for BUY orders
            Asset asset = tryAsset.get();
            asset.setUsableSize(asset.getUsableSize() - (int)(order.getPrice() * order.getSize()));
            assetRepository.save(asset);
            order.setStatus(Status.PENDING);
            return orderRepository.save(order);
        } else if(Objects.nonNull(order)) {

            Asset asset = new Asset();
            asset.setAssetName(order.getAssetName());
            asset.setCustomerId(order.getCustomerId());
            asset.setSize(order.getSize());
            asset.setUsableSize(OrderConstant.DEFAULT_USABLE_SIZE); // default usable size is 30000, it can be fetched from db
            asset.setUsableSize(asset.getUsableSize() - (int)(order.getPrice() * order.getSize()));
            assetRepository.save(asset);
            order.setStatus(Status.PENDING);
            order.setCreateDate(LocalDateTime.now());
            return orderRepository.save(order);
        }
        throw new OrderNotFoundException("Insufficient funds");
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(String.valueOf(orderId)));
        if (order.getStatus() == Status.PENDING) {
            Asset tryAsset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName()).get();
            tryAsset.setUsableSize(tryAsset.getUsableSize() + (int)(order.getPrice() * order.getSize()));
            assetRepository.save(tryAsset);
            order.setStatus(Status.CANCELED);
            orderRepository.save(order);
        } else {
            throw new OrderNotFoundException("Only pending orders can be canceled");
        }
    }

    public List<Order> listOrders(Long customerId, LocalDateTime start, LocalDateTime end) {
        return orderRepository.findByCustomerIdAndCreateDateBetween(customerId, start, end);
    }

    // Match pending orders
    public void matchOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (Status.PENDING.equals(order.getStatus())) {
            order.setStatus(Status.MATCHED);

            if (Side.BUY.equals(order.getOrderSide())) {
                Asset asset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName())
                        .orElse(new Asset(order.getCustomerId(), order.getAssetName()));
                asset.setSize(asset.getSize() + order.getSize());
                asset.setUsableSize(asset.getUsableSize() + order.getSize());
                assetRepository.save(asset);
            }

            orderRepository.save(order);
        }
    }
}
