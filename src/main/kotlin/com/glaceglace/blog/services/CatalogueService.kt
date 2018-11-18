package com.glaceglace.blog.services

import com.glaceglace.blog.exceptions.ErrorParameterException
import com.glaceglace.blog.exceptions.RepositoryException
import com.glaceglace.blog.models.Catalogue
import com.glaceglace.blog.repositories.CatalogueRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface ICatalogueService {
    fun createOneCatalogue(catName: String): Catalogue
    fun deleteOneCatalogue(id: Int)
    fun modifyOneCatalogue(id: Int, newName: String): Catalogue
    fun getOneCatalogueById(id: Int): Catalogue
    fun getAllCatalogues(): List<Catalogue>
}

@Service
class CatalogueService @Autowired constructor(val catalogueRepository: CatalogueRepository) : ICatalogueService {
    override fun getAllCatalogues(): List<Catalogue> {
        val list: List<Catalogue>
        try {
            list = catalogueRepository.findAll().asSequence().toList()
        } catch (e: Exception) {
            throw RepositoryException("Error from catalogue repository. Nested exception is { ${e.message} }")
        }
        return list
    }

    override fun createOneCatalogue(catName: String): Catalogue {
        if (catName.isBlank()) throw ErrorParameterException("Catalogue name must be not null or empty")
        val result: Catalogue
        try {
            result = catalogueRepository.save(
                    Catalogue(catName)
            )
        } catch (e: Exception) {
            throw RepositoryException("Error from catalogue repository. Nested exception is { ${e.message} }")
        }
        return result
    }

    override fun deleteOneCatalogue(id: Int) {
        try {
            catalogueRepository.deleteById(id)
        } catch (e: Exception) {
            throw RepositoryException("Error from catalogue repository. Nested exception is { ${e.message} }")
        }

    }

    override fun modifyOneCatalogue(id: Int, newName: String): Catalogue {
        if (newName.isBlank()) throw ErrorParameterException("New catalogue name can not be empty")
        val cat = getOneCatalogueById(id)
        cat.catalogueName = newName
        try {
            return catalogueRepository.save(cat)
        } catch (e: Exception) {
            throw RepositoryException("Error from catalogue repository. Nested exception is { ${e.message} }")
        }

    }

    override fun getOneCatalogueById(id: Int): Catalogue {
        val cat: Catalogue
        try {
            cat = catalogueRepository.findById(id).get()
        } catch (e: Exception) {
            throw RepositoryException("Error from catalogue repository. Nested exception is { ${e.message} }")
        }
        return cat
    }
}