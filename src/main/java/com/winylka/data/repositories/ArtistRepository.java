package com.winylka.data.repositories;

import com.winylka.data.entities.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist,Long> {

    public Artist findArtistsByName(String name);
}
