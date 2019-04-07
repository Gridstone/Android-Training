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
    private var isAlbum: Boolean? = null

    @SerializedName("type")
    private var type: String? = null

    @SerializedName("link")
    private var imageURL: String? = null

    @SerializedName("height")
    private var height: Int? = null

    @SerializedName("width")
    private var width: Int? = null

    @SerializedName("views")
    private var viewCount: Int? = null

    fun getTitle(): String {
       return title ?: "-"
    }

    fun getIsAlbum(): Boolean {
        return isAlbum ?: false
    }

    fun getType(): String {
        return type ?: ""
    }

    fun getImageURL(): String {
        return imageURL ?: ""
    }

    fun getHeight(): Int {
        return height ?: 0
    }

    fun getWidth(): Int {
        return width ?: 0
    }

    fun getViewCount(): Int {
        return viewCount ?: 0
    }
}

class ImageDataResponse {
    @SerializedName("data")
    private var data: List<ImageData>? = null

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