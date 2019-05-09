package au.com.gridstone.trainingkotlin

private fun imageURL(id: Int): String {
  val formattedID = String.format("%03d", id)
  return "https://assets.pokemon.com/assets/cms2/img/pokedex/full/$formattedID.png"
}

fun createPokemonDisplayable(pokemonName: String, id: Int): PokemonDisplayable {
  return PokemonDisplayable(pokemonName.capitalize(),
                            id,
                            imageURL(id))
}

fun createPokemonDetailsDisplayable(pokemon: Pokemon): PokemonDetailsDisplayable {

  fun statForType(type: String): Int? {
    return pokemon.stats.firstOrNull { stat ->
      stat.stat.name == type
    }?.baseStat
  }

  fun stringForStat(name: String): String {
    return statForType(name)?.toString() ?: "-"
  }

  val attackValue = stringForStat("attack")
  val defenseValue = stringForStat("defense")
  val specialAttackValue = stringForStat("special-attack")
  val specialDefenseValue = stringForStat("special-defense")
  val speedValue = stringForStat("speed")
  val hpValue = stringForStat("hp")

  return PokemonDetailsDisplayable(pokemon.name.capitalize(),
                                   imageURL(pokemon.id),
                                   attackValue,
                                   defenseValue,
                                   specialAttackValue,
                                   specialDefenseValue,
                                   speedValue,
                                   hpValue)
}

