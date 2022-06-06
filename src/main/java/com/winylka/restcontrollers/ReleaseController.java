package com.winylka.restcontrollers;

import com.winylka.business.services.AlbumService;
import com.winylka.business.services.ReleaseService;
import com.winylka.data.entities.Release;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ReleaseController {

    private final ReleaseService service;
    private final AlbumService albumService;
    private final ReleaseModelAssembler assembler;

    public ReleaseController(ReleaseService service, AlbumService albumService, ReleaseModelAssembler assembler) {
        this.service = service;
        this.albumService = albumService;
        this.assembler = assembler;
    }

    @GetMapping("/store")
    public CollectionModel<EntityModel<Release>> all() {
        List<EntityModel<Release>> releases = service.getAll().stream()
                .map(assembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(releases,
                linkTo(methodOn(ReleaseController.class).all()).withSelfRel()
        );
    }

    @GetMapping("/store/artists/{artistId}/releases")
    public CollectionModel<EntityModel<Release>> allByArtist(@PathVariable long artistId) {
        List<EntityModel<Release>> releases = service.getReleasesByArtistId(artistId).stream()
                .map(assembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(releases,
                linkTo(methodOn(ReleaseController.class).allByArtist(artistId)).withSelfRel(),
                linkTo(methodOn(ArtistController.class).one(artistId)).withRel("Info about the artist"),
                linkTo(methodOn(ReleaseController.class).all()).withRel("List of all releases in the store")
        );
    }

    @GetMapping({"/store/albums/{albumId}/releases",
            "/store/artists/{artistId}/albums/{albumId}/releases"})
    public CollectionModel<EntityModel<Release>> allByAlbum(@PathVariable long albumId){
        List<EntityModel<Release>> releases = service.getReleasesByAlbumId(albumId).stream()
                .map(assembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(releases,
                linkTo(methodOn(ReleaseController.class).allByAlbum(albumId)).withSelfRel(),
                linkTo(methodOn(AlbumController.class).one(albumId)).withRel("Info about the album"),
                linkTo(methodOn(ReleaseController.class).all()).withRel("List of all releases in the store")
        );
    }

    @GetMapping({"/store/{id}",
            "/store/artists/{artistId}/albums/{albumId}/releases/{id}"})
    public EntityModel<Release> one(@PathVariable long id) {
        return assembler.toModel(service.getReleaseById(id));
    }

    @PostMapping({"/store/albums/{albumId}/releases",
            "/store/artists/{artistId}/albums/{albumId}/releases"})
    public ResponseEntity<?> addRelease(@RequestBody Release release, @PathVariable long albumId) {
        release.setAlbum(albumService.getAlbumById(albumId));
        EntityModel<Release> releaseModel = assembler.toModel(service.save(release));

        return ResponseEntity
                .created(releaseModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(releaseModel);
    }

    @PutMapping({"/store/{id}/release",
            "/store/albums/{albumId}/releases/{id}/release",
            "/store/artists/{artistId}/albums/{albumId}/releases/{id}/release"})
    public ResponseEntity<?> releaseIt(@PathVariable long id) {
        service.release(id);

        return ResponseEntity.ok().build();
    }

    @PatchMapping( {"/store/{id}/edit",
            "/store/albums/{albumId}/releases/{id}/edit",
            "/store/artists/{artistId}/albums/{albumId}/releases/{id}/edit"})
    public ResponseEntity<?> editRelease(@PathVariable long id, @RequestBody Release release) {
        EntityModel<Release> editedRelease = assembler.toModel(service.edit(release, id));

        return ResponseEntity
                .ok(editedRelease.getRequiredLink(IanaLinkRelations.SELF).toUri());
    }

    @DeleteMapping({"/store/{id}/delete",
            "/store/albums/{albumId}/releases/{id}/delete",
            "/store/artists/{artistId}/albums/{albumId}/releases/{id}/delete"})
    public ResponseEntity<?> deleteRelease(@PathVariable long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}
