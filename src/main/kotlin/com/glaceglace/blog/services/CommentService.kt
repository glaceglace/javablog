package com.glaceglace.blog.services

import com.glaceglace.blog.exceptions.ErrorParameterException
import com.glaceglace.blog.exceptions.RepositoryException
import com.glaceglace.blog.models.Article
import com.glaceglace.blog.models.Comment
import com.glaceglace.blog.repositories.CommentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface ICommentService {
    fun createOneComment(content: String, article: Article, fromUsr: String, toUser: String?): Comment
    fun deleteOneComment(id: Int)
    fun modifyOneComment(id: Int, newContent: String): Comment
    fun getOneCommentById(id: Int): Comment
    fun getAllCommentsOfAnArticle(articleId: Int): List<Comment>
}

@Service
class CommentService @Autowired constructor(val commentRepository: CommentRepository) : ICommentService {
    override fun getAllCommentsOfAnArticle(articleId: Int): List<Comment> {
        val list: List<Comment>
        try {
            list = commentRepository.findByArticleId(articleId).asSequence().toList()
        } catch (e: Exception) {
            throw RepositoryException("Error from comment repository. Nested exception is { ${e.message} }")
        }
        return list
    }

    override fun createOneComment(content: String, article: Article, fromUsr: String, toUser: String?): Comment {
        if (content.isBlank() || fromUsr.isBlank()) throw ErrorParameterException("Comment parameter must be not null or empty")
        val result: Comment
        try {
            result = commentRepository.save(
                    Comment(content, article, fromUsr, toUser)
            )
        } catch (e: Exception) {
            throw RepositoryException("Error from comment repository. Nested exception is { ${e.message} }")
        }
        return result
    }

    override fun deleteOneComment(id: Int) {
        try {
            commentRepository.deleteById(id)
        } catch (e: Exception) {
            throw RepositoryException("Error from comment repository. Nested exception is { ${e.message} }")
        }
    }

    override fun modifyOneComment(id: Int, newContent: String): Comment {
        if (newContent.isBlank()) throw ErrorParameterException("New comment name can not be empty")
        val comment = getOneCommentById(id)
        comment.content = newContent
        try {
            return commentRepository.save(comment)
        } catch (e: Exception) {
            throw RepositoryException("Error from comment repository. Nested exception is { ${e.message} }")
        }
    }

    override fun getOneCommentById(id: Int): Comment {
        val comment: Comment
        try {
            comment = commentRepository.findById(id).get()
        } catch (e: Exception) {
            throw RepositoryException("Error from comment repository. Nested exception is { ${e.message} }")
        }
        return comment
    }
}