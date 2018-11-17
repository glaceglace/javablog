package com.glaceglace.blog.repositories

import com.glaceglace.blog.models.Comment
import org.springframework.data.repository.CrudRepository

interface CommentRepository:CrudRepository<Comment,Int>