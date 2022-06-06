package com.winylka.business.services;

import com.winylka.Exceptions.WrongStatusException;
import com.winylka.data.entities.*;
import com.winylka.data.repositories.OrderItemRepository;
import com.winylka.data.repositories.ReleaseRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderItemService {

    private final OrderItemRepository items;
    private final ReleaseRepository releases;
    private final OrderService orders;


    public OrderItemService(OrderItemRepository items, ReleaseRepository releases, OrderService orders) {
        this.items = items;
        this.releases = releases;
        this.orders = orders;
    }

    public OrderItem getOrderItem(long orderId, long releaseId) {
        return items.findOrderItemByPk(getPK(orderId, releaseId));
    }

    public OrderItemPK getPK(long orderId, long releaseId) {
        return new OrderItemPK(orders.getOrderById(orderId), releases.findByReleaseId(releaseId));
    }

    public List<OrderItem> getOrderItems(long orderId)  {
        return items.findAllByPkOrder(orders.getOrderById(orderId));
    }

    public OrderItem cancelItem(long orderId, long releaseId) {
        OrderItem item = setStatusToCanceled(orderId,releaseId);
        if (orders.isOrderReadyToBeCanceled(orderId))
            orders.setStatusToCanceled(orderId);
        if (orders.isOrderReadyToBeComplete(orderId))
            orders.setStatusToComplete(orderId);
        return item;
    }

    public OrderItem setStatusToCanceled(long orderId, long releaseId) {
        OrderItem itemToCancel = getOrderItem(orderId, releaseId);
        if (itemToCancel.getStatus() == OrderItemStatus.IN_PROGRESS ||
                itemToCancel.getStatus() == OrderItemStatus.PRE_ORDER ||
                itemToCancel.getStatus() == OrderItemStatus.CANCELLED) {
            itemToCancel.setStatus(OrderItemStatus.CANCELLED);
            return items.save(itemToCancel);
        }
        return itemToCancel;
    }

    public OrderItem completeItem(long orderId, long releaseId) {
        OrderItem item = setStatusToComplete(orderId, releaseId);
        if (orders.isOrderReadyToBeComplete(orderId))
            orders.setStatusToComplete(orderId);
        return item;
    }

    public OrderItem setStatusToComplete(long orderId, long releaseId) {
        OrderItem itemToComplete = getOrderItem(orderId, releaseId);
        if (itemToComplete.getStatus() == OrderItemStatus.SENT) {
            itemToComplete.setStatus(OrderItemStatus.RECEIVED);
            return items.save(itemToComplete);
        } else
            throw new WrongStatusException("Can't complete item with status "+itemToComplete.getStatus());
    }

    public OrderItem setStatusToProgress(long orderId, long releaseId) {
        OrderItem itemToRelease = getOrderItem(orderId, releaseId);
        if (itemToRelease.getStatus() == OrderItemStatus.PRE_ORDER) {
            itemToRelease.setStatus(OrderItemStatus.IN_PROGRESS);
            return items.save(itemToRelease);
        } else
            throw new WrongStatusException("Can't release item with status "+itemToRelease.getStatus());
    }

    public void setStatusToProgressForAll(long releaseId) {
        List<OrderItem> orderItems = items.findAllByPkRelease(releases.findByReleaseId(releaseId));
        orderItems.stream().filter(orderItem -> orderItem.getStatus() == OrderItemStatus.PRE_ORDER)
                .forEach(orderItem -> orderItem.setStatus(OrderItemStatus.IN_PROGRESS));
        items.saveAll(orderItems);
    }

    public OrderItem setStatusToSent(long orderId, long releaseId) {
        OrderItem itemToSend = getOrderItem(orderId, releaseId);
        if (itemToSend.getStatus() == OrderItemStatus.IN_PROGRESS) {
            itemToSend.setStatus(OrderItemStatus.SENT);
            return items.save(itemToSend);
        } else
            throw new WrongStatusException("Can't send item with status "+itemToSend.getStatus());
    }

    public OrderItem saveOrderItem(OrderItem orderItem) {
        Date now = new Date();
        Order order = orders.getOrderById(orderItem.getOrderId());
        if (order.getStatus() != OrderStatus.IN_PROGRESS)
            throw new WrongStatusException("Can't add an item to an order with status"
                        + order.getOrderItems());

        Date releaseDate = releases.findByReleaseId(orderItem.getReleaseId()).getReleaseDate();
        if (releaseDate.before(now))
            orderItem.setStatus(OrderItemStatus.IN_PROGRESS);
        else
            orderItem.setStatus(OrderItemStatus.PRE_ORDER);

        return items.save(orderItem);
    }

    public OrderItem incrementQuantity(OrderItem orderItem, int inc) {
        orderItem.setQuantity(orderItem.getQuantity() + inc);
        return items.save(orderItem);
    }

    public int getItemPrice(long orderId, long releaseId) {
        OrderItem item = getOrderItem(orderId, releaseId);
        int quantity = item.getQuantity();
        int pricePerOne = releases.findByReleaseId(releaseId).getPrice();
        return quantity * pricePerOne;
    }
}
