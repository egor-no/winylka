package com.winylka.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "ORDER_ITEMS")
@Entity
@Data
public class OrderItem {

    @EmbeddedId
    private OrderItemPK pk;

    private int quantity;

    private OrderItemStatus status;

    public OrderItem() {

    }

    public OrderItem(OrderItemPK pk, int quantity, OrderItemStatus status) {
        this.pk = pk;
        this.quantity = quantity;
        this.status = status;
    }

    @JsonIgnore
    public long getOrderId() {
        return pk.getOrder().getOrderId();
    }

    @JsonIgnore
    public long getReleaseId() {
        return pk.getRelease().getReleaseId();
    }

    @JsonIgnore
    public Order getOrder() {
        return pk.getOrder();
    }

    @JsonIgnore
    public Release getRelease() {
        return pk.getRelease();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;

        return (this.getOrderId() == orderItem.getOrderId() &&
                this.getReleaseId() == orderItem.getReleaseId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk, status);
    }
}
