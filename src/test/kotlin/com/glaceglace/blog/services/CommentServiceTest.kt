package com.glaceglace.blog.services

import com.glaceglace.blog.exceptions.ErrorParameterException
import com.glaceglace.blog.exceptions.RepositoryException
import com.glaceglace.blog.models.Article
import com.glaceglace.blog.models.Catalogue
import com.glaceglace.blog.models.Comment
import com.glaceglace.blog.models.Tag
import com.glaceglace.blog.repositories.ArticleRepository
import com.glaceglace.blog.repositories.CatalogueRepository
import com.glaceglace.blog.repositories.CommentRepository
import com.glaceglace.blog.repositories.TagRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ExtendWith(SpringExtension::class)
@Transactional
class CommentServiceTest {

    @Autowired
    lateinit var commentService: ICommentService

    @SpyBean
    lateinit var commentRepository: CommentRepository

    @Autowired
    lateinit var articleRepository: ArticleRepository

    @Autowired
    lateinit var catalogueRepository: CatalogueRepository

    @Autowired
    lateinit var tagRepository: TagRepository

    @BeforeEach
    fun setup() {
        reset(commentRepository)
    }

    @Test
    @DisplayName("when create a comment with good parameter, service shall return a right object")
    fun testCreateComment() {
        val saveArticle = prepareArticle()
        val saveResult = commentService.createOneComment("totoComment", saveArticle, "totoFrom", null)
        assertThat(saveResult.content).isEqualTo("totoComment")
        assertThat(commentRepository.findById(saveResult.id).get().content).isEqualTo("totoComment")
        assertThat(commentRepository.findAll().asSequence().toList().size).isEqualTo(1)
    }

    @Test
    @DisplayName("when create a comment with an empty string, then it shall throw an error parameter exception")
    fun testCreateCommentException() {
        val saveArticle = prepareArticle()
        assertThrows<ErrorParameterException> { commentService.createOneComment("", saveArticle, "user", null) }
    }

    @Test
    @DisplayName("when repository failed to create an comment, then it shall throw an repository exception")
    fun tesCreateRepExc() {
        doThrow(RuntimeException("exc test")).whenever(commentRepository).save(any<Comment>())
        val article = prepareArticle()
        val exception = assertThrows<RepositoryException> { commentService.createOneComment("pipaComment", article, "user", null) }
        assertThat(exception.message).isEqualTo("Error from comment repository. Nested exception is { exc test }")
    }

    @Test
    @DisplayName("when get a comment with good parameters, then it shall return the right comment")
    fun testGetComment() {
        val saveArticle = prepareArticle()
        val saveResult = commentService.createOneComment("totoComment", saveArticle, "totoFrom", null)
        val result = commentService.getOneCommentById(saveResult.id)
        assertThat(result.content).isEqualTo("totoComment")
    }


    @Test
    @DisplayName("when get a comment and repository return exception, then it shall throw a exception")
    fun testGetCommentException() {
        doThrow(RuntimeException("exc test")).whenever(commentRepository).findById(any())
        val exception = assertThrows<RepositoryException> { commentService.getOneCommentById(123) }
        assertThat(exception.message).isEqualTo("Error from comment repository. Nested exception is { exc test }")
    }

    @Test
    @DisplayName("when get a comment and and there is no comment found, then it shall throw a exception")
    fun testGetCommentNotFound() {
        val exception = assertThrows<RepositoryException> { commentService.getOneCommentById(123) }
        assertThat(exception.message).isEqualTo("Error from comment repository. Nested exception is { No value present }")
    }

    @Test
    @DisplayName("when modify a comment's name with good parameters, then it shall modify the comment but not create new one")
    fun testModifyTag() {
        val article = prepareArticle()
        val comment = commentRepository.save(Comment("popo", article, "user"))
        val newComment = commentService.modifyOneComment(comment.id, "pipi")
        assertThat(newComment.content).isEqualTo("pipi")
        assertThat(commentRepository.findAll().asSequence().toList().size).isEqualTo(1)
        assertThat(commentRepository.findById(comment.id).get().content).isEqualTo("pipi")
    }

    @Test
    @DisplayName("when modify a comment but id si not correct then it shall throw an exception")
    fun testModifyNoFound() {
        val exception = assertThrows<RepositoryException> { commentService.modifyOneComment(123, "233") }
        assertThat(exception.message).isEqualTo("Error from comment repository. Nested exception is { No value present }")
    }

    @Test
    @DisplayName("when modify a comment, but new content is empty, then it shall throw an exception")
    fun testEmptyName() {
        val article = prepareArticle()
        val comment = commentRepository.save(Comment("", article, "user"))

        assertThrows<ErrorParameterException> { commentService.modifyOneComment(comment.id, "") }
    }

    @Test
    @DisplayName("when delete with good parameters, then it shall be deleted")
    fun testDelete() {
        val article = prepareArticle()
        val comment = commentRepository.save(Comment("papa", article, "usr"))
        assertThat(commentRepository.findAll().asSequence().toList()).isNotEmpty
        commentService.deleteOneComment(comment.id)
        assertThat(commentRepository.findAll().asSequence().toList()).isEmpty()
    }

    @Test
    @DisplayName("when delete with invalid id, then it shall throw an exception")
    fun testDeleteInvalidId() {
        assertThrows<RepositoryException> { commentService.deleteOneComment(1234) }
    }

    @Test
    @DisplayName("when find all comment of an article, it shall return right comments")
    fun testFindAll() {
        val tag = tagRepository.save(Tag("totoTag"))
        val cat = catalogueRepository.save(Catalogue("totoCat"))
        val saveArticle1 = articleRepository.save(Article(cat, "auth1", "title1", "content1", mutableListOf(tag)))
        val saveArticle2 = articleRepository.save(Article(cat, "auth2", "title2", "content2", mutableListOf(tag)))
        commentRepository.save(Comment("toto",saveArticle1,"usr"))
        commentRepository.save(Comment("titi",saveArticle1,"usr"))
        commentRepository.save(Comment("tata",saveArticle2,"usr"))
        commentRepository.save(Comment("tutu",saveArticle2,"usr"))

        val comments1 = commentService.getAllCommentsOfAnArticle(saveArticle1.id)
        assertThat(comments1.size).isEqualTo(2)
        assertThat(comments1.first().content).isEqualTo("toto")
        assertThat(comments1.last().content).isEqualTo("titi")

        val comments2 = commentService.getAllCommentsOfAnArticle(saveArticle2.id)
        assertThat(comments2.size).isEqualTo(2)
        assertThat(comments2.first().content).isEqualTo("tata")
        assertThat(comments2.last().content).isEqualTo("tutu")
    }

    fun prepareArticle(): Article {
        val tag = tagRepository.save(Tag("totoTag"))
        val cat = catalogueRepository.save(Catalogue("totoCat"))
        val saveArticle = articleRepository.save(Article(cat, "auth", "title", "content", mutableListOf(tag)))
        return saveArticle
    }
}