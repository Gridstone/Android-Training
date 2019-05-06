package au.com.gridstone.trainingkotlin

import au.com.gridstone.trainingkotlin.PokemonListState.Content
import au.com.gridstone.trainingkotlin.PokemonListState.Loading
import com.google.gson.annotations.SerializedName
import com.jakewharton.retrofit2.adapter.rxjava2.Result
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

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
  fun getPokemonList(): Single<Result<PokemonBaseResponse>>
}

interface PokemonDetailsService {
  @GET("pokemon/{id}")
  fun getPokemon(@Path("id") id: Int): Single<Result<Pokemon>>
}

sealed class PokemonListResult {
  object Loading : PokemonListResult()
  data class Content(val list: List<PokemonSummary>) : PokemonListResult()
  data class Error(val message: String = "Unknown Error") : PokemonListResult()
}

sealed class PokemonDetailsResult {
  object Loading : PokemonDetailsResult()
  data class Content(val pokemon: Pokemon) : PokemonDetailsResult()
  data class Error(val message: String = "Unknown Error") : PokemonDetailsResult()
}


object APIManager {

  private val refreshActions: PublishSubject<Unit> = PublishSubject.create()

  private val retrofit = Retrofit.Builder()
      .baseUrl("https://pokeapi.co/api/v2/")
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build()

  private val listService: PokemonListService = retrofit.create(PokemonListService::class.java)

  private val detailsService: PokemonDetailsService = retrofit.create(PokemonDetailsService::class.java)

  private val listObservable: Observable<PokemonListResult> = listService.getPokemonList()
      .map { result ->
        if (!result.isError && result.response()?.isSuccessful == true) {
          PokemonListResult.Content(result.response().body()!!.results)
        } else {
          PokemonListResult.Error(result.response()?.errorBody()?.toString() ?: "Unknown error")
        }
      }
      .toObservable()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .startWith(PokemonListResult.Loading)

  val pokemonListResults: Observable<PokemonListResult> = refreshActions
      .startWith(Unit)
      .switchMap { listObservable }
      .startWith(PokemonListResult.Loading)
      .distinctUntilChanged()
      .replay(1)
      .autoConnect()

  fun refresh() {
    refreshActions.onNext(Unit)
  }

  fun detailsObservable(id: Int): Observable<PokemonDetailsResult> {
    val observable = detailsService.getPokemon(id)
        .map { result ->
          if (!result.isError && result.response()?.isSuccessful == true) {
            PokemonDetailsResult.Content(result.response().body()!!)
          } else {
            PokemonDetailsResult.Error(result.response()?.errorBody()?.toString() ?: "Unknown error")
          }
        }
        .toObservable()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .startWith(PokemonDetailsResult.Loading)

    return observable
  }
}