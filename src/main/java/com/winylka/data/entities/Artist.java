package com.winylka.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
public class Artist implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long artistId;

    private String name;

    private String country;

    private String bio;

    @OneToMany(mappedBy = "artist",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    List<Album> albums = new ArrayList<>();

    public void addAlbum(Album album) {
        albums.add(album);
        album.setArtist(this);
    }

    public void removeAlbum(Album album) {
        albums.remove(album);
        album.setArtist(null);
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        Artist artist = (Artist) o;
        return artistId == artist.artistId;
    }

    @Override
    public int hashCode() {
        return Objects.hash( artistId );
    }
}