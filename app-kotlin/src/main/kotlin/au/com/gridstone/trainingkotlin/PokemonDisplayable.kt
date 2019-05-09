package au.com.gridstone.trainingkotlin

open class PokemonDisplayable(val title: String,
                              val id: Int,
                              val imageURL: String)

class PokemonDetailsDisplayable(val title: String,
                                val imageURL: String,
                                val attackValue: String,
                                val defenseValue: String,
                                val specialAttackValue: String,
                                val specialDefenseValue: String,
                                val speedValue: String,
                                val hpValue: String)