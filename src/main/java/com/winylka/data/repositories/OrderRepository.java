package com.winylka.data.repositories;

import com.winylka.data.entities.Client;
import com.winylka.data.entities.Order;
import org.hibernate.cfg.JPAIndexHolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    public List<Order> findAllByClient(Client id);
}
