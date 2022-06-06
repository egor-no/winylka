package com.winylka.restcontrollers;

import com.winylka.business.services.ReleaseService;
import com.winylka.data.entities.Release;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ReleaseModelAssembler implements RepresentationModelAssembler<Release, EntityModel<Release>> {

    @Autowired
    private final ReleaseService service;

    public ReleaseModelAssembler(ReleaseService service) {
        this.service = service;
    }

    @Override
    public EntityModel<Release> toModel(Release entity) {
        long artistId = service.getArtistIdByReleaseId(entity.getReleaseId());

        return EntityModel.of(entity,
                linkTo(methodOn(ReleaseController.class).one(entity.getReleaseId())).withSelfRel(),
                linkTo(methodOn(AlbumController.class).one(entity.getAlbum().getAlbumId())).withRel("Info about the album"),
                linkTo(methodOn(ReleaseController.class).allByAlbum(entity.getAlbum().getAlbumId())).withRel("List of releases in the store for this album"),
                linkTo(methodOn(ArtistController.class).one(artistId)).withRel("Info about the artist"),
                linkTo(methodOn(ReleaseController.class).allByArtist(artistId)).withRel("List of releases in the store for this artist"),
                linkTo(methodOn(ReleaseController.class).all()).withRel("List of all releases to buy in the store")
        );
    }
}
