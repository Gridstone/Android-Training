package au.com.gridstone.training_kotlin.model

/**
 * Created by rowan on 13/7/17.
 */
class Model {
    var posts: Array<ImgurPost>

    init {
        posts = arrayOf(ImgurPost(), ImgurPost(), ImgurPost(), ImgurPost(), ImgurPost())
    }
}