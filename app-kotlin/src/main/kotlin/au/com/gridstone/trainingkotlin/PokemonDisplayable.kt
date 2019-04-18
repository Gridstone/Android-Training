package au.com.gridstone.trainingkotlin


class PokemonDisplayable(pokemon: Pokemon) {

  val attackValue: String
  val defenseValue: String
  val specialAttackValue: String
  val specialDefenseValue: String
  val speedValue: String
  val hpValue: String
  val imageURL: String
  val title: String = pokemon.name.capitalize()

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

    val formattedID = String.format("%03d", pokemon.id)
    imageURL = "https://assets.pokemon.com/assets/cms2/img/pokedex/full/$formattedID.png"
  }
}