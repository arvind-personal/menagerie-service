package com.menagerie.service;

import com.menagerie.entity.Event;
import com.menagerie.entity.Pet;
import com.menagerie.model.PetsPayload;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface PetService {

    Flux<Pet> findAllPets(List<String> species);

    Mono<Pet> findPetById(Long id,String sortBy, String sortOrder);

    Mono<Pet> savePet(PetsPayload pet);

    Mono<Pet> updatePet(Long id, PetsPayload pet);

    Mono<Void> deletePet(Long id);

    Flux<Event> findEventsByPetId(Long petId);

    Mono<Event> saveEvent(Long petId, Event event);
}
