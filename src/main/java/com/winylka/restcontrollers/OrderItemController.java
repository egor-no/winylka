package com.winylka.restcontrollers;

import com.winylka.business.services.OrderItemService;
import com.winylka.business.services.OrderService;
import com.winylka.business.services.ReleaseService;
import com.winylka.data.entities.Order;
import com.winylka.data.entities.OrderItem;
import com.winylka.data.entities.OrderItemPK;
import com.winylka.data.entities.Release;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderItemController {

    private final OrderItemService service;
    private final OrderItemModelAssembler assembler;
    private final ReleaseService releaseService;
    private final OrderService orderService;

    public OrderItemController(OrderItemService service, OrderItemModelAssembler assembler, ReleaseService releaseService, OrderService orderService) {
        this.service = service;
        this.assembler = assembler;
        this.releaseService = releaseService;
        this.orderService = orderService;
    }

    @GetMapping("/back/orders/{orderId}/items/{releaseId}")
    public EntityModel<OrderItem> one(@PathVariable long orderId, @PathVariable long releaseId) {
        return assembler.toModel(service.getOrderItem(orderId, releaseId));
    }

    @GetMapping("/back/orders/{orderId}/items/")
    public CollectionModel<EntityModel<OrderItem>> allInOrder(@PathVariable long orderId) {
        List<EntityModel<OrderItem>> items = service.getOrderItems(orderId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(items,
                linkTo(methodOn(OrderItemController.class).allInOrder(orderId)).withSelfRel(),
                linkTo(methodOn(OrderController.class).one(orderId)).withRel("Info about the order")
        );
    }

    @PostMapping("/back/orders/{orderId}/items/{releaseId}")
    public ResponseEntity<?> addOrderItem(@PathVariable long orderId, @PathVariable long releaseId, @RequestBody OrderItem newItem) {
        Release releaseForItem = releaseService.getReleaseById(releaseId);
        Order orderForItem = orderService.getOrderById(orderId);

        System.out.println("controller: "+ releaseForItem.getReleaseId() + " order: " + orderForItem.getOrderId());
        newItem.setPk(new OrderItemPK(orderForItem, releaseForItem));
        if (orderService.containsItem(orderForItem, newItem)) {
            return changeOrderItemQuantity(orderId, releaseId, newItem);
        } else
            return createNewOrderItem(newItem);
    }

    private ResponseEntity<?> createNewOrderItem(OrderItem newItem) {
        OrderItem orderItem  = service.saveOrderItem(newItem);

        return ResponseEntity
                .created(linkTo(methodOn(OrderItemController.class).one(orderItem.getOrderId(), orderItem.getReleaseId())).toUri())
                .body(assembler.toModel(orderItem));
    }

    private ResponseEntity<?> changeOrderItemQuantity(long orderId, long releaseId, OrderItem newItem) {
        OrderItem orderItem = service.incrementQuantity(service.getOrderItem(orderId, releaseId),
                newItem.getQuantity());

        return ResponseEntity.ok(assembler.toModel((orderItem)));
    }

    @PutMapping("/back/orders/{orderId}/items/{releaseId}/send")
    public ResponseEntity<?> send(@PathVariable long orderId, @PathVariable long releaseId) {
        OrderItem sent = service.setStatusToSent(orderId, releaseId);
        return ResponseEntity.ok(assembler.toModel(sent));
    }

    @PutMapping("/back/orders/{orderId}/items/{releaseId}/cancel")
    public ResponseEntity<?> cancel(@PathVariable long orderId, @PathVariable long releaseId) {
        OrderItem canceled = service.setStatusToCanceled(orderId, releaseId);
        return ResponseEntity.ok(assembler.toModel(canceled));
    }

    @PutMapping("/back/orders/{orderId}/items/{releaseId}/receive")
    public ResponseEntity<?> receive(@PathVariable long orderId, @PathVariable long releaseId) {
        OrderItem received = service.setStatusToComplete(orderId, releaseId);
        return ResponseEntity.ok(assembler.toModel(received));
    }



}
