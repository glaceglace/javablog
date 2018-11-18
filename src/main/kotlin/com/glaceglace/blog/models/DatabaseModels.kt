package com.glaceglace.blog.models

import java.sql.Date
import javax.persistence.*

@Entity
class Article(
        @OneToMany(cascade = [CascadeType.ALL]) var comments: List<Comment> = emptyList(),
        var authorName: String,
        var title: String,
        var content: String,
        @ManyToMany(cascade = [CascadeType.ALL]) var tags: List<Tag> = emptyList(),
        @ManyToOne(cascade = [CascadeType.MERGE]) var catalogue: Catalogue,
        var viewNumber: Int = 0,
        var createdTime: Date,
        var editedTiem: Date,
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
        var createdTime: Date,
        var fromUsr: String,
        @Column(nullable = true) var toUsr: String?,
        var content: String,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int = 0
)
