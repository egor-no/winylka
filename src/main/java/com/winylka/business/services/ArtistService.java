package com.winylka.business.services;

import com.winylka.Exceptions.NoSuchMusicEndpointException;
import com.winylka.data.entities.Artist;
import com.winylka.data.repositories.ArtistRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {

    private final ArtistRepository repository;

    public ArtistService(ArtistRepository repository) {
        this.repository = repository;
    }

    public List<Artist> getAll() {
        return repository.findAll();
    }

    public Artist getArtistById(long id) {
        if (repository.findById(id).isPresent())
            return repository.findById(id).get();
        else
            throw new NoSuchMusicEndpointException("no such artist");
    }

    public Artist getArtistByName(String name) {
        return repository.findArtistsByName(name);
    }

    public Artist saveArtist(Artist artist) {
        return repository.save(artist);
    }

    public Artist editArtist(Artist updatedArtist, long id) {
        return repository.findById(id)
            .map(artist -> {
                artist.setBio(updatedArtist.getBio()==null ? artist.getBio() : updatedArtist.getBio());
                artist.setName(updatedArtist.getName()==null ? artist.getName() : updatedArtist.getName());
                artist.setCountry(updatedArtist.getCountry()==null ? artist.getCountry() : updatedArtist.getCountry());
                return repository.save(updatedArtist);
            })
            .orElseThrow( () -> new NoSuchMusicEndpointException("Can't update artist: no such artist"));
    }

    public void deleteArtist(long id) {
        repository.deleteById(id);
    }
}
