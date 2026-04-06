interface Attachment {
    val type: String
}

data class Photo(
    val id: Int,
    val ownerId: Int,
    val photo130: String,
    val photo604: String
)

data class PhotoAttachment(
    val photo: Photo
) : Attachment {
    override val type: String = "photo"
}

data class Video(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val duration: Int
)

data class VideoAttachment(
    val video: Video
) : Attachment {
    override val type: String = "video"
}

data class Audio(
    val id: Int,
    val ownerId: Int,
    val artist: String,
    val title: String,
    val duration: Int
)

data class AudioAttachment(
    val audio: Audio
) : Attachment {
    override val type: String = "audio"
}

data class Doc(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val size: Int
)

data class DocAttachment(
    val doc: Doc
) : Attachment {
    override val type: String = "doc"
}

data class Link(
    val url: String,
    val title: String
)

data class LinkAttachment(
    val link: Link
) : Attachment {
    override val type: String = "link"
}

data class Post(
    val id: Int = 0,
    val ownerId: Int = 0,
    val fromId: Int = 0,
    val createdBy: Int? = null,
    val date: Long = 0,
    val text: String? = null,
    val replyOwnerId: Int = 0,
    val replyPostId: Int? = null,
    val friendsOnly: Boolean = false,
    val comments: Comments? = null,
    val likes: Likes = Likes(),
    val markedAsAds: Boolean = false,
    val isFavorite: Boolean = false,
    val attachments: Array<Attachment> = emptyArray()
)

data class Likes(
    val count: Int = 0,
    val userLikes: Boolean = false,
    val canLike: Boolean = true,
    val canPublish: Boolean = true
)

data class Comments(
    val count: Int = 0,
    val canPost: Boolean = true,
    val groupsCanPost: Boolean = true,
    val canClose: Boolean = false,
    val canOpen: Boolean = true
)

object WallService {

    private var posts = emptyArray<Post>()
    private var nextId = 1

    fun add(post: Post): Post {
        val newPost = post.copy(id = nextId++)
        posts += newPost
        return newPost
    }

    fun update(post: Post): Boolean {
        for (index in posts.indices) {
            if (posts[index].id == post.id) {
                posts[index] = post.copy(
                    ownerId = posts[index].ownerId,
                    date = posts[index].date
                )
                return true
            }
        }
        return false
    }

    fun printPosts() {
        for (post in posts) {
            println("Привет")
            println(post)
        }
    }

    fun clear() {
        posts = emptyArray()
        nextId = 1
    }
}

fun main() {
    println("Привет мир")
    val post1 = WallService.add(
        Post(
            ownerId = 1,
            text = "Hello!"
        )
    )

    val post2 = WallService.add(
        Post(
            ownerId = 2,
            text = "Second post"
        )
    )

    WallService.printPosts()

    val updated = WallService.update(
        Post(
            id = post1.id,
            text = "Renew text"
        )
    )

    println("Updated: $updated")

    WallService.printPosts()
}