package com.pokedex.reactiveweb.configuration

import com.pokedex.reactiveweb.model.Pokemon
import com.pokedex.reactiveweb.repository.PokemonRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import reactor.core.publisher.Flux

@Configuration
class StartupConfiguration {

    @Bean
    fun init(operations: ReactiveMongoOperations?, repository: PokemonRepository): CommandLineRunner? {
        return CommandLineRunner {
            val pokemonFlux = Flux.just(
                    Pokemon("1", "Bulbassauro", "Semente", "OverGrow", 6.09),
                    Pokemon("2", "Charizard", "Fogo", "Blaze", 90.05),
                    Pokemon("3", "Caterpie", "Minhoca", "Poeira do Escudo", 2.09),
                    Pokemon("4", "Blastoise", "Marisco", "Torrente", 6.09))
                    .flatMap { s: Pokemon -> repository.save(s) }
            pokemonFlux
                    .thenMany(repository.findAll())
                    .subscribe { x: Pokemon? -> println(x) }
        }
    }
}