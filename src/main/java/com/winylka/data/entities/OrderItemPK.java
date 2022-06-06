package com.winylka.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="orderItems")
public class OrderItemPK implements Serializable {

    @ManyToOne
            //(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "RELEASE_ID")
    private Release release;

    public OrderItemPK() {

    }

    public OrderItemPK(Order ORDER, Release RELEASE) {
        this.order = ORDER;
        this.release = RELEASE;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }
}

