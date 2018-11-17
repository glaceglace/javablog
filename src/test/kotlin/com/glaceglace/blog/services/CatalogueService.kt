package com.glaceglace.blog.services

import com.glaceglace.blog.models.Catalogue
import org.springframework.stereotype.Service

interface ICatalogueService {
    fun createOneCatalogue(): Catalogue
    fun deleteOneCatalogue(): Catalogue
    fun modifyOneCatalogue(): Catalogue
    fun getOneCatalogue(): Catalogue
}

@Service
class CatalogueService:ICatalogueService {
    override fun createOneCatalogue(): Catalogue {

    }

    override fun deleteOneCatalogue(): Catalogue {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun modifyOneCatalogue(): Catalogue {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getOneCatalogue(): Catalogue {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}