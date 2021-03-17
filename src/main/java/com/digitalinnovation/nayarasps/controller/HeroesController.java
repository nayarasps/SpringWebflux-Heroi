package com.digitalinnovation.livecoding.controller;

import com.digitalinnovation.livecoding.document.Heroes;
import com.digitalinnovation.livecoding.repository.HeroesRepository;
import com.digitalinnovation.livecoding.service.HeroesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static com.digitalinnovation.livecoding.constants.HeroesConstant.HEROES_ENDPOINT_LOCAL;

@RestController

public class HeroesController {

  HeroesService heroesService;

  HeroesRepository heroesRepository;

  private static final org.slf4j.Logger log =
    org.slf4j.LoggerFactory.getLogger(HeroesController.class);

  public HeroesController(HeroesService heroesService, HeroesRepository heroesRepository) {
    this.heroesService = heroesService;
    this.heroesRepository = heroesRepository;
  }

  @GetMapping(HEROES_ENDPOINT_LOCAL)
  @ResponseStatus(HttpStatus.OK)
  public Flux<Heroes> getAllItems() {
    log.info("requesting the list off all heroes");
    return heroesService.findAll();

  }

  @GetMapping(HEROES_ENDPOINT_LOCAL + "/{id}")
  public Mono<ResponseEntity<Heroes>> findByIdHero(@PathVariable String id) {
    log.info("Requesting the hero with id {}", id);
    return heroesService.findByIdHero(id)
      .map((item) -> new ResponseEntity<>(item, HttpStatus.OK))
      .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PutMapping(HEROES_ENDPOINT_LOCAL + "/{id}")
  public Mono<ResponseEntity<Heroes>> updateHeroMovies(@PathVariable String id,
                                        @RequestHeader int newFilms) {
    log.info("Updating number of movies for hero with id {}", id);
    return heroesService.findByIdHero(id)
            .map((hero) -> {
              hero.setFilms(newFilms);
              heroesService.save(hero);
              return new ResponseEntity<>(hero, HttpStatus.OK);
            })
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  // If the hero id is already in use, the response will be a bad request
  @PostMapping(HEROES_ENDPOINT_LOCAL)
  public ResponseEntity<?> createHero(@RequestBody Heroes heroes) {
    Mono<Heroes> hero = heroesService.findByIdHero(heroes.getId());
    if (hero.toString().equals("MonoJust")) {
      log.info("Hero with id {} already exists", heroes.getId());
      return new ResponseEntity<String>("Hero with id " + heroes.getId() + " already exists",
              HttpStatus.CONFLICT);
    } else {
      log.info("New Hero created");
      heroesService.save(heroes);
      return new ResponseEntity<>(heroes, HttpStatus.CREATED);
    }
  }

  // Delete Hero by Id
  @DeleteMapping(HEROES_ENDPOINT_LOCAL + "/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public Mono<HttpStatus> deletebyIDHero(@PathVariable String id) {
    heroesService.deletebyIDHero(id);
    log.info("Deleting the hero with id {}", id);
    return Mono.just(HttpStatus.OK);
  }


}
