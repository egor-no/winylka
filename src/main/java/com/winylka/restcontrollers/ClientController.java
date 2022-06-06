package com.winylka.restcontrollers;

import com.winylka.business.services.ClientService;
import com.winylka.data.entities.Client;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class ClientController {

    private final ClientService service;
    private final ClientModelAssembler assembler;

    public ClientController(ClientService service, ClientModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("back/clients/{id}")
    public EntityModel<Client> one(@PathVariable long id) {
        return assembler.toModel(service.getClientById(id));
    }

    @GetMapping("back/clients")
    public CollectionModel<EntityModel<Client>> all() {
        List<EntityModel<Client>> clients = service.getClients().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(clients,
                linkTo(methodOn(ClientController.class).all()).withSelfRel()
        );
    }

    @PostMapping("back/clients")
    public ResponseEntity<?> newClient(@RequestBody Client newClient) {
        EntityModel<Client> clientModel = assembler.toModel(service.saveClient(newClient));

        return ResponseEntity
                .created(clientModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(clientModel);
    }

    @PutMapping("back/clients/{id}")
    public ResponseEntity<?> editClient(@PathVariable long id, @RequestBody Client editedClient) {
        EntityModel<Client> clientModel = assembler.toModel(service.editClient(editedClient, id));

        return ResponseEntity
                .ok(clientModel.getRequiredLink(IanaLinkRelations.SELF).toUri());
    }

}
