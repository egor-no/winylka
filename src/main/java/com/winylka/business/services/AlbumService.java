package com.winylka.business.services;

import com.winylka.Exceptions.NoSuchMusicEndpointException;
import com.winylka.data.entities.Album;
import com.winylka.data.repositories.AlbumRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class AlbumService {

    private final AlbumRepository repository;
    private final ArtistService artistService;

    public AlbumService(AlbumRepository albumRepository, ArtistService artistService) {
        this.repository = albumRepository;
        this.artistService = artistService;
    }

    public List<Album> getAllAlbums() {
        return repository.findAll();
    }

    public List<Album> getAllAlbumsByArtist(long id) {
        return repository.findAlbumsByArtistArtistId(id);
    }

    public List<Album> getAllAlbumsByArtistName(String name) {
        return getAllAlbumsByArtist(
                artistService.getArtistByName(name).getArtistId());
    }

    public Album getAlbumById(long id) {
        if (repository.findById(id).isPresent())
            return repository.findById(id).get();
        else
            throw new NoSuchMusicEndpointException("no such album");
    }

    public List<Album> getAlbumsByYear(int year) {
        return repository.findAlbumsByYear(year);
    }

    public List<Album> getAlbumsThisYear() {
        return repository.findAlbumsByYear(
                Calendar.getInstance().get(Calendar.YEAR));
    }

    public Album saveAlbum(Album album) {
        return repository.save(album);
    }

    public Album editAlbum(Album editedAlbum, long id) {
        return repository.findById(id)
            .map(album -> {
                album.setInfo(editedAlbum.getInfo() == null ? album.getInfo() : editedAlbum.getInfo());
                album.setYear(editedAlbum.getYear() == 0 ? album.getYear() : editedAlbum.getYear());
                album.setTitle(editedAlbum.getTitle() == null ? album.getTitle() : editedAlbum.getTitle());
                return album;
            })
            .orElseThrow(() -> new NoSuchMusicEndpointException("Cannot update the album: no such album"));
    }

    public void deleteAlbum(long id) {
        repository.deleteById(id);
    }
}
