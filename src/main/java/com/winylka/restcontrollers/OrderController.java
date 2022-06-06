package com.winylka.restcontrollers;

import com.winylka.business.services.ClientService;
import com.winylka.business.services.OrderService;
import com.winylka.data.entities.Order;
import com.winylka.data.entities.OrderStatus;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderController {

    private final OrderService service;
    private final OrderModelAssembler assembler;
    private final ClientService clientService;

    public OrderController(OrderService service, OrderModelAssembler assembler, ClientService clientService) {
        this.service = service;
        this.assembler = assembler;
        this.clientService = clientService;
    }

    @GetMapping("/back/orders")
    public CollectionModel<EntityModel<Order>> all() {
        List<EntityModel<Order>> orders = service.getAllOrders().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).all()).withSelfRel()
        );
    }

    @GetMapping("/back/orders/{id}")
    public EntityModel<Order> one(@PathVariable long id) {
        return assembler.toModel(service.getOrderById(id));
    }

    @GetMapping("/back/clients/{clientId}/orders")
    public CollectionModel<EntityModel<Order>> allByClient(@PathVariable long clientId) {
        List<EntityModel<Order>> orders = service.getOrdersByClientId(clientId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).allByClient(clientId)).withSelfRel()
        );
    }

    @PutMapping({"/back/clients/{clientId}/orders/{orderId}/complete",
            "/back/orders/{orderId}/complete"})
    public ResponseEntity<?> complete(@PathVariable long orderId) {
        Order completedOrder = service.completeOrderAndAllItems(orderId);
        return ResponseEntity.ok(assembler.toModel(completedOrder));
    }

    @PutMapping({"/back/clients/{clientId}/orders/{orderId}/cancel",
            "/back/orders/{orderId}/cancel"})
    public ResponseEntity<?> cancel(@PathVariable long orderId) {
        Order canceledOrder = service.cancelOrderAndAllItems(orderId);
        return ResponseEntity.ok(assembler.toModel(canceledOrder));
    }

    @PostMapping("/back/clients/{clientId}/orders")
    public ResponseEntity<?> newOrder(@RequestBody Order newOrder, @PathVariable long clientId) {
        newOrder.setClient(clientService.getClientById(clientId));

        newOrder.setStatus(OrderStatus.IN_PROGRESS);
        newOrder = service.saveOrder(newOrder);

        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).one(newOrder.getOrderId())).toUri()) //
                .body(assembler.toModel(newOrder));
    }


}
