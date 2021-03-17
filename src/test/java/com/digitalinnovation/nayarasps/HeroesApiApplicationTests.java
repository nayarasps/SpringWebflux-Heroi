package com.digitalinnovation.livecoding;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.*;
import org.springframework.boot.test.context.SpringBootTest;
import com.digitalinnovation.livecoding.document.Heroes;
import com.digitalinnovation.livecoding.repository.HeroesRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;


import static com.digitalinnovation.livecoding.constants.HeroesConstant.HEROES_ENDPOINT_LOCAL;


@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@SpringBootTest
public class HeroesApiApplicationTests {

  @Autowired
  WebTestClient webTestClient;

  @Autowired
  HeroesRepository heroesRepository;

  // Hero Model for tests
  @Before
  public void heroModel() {
    Heroes hero = new Heroes("100", "Spider Man", "Marvel", 8);
    webTestClient.post().uri(HEROES_ENDPOINT_LOCAL)
            .bodyValue(hero)
            .exchange();
  }

  // Delete Hero Moodel after tests
  @After
  public void deleteHeroModel() {
    webTestClient.delete().uri(HEROES_ENDPOINT_LOCAL.concat("/{id}"),"100")
            .accept(MediaType.APPLICATION_JSON)
            .exchange();
  }

  // Test for POST a hero
  @Test
  public void createHero() {
    Heroes hero = new Heroes("99", "Deadpool", "Marvel", 2);
    webTestClient.post().uri(HEROES_ENDPOINT_LOCAL)
            .bodyValue(hero)
            .exchange()
            .expectStatus().isCreated()
            .expectBody();
  }

  // Test for POST a hero with a existent id (Suppose to failure)
  @Test
  public void createHeroWithExistentId() {
    Heroes hero = new Heroes("100", "Wolverine", "Marvel", 5);
    webTestClient.post().uri(HEROES_ENDPOINT_LOCAL)
            .bodyValue(hero)
            .exchange()
            .expectStatus().is4xxClientError();
  }

  // Test GET hero by id using hero model id
  @Test
  public void getOneHeroeById(){

    webTestClient.get().uri(HEROES_ENDPOINT_LOCAL.concat("/{id}"),"100")
      .exchange()
      .expectStatus().isOk()
      .expectBody();
  }

  @Test
  public void getOneHeronotFound(){

    webTestClient.get().uri(HEROES_ENDPOINT_LOCAL.concat("/{id}"),"15")
      .exchange()
      .expectStatus().isNotFound();

  }

  // Test for PUT the number of movies in a Hero
  @Test
  public void putNumberofMovies() {
    webTestClient.put().uri(HEROES_ENDPOINT_LOCAL.concat("/{id}"), "100")
            .header("newFilms", "9")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.films").isEqualTo(9);
  }

  // Test for PUT the number of movies in a not existent Hero
  @Test
  public void putNumberOfMoviesInexistentHero() {
    webTestClient.put().uri(HEROES_ENDPOINT_LOCAL.concat("/{id}"), "15")
            .header("newFilms", "9")
            .exchange()
            .expectStatus().isNotFound();
  }

  @Test
  public void deleteHero(){

    webTestClient.delete().uri(HEROES_ENDPOINT_LOCAL.concat("/{id}"),"99")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isOk()
      .expectBody(Void.class);

  }

}


