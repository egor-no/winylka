package com.winylka.restcontrollers;

import com.winylka.data.entities.OrderItem;
import com.winylka.data.entities.OrderItemStatus;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class OrderItemModelAssembler implements RepresentationModelAssembler<OrderItem, EntityModel<OrderItem>> {

    @Override
    public EntityModel<OrderItem> toModel(OrderItem entity) {
        EntityModel model = EntityModel.of(entity,
            linkTo(methodOn(OrderItemController.class).one(entity.getOrderId(), entity.getReleaseId())).withSelfRel(),
            linkTo(methodOn(OrderItemController.class).allInOrder(entity.getOrderId())).withRel("All items in the order"),
            linkTo(methodOn(OrderController.class).one(entity.getOrderId())).withRel("Info about the order"),
            linkTo(methodOn(ReleaseController.class).one(entity.getReleaseId())).withRel("Info about the release")
        );

        if (entity.getStatus() == OrderItemStatus.PRE_ORDER || entity.getStatus() == OrderItemStatus.IN_PROGRESS) {
            model.add(linkTo(methodOn(OrderItemController.class).cancel(entity.getOrderId(), entity.getReleaseId())).withRel("Cancel the item"));
        }

        return model;
    }
}
