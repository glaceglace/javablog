package com.glaceglace.blog.services

import com.glaceglace.blog.models.Article
import com.glaceglace.blog.models.Catalogue
import com.glaceglace.blog.models.Tag
import com.glaceglace.blog.repositories.ArticleRepository
import com.glaceglace.blog.repositories.CatalogueRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Date

@Service
class ArticleService @Autowired constructor(
        val articleRepository: ArticleRepository,
        val catalogueRepository: CatalogueRepository
) : IArticleService {
    override fun getArticleDetailById(articleId: Int): Article {
        return articleRepository.findById(articleId).get()
    }

    override fun deleteOneArticleById(articleId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun addNewArticle(author: String, title: String, content: String, tagList: ArrayList<Tag>,
                               catalogue: Catalogue): Article {
        val article = Article(
                authorName = author,
                title = title,
                content = content,
                tags = tagList,
                catalogue = catalogue,
                createdTime = Date(java.util.Date().time),
                editedTiem = Date(java.util.Date().time),
                id = 0
        )
        return articleRepository.save(article)
    }


    override fun getAllArticles(): List<Article> {
        return articleRepository.findAll().toList()
    }

}

interface IArticleService {
    fun getArticleDetailById(articleId: Int): Article
    fun addNewArticle(author: String, title: String, content: String, tagList: ArrayList<Tag>, catalogue: Catalogue): Article
    fun deleteOneArticleById(articleId: Int)
    fun getAllArticles(): List<Article>


}