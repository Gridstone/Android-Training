package au.com.gridstone.trainingkotlin

import io.reactivex.ObservableTransformer

private fun imageURL(id: Int): String {
  val formattedID = String.format("%03d", id)
  return "https://assets.pokemon.com/assets/cms2/img/pokedex/full/$formattedID.png"
}

private fun createPokemonDisplayable(pokemonName: String, id: Int): PokemonDisplayable {
  return PokemonDisplayable(pokemonName.capitalize(),
                            id,
                            imageURL(id))
}

private fun createPokemonDetailsDisplayable(pokemon: Pokemon): PokemonDetailsDisplayable {

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

private fun generateDisplayables(pokemonSummaries: List<PokemonSummary>): List<PokemonDisplayable> =
    pokemonSummaries.map { summary ->
      createPokemonDisplayable(summary.name, pokemonSummaries.indexOf(summary) + 1)
    }

fun pokemonDetailsResultsToDetailState(): ObservableTransformer<PokemonDetailsResult, PokemonDetailsState> =
    ObservableTransformer { results ->
      results.map { result ->
        when (result) {
          is PokemonDetailsResult.Loading -> PokemonDetailsState.Loading
          is PokemonDetailsResult.Content -> {
            PokemonDetailsState.Content(createPokemonDetailsDisplayable((result.pokemon)))
          }
          is PokemonDetailsResult.Error -> PokemonDetailsState.Error(result.message)
        }
      }
    }

fun pokemonListResultsToListState(): ObservableTransformer<PokemonListResult, PokemonListState> =
    ObservableTransformer { result ->
      result.map { result ->
        when (result) {
          is PokemonListResult.Loading -> PokemonListState.Loading
          is PokemonListResult.Content -> {
            PokemonListState.Content(generateDisplayables(result.list))
          }
          is PokemonListResult.Error -> PokemonListState.Error(result.message)
        }
      }
    }

