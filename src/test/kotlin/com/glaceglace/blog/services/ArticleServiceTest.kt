package com.glaceglace.blog.services

import com.glaceglace.blog.exceptions.ErrorParameterException
import com.glaceglace.blog.exceptions.RepositoryException
import com.glaceglace.blog.models.Catalogue
import com.glaceglace.blog.models.Tag
import com.glaceglace.blog.repositories.CatalogueRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doThrow
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


    @BeforeEach
    fun setData() {

    }

    @Test
    @DisplayName("when call get by id with good id, then it shall return a good article")
    fun testGetArticleById() {
        val article = articleService.addNewArticle("author", "title", "content", arrayListOf(Tag("tag", 0)), Catalogue("cat", 0))
        val articleFromDb = articleService.getArticleDetailById(article.id)
        assertThat(article.authorName).isEqualTo("author")
        assertThat(article.id).isNotEqualTo(0)
        assertThat(articleFromDb.id).isEqualTo(article.id)
        assertThat(articleFromDb.authorName).isEqualTo("author")
    }



}