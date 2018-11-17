package com.glaceglace.blog.repositories

import com.glaceglace.blog.models.Tag
import org.springframework.data.repository.CrudRepository

interface TagRepository : CrudRepository<Tag, Int>