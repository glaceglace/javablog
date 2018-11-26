package com.glaceglace.blog.services

import com.glaceglace.blog.exceptions.ArticleNotFoundException
import com.glaceglace.blog.exceptions.ErrorParameterException
import com.glaceglace.blog.exceptions.RepositoryException
import com.glaceglace.blog.models.Article
import com.glaceglace.blog.models.Catalogue
import com.glaceglace.blog.models.Tag
import com.glaceglace.blog.repositories.ArticleRepository
import com.glaceglace.blog.repositories.CatalogueRepository
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
@Transactional
@ExtendWith(SpringExtension::class)
class ArticleServiceTest {
    @Autowired
    lateinit var articleService: IArticleService

    @SpyBean
    lateinit var catalogueRepository: CatalogueRepository

    @Autowired
    lateinit var tagRepository: TagRepository

    @Autowired
    lateinit var articleRepository: ArticleRepository

    @BeforeEach
    fun setData() {
        reset(catalogueRepository)
    }

    @Test
    @DisplayName("when call get by id with good id, then it shall return a good article")
    fun testGetArticleById() {
        val article = prepareArticle()
        val articleFromDb = articleService.getArticleDetailById(article.id)
        assertThat(article.authorName).isEqualTo("auth")
        assertThat(article.id).isNotEqualTo(0)
        assertThat(articleFromDb.id).isEqualTo(article.id)
        assertThat(articleFromDb.authorName).isEqualTo("auth")
    }

    @Test
    @DisplayName("when call get by id but there is no article found, then it shall throw an exception")
    fun testGetException() {
        val exception = assertThrows<ArticleNotFoundException> { articleService.getArticleDetailById(123) }
        assertThat(exception.message).isEqualTo("Can not find this article")
    }

    @Test
    @DisplayName("when add an article with successful, then it shall return the article")
    fun testAdd() {
        val tag1 = tagRepository.save(Tag("totoTag"))
        val tag2 = tagRepository.save(Tag("titiTag"))
        val tag3 = tagRepository.save(Tag("tataTag"))
        val cat = catalogueRepository.save(Catalogue("totoCat"))
        val article = articleService.addNewArticle("totoauth", "tototitle", "totocontent", mutableListOf(tag1.id, tag2.id, tag3.id), cat.id)
        val articleFromRep = articleRepository.findById(article.id).get()
        assertThat(articleFromRep.authorName).isEqualTo("totoauth")
        assertThat(articleFromRep.tags.size).isEqualTo(3)
        assertThat(articleFromRep.tags.map { it.tagName }).containsAll(listOf("totoTag", "titiTag", "tataTag"))
    }

    @Test
    @DisplayName("when add an article but tag service has an problem, then it shall throw an exception")
    fun testAddException1() {
        doThrow(RuntimeException("todo")).whenever(catalogueRepository).findById(any())
        val exception = assertThrows<RepositoryException> { articleService.addNewArticle("toot", "titi", "tata", mutableListOf(), 123) }
        assertThat(exception.message).isEqualTo("Error from Tag/Catalogue repository. Nested exception is { Error from catalogue repository. Nested exception is { todo } }")
    }

    @Test
    @DisplayName("when add article but content is blank it shall throw an exception")
    fun testAddBlank() {
        val exception = assertThrows<ErrorParameterException> { articleService.addNewArticle("", "titi", "tata", mutableListOf(), 123) }
        assertThat(exception.message).isEqualTo("Parameter can not be blank")

    }

    @Test
    @DisplayName("when delete a existed article with success, it shall do the right thing")
    fun testDelete() {
        val article = prepareArticle()
        assertThat(articleRepository.findAll().asSequence().toList().size).isEqualTo(1)
        articleService.deleteOneArticleById(article.id)
        assertThat(articleRepository.findAll().asSequence().toList().size).isEqualTo(0)

    }

    @Test
    @DisplayName("when delete a non existed article, it shall throw not found exception")
    fun testDeleteNotFound() {
        assertThrows<RepositoryException> { articleService.deleteOneArticleById(123) }
    }

    fun prepareArticle(): Article {
        val tag = tagRepository.save(Tag("totoTag"))
        val cat = catalogueRepository.save(Catalogue("totoCat"))
        return articleRepository.save(Article(cat, "auth", "title", "content", mutableListOf(tag)))
    }
}