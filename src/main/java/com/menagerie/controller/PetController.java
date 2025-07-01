package com.menagerie.controller;

import com.menagerie.entity.Event;
import com.menagerie.entity.Pet;
import com.menagerie.model.PetsPayload;
import com.menagerie.service.PetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@Slf4j
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public ResponseEntity<Flux<Pet>> getAllPets(@RequestParam(required = false) List<String> species) {
        /*In this method, if species is provided, it will filter pets by species
        If species is not provided, it will return all pets
        It expects species to be a list of strings,so pass (,) separated values in the request
         */
        log.info("Fetching all pets");
        return ResponseEntity.ok(petService.findAllPets(species));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<Pet>> getPetById(
            @PathVariable Long id,
            @RequestParam(value = "sortBy", required = false, defaultValue = "date") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "desc") String order) {
        log.info("Fetching pet with ID: {}, sortBy: {}, order: {}", id, sortBy, order);
        Mono<Pet> pet = petService.findPetById(id, sortBy, order);
        return ResponseEntity.ok(pet);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Mono<Pet>> createPet(@RequestBody PetsPayload pet) {
        log.info("Creating new pet: {}", pet.getName());
        Mono<Pet> createdPet = petService.savePet(pet);
        return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mono<Pet>> updatePet(@PathVariable Long id, @RequestBody PetsPayload pet) {
        log.info("Updating pet with ID: {}", id);
        Mono<Pet> updatedPet = petService.updatePet(id, pet);
        return ResponseEntity.ok(updatedPet);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Mono<Void>> deletePet(@PathVariable Long id) {
        log.info("Deleting pet with ID: {}", id);
        Mono<Void> result = petService.deletePet(id);
        return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Mono<Event>> addEventToPet(@PathVariable Long id, @RequestBody Event event) {
        log.info("Adding event to pet ID: {}", id);
        Mono<Event> savedEvent = petService.saveEvent(id, event);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }
}
