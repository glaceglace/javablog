package com.glaceglace.blog.repositories

import com.glaceglace.blog.models.Article
import org.springframework.data.repository.CrudRepository

interface ArticleRepository : CrudRepository<Article, Int>
