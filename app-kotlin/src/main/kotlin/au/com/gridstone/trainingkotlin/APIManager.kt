package au.com.gridstone.trainingkotlin

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Retrofit
import com.google.gson.annotations.SerializedName
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers
import java.io.Serializable

data class ImageData(
  val id: String?,
  val title: String = "-",
  @SerializedName("is_album") val isAlbum: Boolean = false,
  val type: String?,
  @SerializedName("link") val imageUrl: String?,
  val views: Int = 0,
  val width: Int = 0,
  val height: Int = 0,
  @SerializedName("datetime") val dateTime: Long?
)

data class ImageDataResponse(
  val data: List<ImageData>
)

interface ImgurService {
  @Headers("Authorization: Client-ID 3436c108ccc17d3")
  @GET("3/gallery/hot/viral")
  fun getImages(): Call<ImageDataResponse>
}

object APIManager {

  private val retrofit = Retrofit.Builder()
      .baseUrl("https://api.imgur.com/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()

  val service: ImgurService = retrofit.create<ImgurService>(ImgurService::class.java)

  private var cachedImageData: List<ImageData>? = null

  fun getImages(
    useCached: Boolean,
    callback: (items: List<ImageData>) -> Unit
  ) {
    if (useCached) {
      cachedImageData?.let {
        callback(it)
        return
      }
    }

    service.getImages()
        .enqueue(object : Callback<ImageDataResponse> {
          override fun onResponse(
            call: Call<ImageDataResponse>,
            response: Response<ImageDataResponse>
          ) {
            response.body()
                ?.data?.let { data ->
              val filteredResults = filteredResults(data)
              cachedImageData = filteredResults
              callback(filteredResults)
            }
          }

          override fun onFailure(
            call: Call<ImageDataResponse>,
            t: Throwable
          ) {
          }
        })
  }

  fun getCachedImageForID(id: String): ImageData? {
    return cachedImageData?.first { data ->
      data.id == id
    }
  }

  private fun filteredResults(results: List<ImageData>): List<ImageData> {
    return results.filter { !it.isAlbum }
        .filter { it.type == "image/jpeg" || it.type == "image/png" }
  }
}