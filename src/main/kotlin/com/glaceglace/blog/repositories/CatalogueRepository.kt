package com.glaceglace.blog.repositories

import com.glaceglace.blog.models.Catalogue
import org.springframework.data.repository.CrudRepository

interface CatalogueRepository : CrudRepository<Catalogue, Int>