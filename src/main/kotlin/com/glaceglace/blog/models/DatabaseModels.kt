package com.glaceglace.blog.models

import java.sql.Date
import javax.persistence.*

@Entity
class Article(
        @ManyToOne(cascade = [CascadeType.ALL]) var catalogue: Catalogue,
        var authorName: String,
        var title: String,
        var content: String,
        @ManyToMany(cascade = [CascadeType.ALL]) var tags: MutableList<Tag> = mutableListOf(),
        var viewNumber: Int = 0,
        @OneToMany(cascade = [CascadeType.ALL]) var comments: MutableList<Comment> = mutableListOf(),
        var editedTiem: Date = Date(java.util.Date().time),
        var createdTime: Date = Date(java.util.Date().time),
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int = 0
)

@Entity
class Tag(
        @Column(unique = true) var tagName: String,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int = 0

)

@Entity
class Catalogue(
        @Column(unique = true) var catalogueName: String,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int = 0
)

@Entity
class Comment(
        var content: String,
        @ManyToOne(cascade = [CascadeType.ALL])  var article: Article,
        var fromUsr: String,
        @Column(nullable = true) var toUsr: String? = null,
        var createdTime: Date = Date(java.util.Date().time),
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int = 0
)
