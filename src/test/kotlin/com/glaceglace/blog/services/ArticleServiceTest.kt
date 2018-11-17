package com.glaceglace.blog.services

import com.glaceglace.blog.models.Catalogue
import com.glaceglace.blog.models.Tag
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension::class)
class ArticleServiceTest {
    @Autowired
    lateinit var articleService: IArticleService


    @BeforeEach
    fun setData() {

    }

    @Test
    @DisplayName("when call get by id with good id, then it shall return a good article")
    fun testGetById() {
        val article = articleService.addNewArticle("author", "title", "content", arrayListOf(Tag(0, "tag")), Catalogue(0, "cat"))
        val articleFromDb = articleService.getArticleDetailById(article.id)
        assertThat(article.authorName).isEqualTo("author")
        assertThat(article.id).isNotEqualTo(0)
        assertThat(articleFromDb.id).isEqualTo(article.id)
        assertThat(articleFromDb.authorName).isEqualTo("author")
    }

    @Test
    @DisplayName("when call get all article method ,it shall return all articles")
    fun testGetAll() {
        createListOfArticle()
        val articleList = articleService.getAllArticles()
        assertThat(articleList.size).isEqualTo(5)
    }

    fun createListOfArticle() {

        articleService.addNewArticle("author1", "title1", "content1", arrayListOf(Tag(1, "tag1")), Catalogue(1, "cat1"))
        articleService.addNewArticle("author2", "title2", "content2", arrayListOf(Tag(2, "tag2")), Catalogue(1, "cat2"))
        articleService.addNewArticle("author3", "title3", "content3", arrayListOf(Tag(3, "tag3")), Catalogue(1, "cat3"))
        articleService.addNewArticle("author4", "title4", "content4", arrayListOf(Tag(4, "tag4")), Catalogue(1, "cat4"))
        articleService.addNewArticle("author5", "title5", "content5", arrayListOf(Tag(5, "tag5")), Catalogue(1, "cat5"))


    }
}