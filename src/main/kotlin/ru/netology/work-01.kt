class PostNotFoundException(message: String) : RuntimeException(message)

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

    val comments: Array<Comment> = emptyArray(),

    val likes: Likes = Likes(),
    val markedAsAds: Boolean = false,
    val isFavorite: Boolean = false,
    val attachments: Array<Attachment> = emptyArray()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Post

        if (id != other.id) return false
        if (ownerId != other.ownerId) return false
        if (fromId != other.fromId) return false
        if (createdBy != other.createdBy) return false
        if (date != other.date) return false
        if (replyOwnerId != other.replyOwnerId) return false
        if (replyPostId != other.replyPostId) return false
        if (friendsOnly != other.friendsOnly) return false
        if (markedAsAds != other.markedAsAds) return false
        if (isFavorite != other.isFavorite) return false
        if (text != other.text) return false
        if (!comments.contentEquals(other.comments)) return false
        if (likes != other.likes) return false
        if (!attachments.contentEquals(other.attachments)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + ownerId
        result = 31 * result + fromId
        result = 31 * result + (createdBy ?: 0)
        result = 31 * result + date.hashCode()
        result = 31 * result + replyOwnerId
        result = 31 * result + (replyPostId ?: 0)
        result = 31 * result + friendsOnly.hashCode()
        result = 31 * result + markedAsAds.hashCode()
        result = 31 * result + isFavorite.hashCode()
        result = 31 * result + (text?.hashCode() ?: 0)
        result = 31 * result + comments.contentHashCode()
        result = 31 * result + likes.hashCode()
        result = 31 * result + attachments.contentHashCode()
        return result
    }
}

data class Comment(
    val count: Int = 0,
    val text: String,
    val date: Long
)

data class Likes(
    val count: Int = 0,
    val userLikes: Boolean = false,
    val canLike: Boolean = true,
    val canPublish: Boolean = true
)


object WallService {

    private var posts = emptyArray<Post>()
    private var nextId = 1

    fun createComment(postId: Int, comment: Comment): Comment {
        for (index in posts.indices) {
            if (posts[index].id == postId) {
                val newComment = comment.copy(count = (posts[index].comments.lastOrNull()?.count ?: 0) + 1)
                val updatedPost = posts[index].copy(
                    comments = posts[index].comments + newComment
                )
                posts[index] = updatedPost
                return newComment
            }
        }
        throw PostNotFoundException("Post with id=$postId not found")
    }

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
            println(post)
        }
    }

    fun clear() {
        posts = emptyArray()
        nextId = 1
    }
}

fun main() {

    val post1 = WallService.add(
        Post(
            ownerId = 1,
            text = "First post"
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

    val vComm1 = Comment(text = "My comment 1", date = System.currentTimeMillis())

    val vComm2 = Comment(text = "My comment 2", date = System.currentTimeMillis())

    WallService.createComment(postId = post1.id, comment = vComm1)
    WallService.createComment(postId = post1.id, comment = vComm2)


    println("Updated: $updated")

    WallService.printPosts()
}