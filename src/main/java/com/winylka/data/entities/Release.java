package com.winylka.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="RELEASE")
@Data
public class Release {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long releaseId;

    @ManyToOne
    @JoinColumn(name="ALBUM_ID")
    @JsonIgnore
    private Album album;

    private Date releaseDate;

    private String format;

    private String notes;

    private String label;

    private int price;

    private String img;

    public String getArtistName() {
        return album.getArtistName();
    }

    public String getAlbumTitle() {
        return album.getTitle();
    }
}
