package au.com.gridstone.trainingkotlin

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path


data class Stat(
  val name: String
)

data class StatDetail(
  @SerializedName("base_stat")
  val baseStat: Int,
  val stat: Stat
)

data class Pokemon(
  val id: Int,
  val name: String,
  val stats: List<StatDetail>
)

data class PokemonSummary(
  val name: String
)

data class PokemonBaseResponse(
  val results: List<PokemonSummary>
)

interface PokemonListService {
  @GET("pokemon?limit=151")
  fun getPokemonList(): Call<PokemonBaseResponse>
}

interface PokemonDetailsService {
  @GET("pokemon/{name}")
  fun getPokemon(@Path("name") user: String): Call<Pokemon>
}

object APIManager {

  private val retrofit = Retrofit.Builder()
      .baseUrl("https://pokeapi.co/api/v2/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()

  val listService: PokemonListService = retrofit.create(PokemonListService::class.java)

  val detailsService: PokemonDetailsService = retrofit.create(PokemonDetailsService::class.java)

  private var cachedPokemon: List<Pokemon>? = null

  fun getCachedPokemonForID(id: Int): Pokemon? {
    return cachedPokemon?.first { pokemon ->
      pokemon.id == id
    }
  }

  fun getPokemon(
    useCached: Boolean,
    callback: (pokemon: List<Pokemon>) -> Unit
  ) {
    if (useCached) {
      cachedPokemon?.let { pokemon ->
        callback(pokemon)
        return
      }
    }

    listService.getPokemonList()
        .enqueue(object : Callback<PokemonBaseResponse> {
          override fun onResponse(
            call: Call<PokemonBaseResponse>,
            response: Response<PokemonBaseResponse>
          ) {
            response.body()
                ?.results?.let { results ->
              val pokemonToReturn: ArrayList<Pokemon> = ArrayList()
              results.forEach { result ->
                detailsService.getPokemon(result.name)
                    .enqueue(object : Callback<Pokemon> {
                      override fun onResponse(
                        call: Call<Pokemon>,
                        response: Response<Pokemon>
                      ) {
                        response.body()
                            ?.let { pokemon ->
                              pokemonToReturn.add(pokemon)
                              val sortedPokemon = pokemonToReturn.sortedWith(compareBy { it.id })
                              cachedPokemon = sortedPokemon
                              callback(sortedPokemon)
                            }
                      }

                      override fun onFailure(
                        call: Call<Pokemon>,
                        t: Throwable
                      ) {
                      }
                    })
              }
            }
          }

          override fun onFailure(
            call: Call<PokemonBaseResponse>,
            t: Throwable
          ) {
          }
        })
  }
}