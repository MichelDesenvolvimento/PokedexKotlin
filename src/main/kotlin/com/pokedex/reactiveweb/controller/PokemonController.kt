package com.pokedex.reactiveweb.controller

import com.pokedex.reactiveweb.model.Pokemon
import com.pokedex.reactiveweb.model.PokemonEvent
import com.pokedex.reactiveweb.repository.PokemonRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration


@RestController
@RequestMapping("/pokemons")
class PokemonController(private val repository: PokemonRepository) {

    @GetMapping
    fun allPokemons() = repository.findAll()

    @GetMapping("/{id}")
    fun getPokemon(@PathVariable id: String) = repository.findById(id)
            .map { pokemon -> ResponseEntity.ok(pokemon) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun savePokemon(@RequestBody pokemon: Pokemon) = repository.save(pokemon)

    @PutMapping("{id}")
    fun updatePokemon(@PathVariable(value = "id") id: String,
                      @RequestBody pokemon: Pokemon) = repository.findById(id)
            .flatMap { existingPokemon ->
                existingPokemon.nome = pokemon.nome
                existingPokemon.categoria = pokemon.categoria
                existingPokemon.habilidades = pokemon.habilidades
                existingPokemon.peso = pokemon.peso
                repository.save<Pokemon?>(existingPokemon)
            }
            .map { updatePokemon: Pokemon? -> ResponseEntity.ok<Pokemon?>(updatePokemon!!) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @DeleteMapping("{id}")
    fun deletePokemon(@PathVariable(value = "id") id: String) = repository.findById(id)
            .flatMap { existingPokemon: Pokemon? ->
                repository.delete(existingPokemon!!)
                        .then(Mono.just(ResponseEntity.ok().build<Void>()))
            }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @DeleteMapping
    fun deleteAllPokemons() = repository.deleteAll()

    @GetMapping(value = ["/events"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getPokemonEvents() = Flux.interval(Duration.ofSeconds(5))
            .map { value: Long -> PokemonEvent(value, "Product Event") }
}