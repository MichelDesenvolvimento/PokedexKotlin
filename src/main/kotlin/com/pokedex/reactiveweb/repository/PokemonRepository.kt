package com.pokedex.reactiveweb.repository

import com.pokedex.reactiveweb.model.Pokemon

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface PokemonRepository : ReactiveMongoRepository<Pokemon, String>