package com.winylka.restcontrollers;

import com.winylka.data.entities.Artist;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ArtistModelAssembler implements RepresentationModelAssembler<Artist, EntityModel<Artist>> {

    @Override
    public EntityModel<Artist> toModel(Artist entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ArtistController.class).one(entity.getArtistId())).withSelfRel(),
                linkTo(methodOn(ArtistController.class).all()).withRel("List of all artists"),
                linkTo(methodOn(AlbumController.class).allByArtist(entity.getArtistId())).withRel("List of albums in the store by this artist"),
                linkTo(methodOn(ReleaseController.class).allByArtist(entity.getArtistId())).withRel("List of releases in the store by this artist")
        );
    }
}
