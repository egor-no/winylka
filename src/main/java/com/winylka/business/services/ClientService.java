package com.winylka.business.services;

import com.winylka.Exceptions.NoSuchClientException;
import com.winylka.data.entities.Client;
import com.winylka.data.repositories.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public List<Client> getClients() {
        return repository.findAll();
    }

    public Client getClientById(Long id) {
        return repository.findById(id).isPresent() ?
                repository.findById(id).get() :
                null;
    }

    public Client saveClient(Client client) {
        return repository.save(client);
    }

    public Client editClient(Client editedClient, long id) {
        return repository.findById(id)
                .map(client -> {
                    if (editedClient.getAddress() != null)
                        client.setAddress(editedClient.getAddress());
                    if (editedClient.getFirstName() != null)
                        client.setFirstName(editedClient.getFirstName());
                    if (editedClient.getLastName() != null)
                        client.setLastName(editedClient.getLastName());
                    return client;
                })
                .orElseThrow(
                    () -> new NoSuchClientException("Can't update the client - there's no such client")
                );
    }

}
