package com.glaceglace.blog.services

import com.glaceglace.blog.exceptions.ErrorParameterException
import com.glaceglace.blog.exceptions.RepositoryException
import com.glaceglace.blog.models.Tag
import com.glaceglace.blog.repositories.TagRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.reset
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
internal class TagServiceTest {

    @SpyBean
    lateinit var tagRepository: TagRepository

    @Autowired
    lateinit var tagService: ITagService

    @BeforeEach
    fun setup() {
        reset(tagRepository)
    }

    @Test
    @DisplayName("when create a tag with good parameter, service shall return a right object")
    fun testCreateTag() {
        val saveResult = tagService.createOneTag("totoTag")
        assertThat(saveResult.tagName).isEqualTo("totoTag")
        assertThat(tagRepository.findById(saveResult.id).get().tagName).isEqualTo("totoTag")
        assertThat(tagRepository.findAll().asSequence().toList().size).isEqualTo(1)
    }

    @Test
    @DisplayName("when create a tag with an empty string, then it shall throw an error parameter exception")
    fun testCreateTagException() {
        assertThrows<ErrorParameterException> { tagService.createOneTag("") }
    }

    @Test
    @DisplayName("when repository failed to create an tag, then it shall throw an repository exception")
    fun tesCreateRepExc() {
        doThrow(RuntimeException("exc test")).whenever(tagRepository).save(any<Tag>())
        val exception = assertThrows<RepositoryException> { tagService.createOneTag("pipaTag") }
        assertThat(exception.message).isEqualTo("Error from tag repository. Nested exception is { exc test }")
    }

    @Test
    @DisplayName("when get a tag with good parameters, then it shall return the right tag")
    fun testGetTag() {
        val tag = tagRepository.save(Tag("pipiTag"))
        val result = tagService.getOneTagById(tag.id)
        assertThat(result.tagName).isEqualTo("pipiTag")
    }

    @Test
    @DisplayName("when get a tag and repository return exception, then it shall throw a exception")
    fun testGetTagException() {
        doThrow(RuntimeException("exc test")).whenever(tagRepository).findById(any())
        val exception = assertThrows<RepositoryException> { tagService.getOneTagById(123) }
        assertThat(exception.message).isEqualTo("Error from tag repository. Nested exception is { exc test }")
    }

    @Test
    @DisplayName("when get a tag and and there is no tag found, then it shall throw a exception")
    fun testGetTagNotFound() {
        val exception = assertThrows<RepositoryException> { tagService.getOneTagById(123) }
        assertThat(exception.message).isEqualTo("Error from tag repository. Nested exception is { No value present }")
    }

    @Test
    @DisplayName("when modify a tag's name with good parameters, then it shall modify the tag but not create new one")
    fun testModifyTag() {
        val tag = tagRepository.save(Tag("popo"))
        val newTag = tagService.modifyOneTag(tag.id, "pipi")
        assertThat(newTag.tagName).isEqualTo("pipi")
        assertThat(tagRepository.findAll().asSequence().toList().size).isEqualTo(1)
        assertThat(tagRepository.findById(tag.id).get().tagName).isEqualTo("pipi")
    }

    @Test
    @DisplayName("when modify a tag but id si not correct then it shall throw an exception")
    fun testModifyNoFound() {
        val exception = assertThrows<RepositoryException> { tagService.modifyOneTag(123, "233") }
        assertThat(exception.message).isEqualTo("Error from tag repository. Nested exception is { No value present }")
    }

    @Test
    @DisplayName("when modify a tag, but new name is empty, then it shall throw an exception")
    fun testEmptyName() {
        val tag = tagRepository.save(Tag("popo"))

        assertThrows<ErrorParameterException> { tagService.modifyOneTag(tag.id, "") }
    }

    @Test
    @DisplayName("when delete with good parameters, then it shall be deleted")
    fun testDelete() {
        val tag = tagRepository.save(Tag("papa"))
        assertThat(tagRepository.findAll().asSequence().toList()).isNotEmpty
        tagService.deleteOneTag(tag.id)
        assertThat(tagRepository.findAll().asSequence().toList()).isEmpty()
    }

    @Test
    @DisplayName("when delete with invalid id, then it shall throw an exception")
    fun testDeleteInvalidId() {
        assertThrows<RepositoryException> { tagService.deleteOneTag(1234) }
    }

    @Test
    @DisplayName("when find all tag with ok, then it shall return a list")
    fun testFindAll() {
        val tag = tagRepository.saveAll(listOf(Tag("papa"), Tag("pipi"),
                Tag("popo")))
        val response = tagService.getAllTags()

        assertThat(response.size).isEqualTo(3)
        assertThat(response.map { it.tagName }).contains("papa")
        assertThat(response.map { it.tagName }).contains("pipi")
        assertThat(response.map { it.tagName }).contains("popo")
        println(response.map { it.id })
    }
}