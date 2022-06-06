package com.winylka.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Table(name = "ORDER_INFO")
@Entity
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @ManyToOne
    @JoinColumn(name="CLIENT_ID", nullable = false)
    private Client client;

    private OrderStatus status;

    @OneToMany(mappedBy = "pk.order",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    List<OrderItem> orderItems;
}
