package au.com.gridstone.trainingkotlin

import com.google.gson.annotations.SerializedName
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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
  fun getPokemonList(): Observable<PokemonBaseResponse>
}

interface PokemonDetailsService {
  @GET("pokemon/{id}")
  fun getPokemon(@Path("id") id: Int): Observable<Pokemon>
}

object APIManager {

  private val retrofit = Retrofit.Builder()
      .baseUrl("https://pokeapi.co/api/v2/")
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build()

  private val listService: PokemonListService = retrofit.create(PokemonListService::class.java)

  private val detailsService: PokemonDetailsService = retrofit.create(PokemonDetailsService::class.java)

  val listObservable = listService.getPokemonList()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .replay(1)
      .autoConnect()

  private var cachedPokemon = mutableMapOf<Int, Pokemon>()

  fun getPokemon(
    id: Int,
    subscriber: Observer<Pokemon>
  ): Observable<Pokemon> {

    cachedPokemon[id]?.let { pokemon ->
      val observable = Observable.just(pokemon)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
      observable.subscribe(subscriber)
      return observable
    }

    val observable = detailsService.getPokemon(id)
        .subscribeOn(Schedulers.io())
        .doOnNext { pokemon ->
          cachedPokemon[id] = pokemon
        }
        .observeOn(AndroidSchedulers.mainThread())

    observable.subscribe(subscriber)

    return observable
  }
}