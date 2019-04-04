package au.com.gridstone.trainingkotlin

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Retrofit
import com.google.gson.annotations.SerializedName
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers

class ImageData {
    @SerializedName("title")
    private var title: String? = null

    @SerializedName("is_album")
    private var isAlbum: String? = null

    fun setTitle(title: String) {
        this.title = title
    }

    fun getTitle(): String {
       return title ?: "-"
    }

    fun getIsAlbum(): Boolean {
        return isAlbum == "true"
    }
}

class ImageDataResponse {
    @SerializedName("data")
    private var data: List<ImageData>? = null

    fun setData(data: List<ImageData>) {
        this.data = data
    }

    fun getData(): List<ImageData> {
        return data ?: ArrayList()
    }
}

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

    val service = retrofit.create<ImgurService>(ImgurService::class.java)
}