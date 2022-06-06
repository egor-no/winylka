package com.winylka.restcontrollers;

import com.winylka.business.services.AlbumService;
import com.winylka.business.services.ArtistService;
import com.winylka.data.entities.Album;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AlbumController {

    private final AlbumService service;
    private final ArtistService artistService;
    private final AlbumModelAssembler assembler;

    public AlbumController(AlbumService service, ArtistService artistService, AlbumModelAssembler assembler) {
        this.service = service;
        this.artistService = artistService;
        this.assembler = assembler;
    }

    @GetMapping("/store/albums")
    public CollectionModel<EntityModel<Album>> all() {
        List<EntityModel<Album>> albums = service.getAllAlbums().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(albums,
                linkTo(methodOn(AlbumController.class).all()).withSelfRel());
    }

    @GetMapping({"/store/albums/{id}",
            "/store/artists/{idArtist}/albums/{id}"})
    public EntityModel<Album> one(@PathVariable long id) {
        return assembler.toModel(service.getAlbumById(id));
    }

    @GetMapping("/store/artists/{idArtist}/albums/")
    public CollectionModel<EntityModel<Album>> allByArtist(@PathVariable long idArtist) {
        List<EntityModel<Album>> albums = service.getAllAlbumsByArtist(idArtist).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(albums,
                linkTo(methodOn(AlbumController.class).allByArtist(idArtist)).withSelfRel(),
                linkTo(methodOn(ArtistController.class).one(idArtist)).withRel("Info about the artist"),
                linkTo(methodOn(AlbumController.class).all()).withRel("List of all albums in the store")
        );
    }

    @PostMapping("/store/artists/{idArtist}/albums/")
    public ResponseEntity<?> addAlbum(@RequestBody Album album, @PathVariable long idArtist) {
       // album.setArtist(artistService.getArtistById(idArtist));
        artistService.getArtistById(idArtist).addAlbum(album);
        EntityModel<Album> modelAlbum = assembler.toModel(service.saveAlbum(album));

        return ResponseEntity
                .created(modelAlbum.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(modelAlbum);
    }

    @PatchMapping({"/store/albums/{id}/edit",
            "/store/artists/{idArtist}/albums/{id}/edit"})
    public ResponseEntity<?> editAlbum(@PathVariable long id, @RequestBody Album album) {
        EntityModel<Album> editedAlbum = assembler.toModel(service.editAlbum(album, id));

        return ResponseEntity
                .ok(editedAlbum.getRequiredLink(IanaLinkRelations.SELF).toUri());
    }

    @DeleteMapping({"/store/albums/{id}/delete",
            "/store/artists/{idArtist}/albums/{id}/delete"})
    public ResponseEntity<?> deleteAlbum(@PathVariable long id) {
        service.deleteAlbum(id);

        return ResponseEntity.noContent().build();
    }

}
