package com.winylka.business.services;

import com.winylka.Exceptions.NoSuchMusicEndpointException;
import com.winylka.data.entities.Album;
import com.winylka.data.entities.Release;
import com.winylka.data.repositories.OrderItemRepository;
import com.winylka.data.repositories.ReleaseRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReleaseService {

    private final ReleaseRepository repository;
    private final AlbumService albumService;
    private final OrderItemService orderitems;

    public ReleaseService(ReleaseRepository repository, AlbumService albumService, OrderItemService orderitems) {
        this.repository = repository;
        this.albumService = albumService;
        this.orderitems = orderitems;
    }

    public List<Release> getAll() {
        return repository.findAll();
    }

    public Release getReleaseById(Long id) {
        if (repository.findById(id).isPresent()) {
            return repository.findById(id).get();
        } else
            throw new NoSuchMusicEndpointException("There's no such release");
    }

    public List<Release> getReleasesYetToCome(Date date) {
        return repository.findReleasesByReleaseDateBetween(new Date(), date);
    }

    public List<Release> getFreshReleases(Date date) {
        return repository.findReleasesByReleaseDateBetween(date, new Date());
    }

    public List<Release> getReleasesByAlbumId(long id) {
        return repository.findReleasesByAlbumAlbumId(id);
    }

    public List<Release> getReleasesByArtistId(long id) {
        return albumService.getAllAlbumsByArtist(id)
                .stream().map(Album::getReleases)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public long getArtistIdByReleaseId(long releaseId) {
        return albumService.getAlbumById(
                repository.findByReleaseId(releaseId).getAlbum().getAlbumId()
        ).getArtist().getArtistId();
    }

    public List<Release> getReleasesByAlbumName(String albumName, String artistName) {
        long id = albumService.getAllAlbumsByArtistName(artistName).stream()
                .filter(album -> album.getTitle().equals(albumName)).findFirst().get().getAlbumId();
        return getReleasesByAlbumId(id);
    }

    public Release save(Release release) {
        return repository.save(release);
    }

    public Release edit(Release editedRelease, long id) {
        return repository.findById(id)
                .map(release -> {
                    release.setReleaseDate(editedRelease.getReleaseDate() == null ? release.getReleaseDate() : editedRelease.getReleaseDate());
                    release.setImg(editedRelease.getImg() == null ? release.getImg() : editedRelease.getImg());
                    release.setNotes(editedRelease.getNotes() == null ? release.getNotes() : editedRelease.getNotes());
                    release.setPrice(editedRelease.getPrice() == 0 ? release.getPrice() : editedRelease.getPrice());
                    return release;
                })
                .orElseThrow(NoSuchMusicEndpointException::new);
    }

    public void release(long id) {
        orderitems.setStatusToProgressForAll(id);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

}
