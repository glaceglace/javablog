package com.glaceglace.blog.services

import com.glaceglace.blog.exceptions.ErrorParameterException
import com.glaceglace.blog.exceptions.RepositoryException
import com.glaceglace.blog.models.Article
import com.glaceglace.blog.repositories.ArticleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ArticleService @Autowired constructor(
        val articleRepository: ArticleRepository,
        val catalogueService: ICatalogueService,
        val tagService: ITagService,
        val commentService: ICommentService
) : IArticleService {
    override fun getArticleDetailById(articleId: Int): Article {
        return articleRepository.findById(articleId).get()
    }

    override fun deleteOneArticleById(articleId: Int) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun addNewArticle(
            author: String,
            title: String,
            content: String,
            tagIds: MutableList<Int>,
            catalogueId: Int
    ): Article {
        if (author.isBlank() || title.isBlank() || content.isBlank()) throw ErrorParameterException("Parameter can not be blank")
        val cat = catalogueService.getOneCatalogueById(catalogueId)
        val tags = tagService.getTagsByIds(tagIds.toTypedArray())
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
}