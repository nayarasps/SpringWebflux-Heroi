package com.digitalinnovation.heroes.service;

import com.digitalinnovation.heroes.document.Heroes;
import com.digitalinnovation.heroes.exception.HeroIdAlreadyExistsException;
import com.digitalinnovation.heroes.exception.HeroNotFoundException;
import com.digitalinnovation.heroes.repository.HeroesRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Service
public class HeroesService {
  private final HeroesRepository heroesRepository;

  public HeroesService(HeroesRepository heroesRepository) {
    this.heroesRepository = heroesRepository;
  }

  public Flux<Heroes> findAll(){

    return Flux.fromIterable(this.heroesRepository.findAll());
  }

  public Mono<Heroes> createHero(Heroes heroes) throws HeroIdAlreadyExistsException {
    verifyIfIsAlreadyRegistered(heroes.getId());
    return save(heroes);
  }

  public Mono<Heroes> findByIdHero(String id) throws HeroNotFoundException {
    Optional<Heroes> heroesOptional = this.heroesRepository.findById(id);

    if (!heroesOptional.isPresent()) {
      throw new HeroNotFoundException(id);
    }

    return  Mono.just(heroesOptional.get());
  }

  public Mono<Heroes> save(Heroes heroes){
    return  Mono.just(this.heroesRepository.save(heroes));
  }

  public Mono<Boolean> deletebyIDHero(String id) throws HeroNotFoundException {
    verifyIfExists(id);
    heroesRepository.deleteById(id);
    return Mono.just(true);
  }

  private void verifyIfIsAlreadyRegistered(String id) throws HeroIdAlreadyExistsException {
    Optional<Heroes> heroesOptional = this.heroesRepository.findById(id);

    if (heroesOptional.isPresent()) {
      throw new HeroIdAlreadyExistsException(id);
    }
  }

  private void verifyIfExists(String id) throws HeroNotFoundException {
    Optional<Heroes> heroesOptional = this.heroesRepository.findById(id);

    if (!heroesOptional.isPresent()) {
      throw new HeroNotFoundException(id);
    }
  }

}

