package com.glaceglace.blog.controllers

import com.glaceglace.blog.services.IArticleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("article")
class ArticleController @Autowired constructor(articleService: IArticleService) {

    @GetMapping("{articleId}")
    fun getArticleById(
            @RequestParam articleId:String
    ) {

    }
}
