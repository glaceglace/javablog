package com.glaceglace.blog.services

import com.glaceglace.blog.exceptions.ErrorParameterException
import com.glaceglace.blog.exceptions.RepositoryException
import com.glaceglace.blog.models.Catalogue
import com.glaceglace.blog.repositories.CatalogueRepository
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
internal class CatalogueServiceTest {

    @SpyBean
    lateinit var catalogueRepository: CatalogueRepository

    @Autowired
    lateinit var catalogueService: ICatalogueService

    @BeforeEach
    fun setup() {
        reset(catalogueRepository)
    }

    @Test
    @DisplayName("when create a catalogue with good parameter, service shall return a right object")
    fun testCreateCatalogue() {
        val saveResult = catalogueService.createOneCatalogue("totoCat")
        assertThat(saveResult.catalogueName).isEqualTo("totoCat")
        assertThat(catalogueRepository.findById(saveResult.id).get().catalogueName).isEqualTo("totoCat")
        assertThat(catalogueRepository.findAll().asSequence().toList().size).isEqualTo(1)
    }

    @Test
    @DisplayName("when create a catalogue with an empty string, then it shall throw an error parameter exception")
    fun testCreateCatException() {
        assertThrows<ErrorParameterException> { catalogueService.createOneCatalogue("") }
    }

    @Test
    @DisplayName("when repository failed to create an catalogue, then it shall throw an repository exception")
    fun tesCreateRepExc() {
        doThrow(RuntimeException("exc test")).whenever(catalogueRepository).save(any<Catalogue>())
        val exception = assertThrows<RepositoryException> { catalogueService.createOneCatalogue("pipaCat") }
        assertThat(exception.message).isEqualTo("Error from catalogue repository. Nested exception is { exc test }")
    }

    @Test
    @DisplayName("when get a catalogue with good parameters, then it shall return the right catalogue")
    fun testGetCatalogue() {
        val cat = catalogueRepository.save(Catalogue("pipiCat"))
        val result = catalogueService.getOneCatalogueById(cat.id)
        assertThat(result.catalogueName).isEqualTo("pipiCat")
    }

    @Test
    @DisplayName("when get a catalogue and repository return exception, then it shall throw a exception")
    fun testGetCatException() {
        doThrow(RuntimeException("exc test")).whenever(catalogueRepository).findById(any())
        val exception = assertThrows<RepositoryException> { catalogueService.getOneCatalogueById(123) }
        assertThat(exception.message).isEqualTo("Error from catalogue repository. Nested exception is { exc test }")
    }

    @Test
    @DisplayName("when get a catalogue and and there is no catalogue found, then it shall throw a exception")
    fun testGetCatNotFound() {
        val exception = assertThrows<RepositoryException> { catalogueService.getOneCatalogueById(123) }
        assertThat(exception.message).isEqualTo("Error from catalogue repository. Nested exception is { No value present }")
    }

    @Test
    @DisplayName("when modify a catalogue's name with good parameters, then it shall modify the catalogue but not create new one")
    fun testModifyCat() {
        val cat = catalogueRepository.save(Catalogue("popo"))
        val newCat = catalogueService.modifyOneCatalogue(cat.id, "pipi")
        assertThat(newCat.catalogueName).isEqualTo("pipi")
        assertThat(catalogueRepository.findAll().asSequence().toList().size).isEqualTo(1)
        assertThat(catalogueRepository.findById(cat.id).get().catalogueName).isEqualTo("pipi")
    }

    @Test
    @DisplayName("when modify a catalogue but id si not correct then it shall throw an exception")
    fun testModifyNoFound() {
        val exception = assertThrows<RepositoryException> { catalogueService.modifyOneCatalogue(123, "233") }
        assertThat(exception.message).isEqualTo("Error from catalogue repository. Nested exception is { No value present }")
    }

    @Test
    @DisplayName("when modify a catalogue, but new name is empty, then it shall throw an exception")
    fun testEmptyName() {
        val cat = catalogueRepository.save(Catalogue("popo"))

        assertThrows<ErrorParameterException> { catalogueService.modifyOneCatalogue(cat.id, "") }
    }

    @Test
    @DisplayName("when delete with good parameters, then it shall be deleted")
    fun testDelete() {
        val cat = catalogueRepository.save(Catalogue("papa"))
        assertThat(catalogueRepository.findAll().asSequence().toList()).isNotEmpty
        catalogueService.deleteOneCatalogue(cat.id)
        assertThat(catalogueRepository.findAll().asSequence().toList()).isEmpty()
    }

    @Test
    @DisplayName("when delete with invalid id, then it shall throw an exception")
    fun testDeleteInvalidId() {
        assertThrows<RepositoryException> { catalogueService.deleteOneCatalogue(1234) }
    }

    @Test
    @DisplayName("when find all catalogue with ok, then it shall return a list")
    fun testFindAll() {
        val cat = catalogueRepository.saveAll(listOf(Catalogue("papa"), Catalogue("pipi"),
                Catalogue("popo")))
        val response = catalogueService.getAllCatalogues()

        assertThat(response.size).isEqualTo(3)
        assertThat(response.map { it.catalogueName }).contains("papa")
        assertThat(response.map { it.catalogueName }).contains("pipi")
        assertThat(response.map { it.catalogueName }).contains("popo")
        println(response.map { it.id })
    }
}