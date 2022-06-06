package com.winylka.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Table(name="ALBUM")
@Entity
@Data
public class Album implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long albumId;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="ARTIST_ID", nullable = false)
    @JsonIgnore
    private Artist artist;

    private int year;

    private String info;

    @OneToMany(mappedBy = "album",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    List<Release> releases;

    public String getArtistName() {
        return artist.getName();
    }

    public void addRelease(Release release) {
        releases.add(release);
        release.setAlbum(this);
    }

    public void removeRelease(Release release) {
        releases.remove(release);
        release.setAlbum(null);
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        Album album = (Album) o;
        return albumId == album.albumId;
    }

    @Override
    public int hashCode() {
        return Objects.hash( albumId );
    }
}
