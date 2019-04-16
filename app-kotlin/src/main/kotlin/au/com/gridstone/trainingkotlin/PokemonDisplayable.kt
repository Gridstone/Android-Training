package au.com.gridstone.trainingkotlin


class PokemonDisplayable(val pokemon: Pokemon) {

  val title: String
    get() = pokemon.name.capitalize()

  val imageURL: String
    get() {
      val formattedID = String.format("%03d", pokemon.id)
      return "https://assets.pokemon.com/assets/cms2/img/pokedex/full/$formattedID.png"
    }

  val attackValue: String
    get() {
      return statForType("attack")?.toString() ?: "-"
    }

  val defenceValue: String
    get() {
      return statForType("defence")?.toString() ?: "-"
    }

  val specialAttackValue: String
    get() {
      return statForType("special-attack")?.toString() ?: "-"
    }

  val specialDefenceValue: String
    get() {
      return statForType("special-defence")?.toString() ?: "-"
    }

  val speedValue: String
    get() {
      return statForType("speed")?.toString() ?: "-"
    }

  val hpValue: String
    get() {
      return statForType("hp")?.toString() ?: "-"
    }

  private fun statForType(type: String): Int? {
    return pokemon.stats.firstOrNull { stat ->
      stat.stat.name == type
    }?.baseStat
  }
}