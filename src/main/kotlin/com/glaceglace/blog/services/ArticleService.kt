package com.glaceglace.blog.services

import com.glaceglace.blog.exceptions.ArticleNotFoundException
import com.glaceglace.blog.exceptions.ErrorParameterException
import com.glaceglace.blog.exceptions.RepositoryException
import com.glaceglace.blog.models.Article
import com.glaceglace.blog.models.Catalogue
import com.glaceglace.blog.models.Tag
import com.glaceglace.blog.repositories.ArticleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Date
import java.util.*

@Service
class ArticleService @Autowired constructor(
        val articleRepository: ArticleRepository,
        val catalogueService: ICatalogueService,
        val tagService: ITagService,
        val commentService: ICommentService
) : IArticleService {
    override fun getArticleDetailById(articleId: Int): Article {
        try {
            return articleRepository.findById(articleId).get()
        } catch (e: NoSuchElementException) {
            throw ArticleNotFoundException()
        } catch (e: Exception) {
            throw RepositoryException("Error from article repository, nested exception is { ${e.message} }")
        }
    }

    override fun deleteOneArticleById(articleId: Int) {
        try {
            return articleRepository.deleteById(articleId)
        } catch (e: NoSuchElementException) {
            throw ArticleNotFoundException()
        } catch (e: Exception) {
            throw RepositoryException("Error from article repository, nested exception is { ${e.message} }")
        }
    }

    override fun addNewArticle(
            author: String,
            title: String,
            content: String,
            tagIds: MutableList<Int>,
            catalogueId: Int
    ): Article {
        if (author.isBlank() || title.isBlank() || content.isBlank()) throw ErrorParameterException("Parameter can not be blank")
        val cat: Catalogue
        val tags: MutableList<Tag>
        try {
            cat = catalogueService.getOneCatalogueById(catalogueId)
            tags = tagService.getTagsByIds(tagIds.toTypedArray())
        } catch (e: Exception) {
            throw  RepositoryException("Error from Tag/Catalogue repository. Nested exception is { ${e.message} }")
        }

        val article = Article(
                catalogue = cat,
                authorName = author,
                title = title,
                content = content,
                tags = tags
        )
        try {
            return articleRepository.save(article)
        } catch (e: Exception) {
            throw RepositoryException("Error from article repository. Nested exception is { ${e.message} }")
        }

    }

    override fun modifyOneArticle(articleId: Int, newContent: String?, newTitle: String?, newCat: Catalogue?, newTags: MutableList<Tag>?): Article {
        var article: Article? = null
        try {
            article = articleRepository.findById(articleId).get()
            if (!newContent.isNullOrBlank()) article.content = newContent!!

            if (!newTitle.isNullOrBlank()) article.title = newTitle!!
            if (newCat != null) article.catalogue = newCat
            if (newTags != null) article.tags = newTags
            if (!newContent.isNullOrBlank() || !newTitle.isNullOrBlank()) article.editedTime = Date(java.util.Date().time)
        } catch (e: Exception) {
            ArticleNotFoundException("Can't find article, nested exception is { ${e.message} }")
        }

        try {
            return articleRepository.save(article!!)
        } catch (e: NullPointerException) {
            throw ArticleNotFoundException("Article is null")
        } catch (e: java.lang.Exception) {
            throw RepositoryException("Error from articleRepository, nested exception is { ${e.message} }")
        }

    }

    override fun getAllArticles(): MutableList<Article> {
        try {
            return articleRepository.findAll().toMutableList()
        } catch (e: Exception) {
            throw RepositoryException("Error from article repository. Nested exception is { ${e.message} }")
        }
    }
}

interface IArticleService {
    fun getArticleDetailById(articleId: Int): Article
    fun addNewArticle(author: String, title: String, content: String, tagIds: MutableList<Int>, catalogueId: Int): Article
    fun deleteOneArticleById(articleId: Int)
    fun getAllArticles(): MutableList<Article>
    fun modifyOneArticle(articleId: Int, newContent: String?, newTitle: String?, newCat: Catalogue?, newTags: MutableList<Tag>?): Article
}