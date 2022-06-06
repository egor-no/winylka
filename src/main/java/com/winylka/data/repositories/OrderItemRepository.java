package com.winylka.data.repositories;

import com.winylka.data.entities.Order;
import com.winylka.data.entities.OrderItem;
import com.winylka.data.entities.OrderItemPK;
import com.winylka.data.entities.Release;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    public List<OrderItem> findAllByPkOrder(Order orderId);

    public List<OrderItem> findAllByPkRelease(Release releaseId);

    public OrderItem findOrderItemByPk(OrderItemPK pk);
}
