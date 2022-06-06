package com.winylka.restcontrollers;

import com.winylka.Exceptions.NoSuchMusicEndpointException;
import com.winylka.business.services.ArtistService;
import com.winylka.data.entities.Artist;
import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ArtistController {

    private final ArtistService service;
    private final ArtistModelAssembler assembler;
    public ArtistController(ArtistService service, ArtistModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("/store/artists")
    public CollectionModel<EntityModel<Artist>> all() {
        List<EntityModel<Artist>> artists = service.getAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(artists,
                linkTo(methodOn(ArtistController.class)).withSelfRel());
    }

    @GetMapping("/store/artists/{id}")
    public EntityModel<Artist> one(@PathVariable long id) {
        return assembler.toModel(service.getArtistById(id));
    }

    @PostMapping("/store/artists")
    public ResponseEntity<?> addArtist(@RequestBody Artist artist) {
        EntityModel<Artist> modelArtist = assembler.toModel(service.saveArtist(artist));

        return ResponseEntity //
                .created(modelArtist.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(modelArtist);
    }

    @PatchMapping("/store/artists/{id}/edit")
    public ResponseEntity<?> editArtists(@RequestBody Artist artist, @PathVariable long id) {
        EntityModel<Artist> modelArtist = assembler.toModel(service.editArtist(artist, id));
        return ResponseEntity.ok(modelArtist.getRequiredLink(IanaLinkRelations.SELF).toUri());
    }

    @DeleteMapping("/store/artists/{id}/delete")
    public ResponseEntity<?> deleteArtist(@PathVariable long id){
        service.deleteArtist(id);

        return ResponseEntity.noContent().build();
    }


}
