package com.glaceglace.blog.services

import com.glaceglace.blog.exceptions.ErrorParameterException
import com.glaceglace.blog.exceptions.RepositoryException
import com.glaceglace.blog.models.Tag
import com.glaceglace.blog.repositories.TagRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface ITagService {
    fun createOneTag(tagName: String): Tag
    fun deleteOneTag(id: Int)
    fun modifyOneTag(id: Int, newName: String): Tag
    fun getOneTagById(id: Int): Tag
    fun getAllTags(): List<Tag>
    fun getTagsByIds(ids: Array<Int>): MutableList<Tag>
}

@Service
class TagService @Autowired constructor(val tagRepository: TagRepository) : ITagService {
    override fun getAllTags(): List<Tag> {
        val list: List<Tag>
        try {
            list = tagRepository.findAll().asSequence().toList()
        } catch (e: Exception) {
            throw RepositoryException("Error from tag repository. Nested exception is { ${e.message} }")
        }
        return list
    }

    override fun getTagsByIds(ids: Array<Int>): MutableList<Tag> {
        val list: MutableList<Tag>
        try {
            list = tagRepository.findAllById(ids.toMutableList()).asSequence().toMutableList()
        } catch (e: Exception) {
            throw RepositoryException("Error from tag repository. Nested exception is { ${e.message} }")
        }
        return list
    }

    override fun createOneTag(tagName: String): Tag {
        if (tagName.isBlank()) throw ErrorParameterException("Tag name must be not null or empty")
        val result: Tag
        try {
            result = tagRepository.save(
                    Tag(tagName)
            )
        } catch (e: Exception) {
            throw RepositoryException("Error from tag repository. Nested exception is { ${e.message} }")
        }
        return result
    }

    override fun deleteOneTag(id: Int) {
        try {
            tagRepository.deleteById(id)
        } catch (e: Exception) {
            throw RepositoryException("Error from tag repository. Nested exception is { ${e.message} }")
        }
    }

    override fun modifyOneTag(id: Int, newName: String): Tag {
        if (newName.isBlank()) throw ErrorParameterException("New tag name can not be empty")
        val tag = getOneTagById(id)
        tag.tagName = newName
        try {
            return tagRepository.save(tag)
        } catch (e: Exception) {
            throw RepositoryException("Error from tag repository. Nested exception is { ${e.message} }")
        }
    }

    override fun getOneTagById(id: Int): Tag {
        val tag: Tag
        try {
            tag = tagRepository.findById(id).get()
        } catch (e: Exception) {
            throw RepositoryException("Error from tag repository. Nested exception is { ${e.message} }")
        }
        return tag
    }
}