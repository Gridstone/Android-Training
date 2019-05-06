package au.com.gridstone.trainingkotlin

open class PokemonDisplayable(name: String, id: Int) {
  val title: String = name.capitalize()
  val imageURL: String

  init {
    val formattedID = String.format("%03d", id)
    imageURL = "https://assets.pokemon.com/assets/cms2/img/pokedex/full/$formattedID.png"
  }
}

class PokemonDetailsDisplayable(pokemon: Pokemon): PokemonDisplayable(pokemon.name, pokemon.id) {
  val attackValue: String
  val defenseValue: String
  val specialAttackValue: String
  val specialDefenseValue: String
  val speedValue: String
  val hpValue: String

  init {
    fun statForType(type: String): Int? {
      return pokemon.stats.firstOrNull { stat ->
        stat.stat.name == type
      }
          ?.baseStat
    }

    fun stringForStat(name: String): String {
      return statForType(name)?.toString() ?: "-"
    }

    attackValue = stringForStat("attack")
    defenseValue = stringForStat("defense")
    specialAttackValue = stringForStat("special-attack")
    specialDefenseValue = stringForStat("special-defense")
    speedValue = stringForStat("speed")
    hpValue = stringForStat("hp")
  }
}