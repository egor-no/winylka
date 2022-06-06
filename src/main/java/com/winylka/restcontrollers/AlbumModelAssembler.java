package com.winylka.restcontrollers;

import com.winylka.data.entities.Album;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AlbumModelAssembler implements RepresentationModelAssembler<Album, EntityModel<Album>> {

    @Override
    public EntityModel<Album> toModel(Album entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(AlbumController.class).one(entity.getAlbumId())).withSelfRel(),
                linkTo(methodOn(ReleaseController.class).allByAlbum(entity.getAlbumId())).withRel("List of all releases in the store for this album"),
                linkTo(methodOn(AlbumController.class).allByArtist(entity.getArtist().getArtistId())).withRel("List of all albums in the store by this artist"),
                linkTo(methodOn(ArtistController.class).one(entity.getArtist().getArtistId())).withRel("Info about the artist"),
                linkTo(methodOn(AlbumController.class).all()).withRel("List of all albums in the store")
                );
    }
}
