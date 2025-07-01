package com.menagerie.service;

import com.menagerie.entity.Event;
import com.menagerie.entity.Pet;
import com.menagerie.exception.NotFoundException;
import com.menagerie.model.PetsPayload;
import com.menagerie.repository.EventRepository;
import com.menagerie.repository.PetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final EventRepository eventRepository;

    public PetServiceImpl(PetRepository petRepository, EventRepository eventRepository) {
        this.petRepository = petRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Flux<Pet> findAllPets(List<String> species) {
        return Mono.fromCallable(() -> {
                    if (species != null && !species.isEmpty()) {
                        return petRepository.findBySpeciesIn(species);
                    } else {
                        return petRepository.findAll();
                    }
                })
                .flatMapMany(list -> {
                    if (list == null || list.isEmpty()) {
                        return Flux.error(new NotFoundException("No pets found"));
                    }
                    return Flux.fromIterable(list);
                })
                .doOnError(error -> log.error("Error fetching pets: {}", error.getMessage()));
    }


    public Mono<Pet> getPetById(Long id) {
        return petRepository.findById(id)
                .map(Mono::just)
                .orElseGet(() -> Mono.error(new NotFoundException("Pet not found with ID: " + id)));
    }

    @Override
    public Mono<Pet> findPetById(Long id,String sortBy, String order) {
        return Mono.fromCallable(() -> petRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Pet not found with ID: " + id)))
                .flatMap(pet -> findEventsByPetId(id)
                        .sort((e1, e2) -> {
                            int cmp = 0;
                            if ("date".equalsIgnoreCase(sortBy)) {
                                cmp = e1.getDate().compareTo(e2.getDate());
                            } else if ("type".equalsIgnoreCase(sortBy)) {
                                cmp = e1.getType().compareTo(e2.getType());
                            }
                            return "desc".equalsIgnoreCase(order) ? -cmp : cmp;
                        })
                        .collectList()
                        .map(events -> {
                            pet.setEvents(events);
                            return pet;
                        })
                );
    }

    @Override
    public Mono<Pet> savePet(PetsPayload pet) {
        return Mono.fromCallable(() -> {
                    Pet newPet = Pet.builder()
                            .name(pet.getName())
                            .owner(pet.getOwner())
                            .species(pet.getSpecies())
                            .sex(pet.getSex().toString())
                            .birth(pet.getBirth())
                            .death(pet.getDeath())
                            .build();
                    return petRepository.save(newPet);
                })
                .switchIfEmpty(Mono.error(new NotFoundException("Failed to save pet")))
                .doOnError(error -> {
                    log.error("Error saving pet: {}", error.getMessage());
                    throw new RuntimeException("Error saving pet: " + error.getMessage());
                });
    }

    @Override
    public Mono<Pet> updatePet(Long id, PetsPayload petPayload) {
        return getPetById(id)
                .flatMap(existingPet -> {
                    Pet tempPet = Pet.builder()
                            .name(petPayload.getName())
                            .owner(petPayload.getOwner())
                            .species(petPayload.getSpecies())
                            .sex(petPayload.getSex().toString())
                            .birth(petPayload.getBirth())
                            .death(petPayload.getDeath())
                            .build();
                    preparePet(tempPet, existingPet);
                    return Mono.just(petRepository.save(existingPet));
                })
                .switchIfEmpty(Mono.error(new NotFoundException("Pet not found with ID: " + id)));
    }


    @Override
    public Mono<Void> deletePet(Long id) {
        return getPetById(id)
                .flatMap(pet -> Mono.fromRunnable(() -> petRepository.delete(pet)))
                .doOnSuccess(aVoid -> log.info("Pet with ID {} deleted successfully", id))
                .doOnError(error -> log.error("Error deleting pet with ID {}: {}", id, error.getMessage())).then();
    }

    @Override
    public Flux<Event> findEventsByPetId(Long petId) {
        return Flux.fromIterable(eventRepository.findByPetId(petId))
                .switchIfEmpty(Flux.just())
                .doOnError(error -> {
            log.error("Error fetching events for pet ID {}: {}", petId, error.getMessage());
            throw new RuntimeException("Error fetching events for pet ID " + petId + ": " + error.getMessage());
        });

    }

    @Override
    public Mono<Event> saveEvent(Long petId, Event event) {
        return getPetById(petId)
                .flatMap(p -> {
                            event.setPet(p);
                            return Mono.just(eventRepository.save(event))
                                    .doOnError(error -> {
                                        log.error("Error saving event for pet ID {}: {}", petId, error.getMessage());
                                        throw new RuntimeException("Error saving event for pet ID " + petId + ": " + error.getMessage());
                                    })
                                    .doOnSuccess(savedEvent -> {
                                        log.info("Event saved successfully for pet ID {}: {}", petId, savedEvent);
                                    });
                        }
                )
                .switchIfEmpty(Mono.error(new NotFoundException("Pet not found with ID: " + petId)));


    }

    private static void preparePet(Pet pet, Pet existingPet) {
        existingPet.setName(pet.getName());
        existingPet.setBirth(pet.getBirth());
        if (pet.getEvents() != null) {
            existingPet.setEvents(pet.getEvents());
        }
        existingPet.setDeath(pet.getDeath());
        existingPet.setSpecies(pet.getSpecies());
        existingPet.setSex(pet.getSex());
        existingPet.setOwner(pet.getOwner());
    }
}
