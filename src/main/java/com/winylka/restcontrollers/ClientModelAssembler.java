package com.winylka.restcontrollers;

import com.winylka.data.entities.Client;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ClientModelAssembler implements RepresentationModelAssembler<Client, EntityModel<Client>> {

    @Override
    public EntityModel<Client> toModel(Client entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ClientController.class).one(entity.getClientId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).allByClient(entity.getClientId())).withRel("List of orders by this client"),
                linkTo(methodOn(ClientController.class).all()).withRel("List of all clients")
        );
    }
}
