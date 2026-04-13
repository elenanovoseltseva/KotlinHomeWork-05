import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse


class WallServiceTest {

    @Before
    fun clearBeforeTest() {
        WallService.clear()
    }

    @Test
    fun add_shouldSetId() {
        WallService.clear()

        val post = Post(text = "Test")
        val result = WallService.add(post)

        assertTrue(result.id != 0)
    }

    @Test
    fun update_existingId_returnsTrue() {
        WallService.clear()

        val post = WallService.add(Post(text = "Old"))

        val update = Post(
            id = post.id,
            text = "New text"
        )
        val result = WallService.update(update)
        assertTrue(result)
    }

    @Test
    fun update_notExistingId_returnsFalse() {
        WallService.clear()
        val update = Post(
            id = 999,
            text = "No post"
        )
        val result = WallService.update(update)
        assertFalse(result)
    }

    @Test
    fun shouldAddCommentToExistingPost() {
        WallService.clear()

        val post = WallService.add(Post(ownerId = 1, text = "Post"))

        val comment = Comment(
            count = 1,
            text = "Hello",
            date = System.currentTimeMillis())

        val result = WallService.createComment(post.id, comment)

        assert(result.text == "Hello")
    }

    @Test(expected = PostNotFoundException::class)
    fun shouldThrowIfPostNotFound() {
        WallService.clear()

        val comment = Comment(
            count = 1,
            text = "Hello",
            date = System.currentTimeMillis()
        )

        WallService.createComment(999, comment)
    }
}