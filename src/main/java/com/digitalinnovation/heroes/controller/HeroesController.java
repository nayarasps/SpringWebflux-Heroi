package com.digitalinnovation.heroes.controller;

import com.digitalinnovation.heroes.document.Heroes;
import com.digitalinnovation.heroes.exception.HeroIdAlreadyExistsException;
import com.digitalinnovation.heroes.exception.HeroNotFoundException;
import com.digitalinnovation.heroes.repository.HeroesRepository;
import com.digitalinnovation.heroes.service.HeroesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.digitalinnovation.heroes.constants.HeroesConstant.HEROES_ENDPOINT_LOCAL;

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
  public Mono<ResponseEntity<Heroes>> findByIdHero(@PathVariable String id) throws HeroNotFoundException {
    log.info("Requesting the hero with id {}", id);
    return heroesService.findByIdHero(id)
      .map((item) -> new ResponseEntity<>(item, HttpStatus.OK))
      .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PutMapping(HEROES_ENDPOINT_LOCAL + "/{id}")
  public Mono<ResponseEntity<Heroes>> updateHeroMovies(@PathVariable String id,
                                        @RequestHeader int newFilms) throws HeroNotFoundException {
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
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Heroes> createHero(@RequestBody Heroes heroes) throws HeroIdAlreadyExistsException {
    log.info("A new Hero was Created");
    return heroesService.createHero(heroes);
  }


  // Delete Hero by Id
  @DeleteMapping(HEROES_ENDPOINT_LOCAL + "/{id}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public Mono<HttpStatus> deletebyIDHero(@PathVariable String id) throws HeroNotFoundException {
    heroesService.deletebyIDHero(id);
    log.info("Deleting the hero with id {}", id);
    return Mono.just(HttpStatus.NO_CONTENT);
  }


}
