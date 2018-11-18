package com.glaceglace.blog.exceptions

open class BlogException(override val message: String, val errorCode: Int) : RuntimeException(message)

class ErrorParameterException(message: String) : BlogException(message, 1001)
class RepositoryException(message: String = "Error from repository communication") : BlogException(message, 1002)