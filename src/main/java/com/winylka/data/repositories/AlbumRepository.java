package com.winylka.data.repositories;

import com.winylka.data.entities.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    public List<Album> findAlbumsByArtistArtistId(long id);

    public List<Album> findAlbumsByYear(int year);

}
