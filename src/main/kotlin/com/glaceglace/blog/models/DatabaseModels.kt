package com.glaceglace.blog.models

import java.sql.Date
import javax.persistence.*

@Entity
class Article(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int,
        val authorName: String,
        val title: String,
        val content: String,
        @ManyToMany(cascade = [CascadeType.ALL]) val tags: List<Tag> = emptyList(),
        @ManyToOne(cascade = [CascadeType.MERGE]) val catalogue: Catalogue,
        val viewNumber: Int = 0,
        val createdTime: Date,
        val editedTiem: Date,
        @OneToMany(cascade = [CascadeType.ALL]) val comments: List<Comment> = emptyList()
)

@Entity
class Tag(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int,
        @Column(unique = true) val tagName: String


)

@Entity
class Catalogue(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int,
        @Column(unique = true) val catalogueName: String
        )

@Entity
class Comment(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int,
        val fromUsr: String,
        @Column(nullable = true) val toUsr: String?,
        val content: String,
        val createdTime: Date
)
