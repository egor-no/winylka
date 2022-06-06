package com.winylka.restcontrollers;

import com.winylka.data.entities.Order;
import com.winylka.data.entities.OrderStatus;
import org.hibernate.annotations.common.reflection.XMethod;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import javax.swing.text.html.parser.Entity;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

    @Override
    public EntityModel<Order> toModel(Order entity) {
        EntityModel<Order> model = EntityModel.of(entity,
                linkTo(methodOn(OrderController.class).one(entity.getOrderId())).withSelfRel(),
                linkTo(methodOn(OrderItemController.class).allInOrder(entity.getOrderId())).withRel("List of all items in the order"),
                linkTo(methodOn(OrderController.class).all()).withRel("List of all orders"),
                linkTo(methodOn(OrderController.class).allByClient(entity.getClient().getClientId())).withRel("List of all orders of the client")
        );

        if (entity.getStatus() == OrderStatus.IN_PROGRESS) {
            model.add(linkTo(methodOn(OrderController.class).complete(entity.getOrderId())).withRel("Complete the order"));
            model.add(linkTo(methodOn(OrderController.class).cancel(entity.getOrderId())).withRel("Cancel the order"));
        }

        return model;
    }

}
