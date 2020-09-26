package com.pokedex.reactiveweb.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Pokemon(
        @Id
        var id: String?,
        var nome: String,
        var categoria: String,
        var habilidades: String,
        var peso: Double
)
