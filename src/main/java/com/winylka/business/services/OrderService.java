package com.winylka.business.services;

import com.winylka.Exceptions.NoSuchOrderException;
import com.winylka.Exceptions.WrongStatusException;
import com.winylka.data.entities.OrderItem;
import com.winylka.data.entities.OrderStatus;
import com.winylka.data.entities.Order;
import com.winylka.data.entities.OrderItemStatus;
import com.winylka.data.repositories.OrderRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final OrderItemService itemService;
    private final ClientService clientService;

    public OrderService(OrderRepository orders, @Lazy OrderItemService itemService, ClientService clientService) {
        this.repository = orders;
        this.itemService = itemService;
        this.clientService = clientService;
    }

    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    public Order getOrderById(long id) {
        return repository.findById(id)
                .orElseThrow(NoSuchOrderException::new);
    }

    public List<Order> getOrdersByClientId(long id) {
        return repository.findAllByClient(clientService.getClientById(id));
    }

    public Order cancelOrderAndAllItems(long orderId) {
        Order order = repository.findById(orderId).orElseThrow(
                NoSuchOrderException::new);
        if (order.getStatus() == OrderStatus.IN_PROGRESS) {
            cancelAllItems(orderId);
            setStatusToCanceled(orderId);
        } else
            throw new WrongStatusException("Can't cancel the order that isn't in progress");
        return order;
    }

    public void cancelAllItems(long orderId) {
        itemService.getOrderItems(orderId).stream()
                .map(OrderItem::getReleaseId)
                .forEach(itemId -> itemService.setStatusToCanceled(orderId, itemId));
    }

    public void setStatusToCanceled(long orderId) {
    //    if (isOrderReadyToBeCanceled(orderId)) {
        Order order = repository.findById(orderId).orElseThrow(
                NoSuchOrderException::new);
        order.setStatus(OrderStatus.CANCELLED);
        repository.save(order);
      //  } else
      //      throw new WrongStatusException("Can't cancel the order if not items are canceled");
    }

    public boolean isOrderReadyToBeCanceled(long orderId) {
        boolean notAllItemsCanceled = itemService.getOrderItems(orderId).stream()
                .map(item -> item.getStatus())
                .anyMatch(status -> status != OrderItemStatus.CANCELLED &&
                                    status != OrderItemStatus.SENT &&
                                    status != OrderItemStatus.RECEIVED);
        return !notAllItemsCanceled;
    }

    public Order completeOrderAndAllItems(long orderId) {
        Order order = repository.findById(orderId).orElseThrow(
                NoSuchOrderException::new);
        if (order.getStatus() == OrderStatus.IN_PROGRESS) {
            completeAllItems(orderId);
            setStatusToComplete(orderId);
        } else
            throw new WrongStatusException("Can't complete the order with status "+order.getStatus());
        return order;
    }

    public void completeAllItems(long orderId) {
        itemService.getOrderItems(orderId).stream()
                .map(OrderItem::getReleaseId)
                .forEach(itemId -> itemService.setStatusToComplete(orderId, itemId));
    }

    public void setStatusToComplete(long orderId) {
   //     if (isOrderReadyToBeComplete(orderId)) {
        Order order = repository.findById(orderId).orElseThrow(
                NoSuchOrderException::new);
        order.setStatus(OrderStatus.COMPLETED);
        repository.save(order);
     //   } else
     //       throw new WrongStatusException("Can't complete the order before all the items in it are received or cancelled");
    }

    public boolean isOrderReadyToBeComplete(long orderId) {
        boolean atLeastOneIsComplete = itemService.getOrderItems(orderId).stream()
                .anyMatch(item -> item.getStatus() == OrderItemStatus.RECEIVED);
        boolean noneArePreOrderOrProcessing = itemService.getOrderItems(orderId).stream()
                .map(item -> item.getStatus())
                .allMatch(status -> status != OrderItemStatus.PRE_ORDER &&
                        status != OrderItemStatus.SENT &&
                        status != OrderItemStatus.IN_PROGRESS);
        return atLeastOneIsComplete && noneArePreOrderOrProcessing;
    }

    public boolean containsItem(Order order, OrderItem newOrderItem) {
        return order.getOrderItems().stream()
                .anyMatch(orderItem -> orderItem.equals(newOrderItem));
    }

    public Order saveOrder(Order order) {
        return repository.save(order);
    }

    public int getOrderPrice(long orderId) {
        return itemService.getOrderItems(orderId).stream()
                .mapToInt(item -> itemService.getItemPrice(orderId, item.getReleaseId()))
                .sum();
    }

}
