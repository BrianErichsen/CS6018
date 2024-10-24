package com.example

import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import io.ktor.server.resources.delete
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*



fun Application.configureResources() {
    install(Resources)
    routing {
        get<Posts> {
            call.respond(
                newSuspendedTransaction(Dispatchers.IO) {
                    Post
                        .selectAll()
                        .map { GetPostData(it[Post.content], it[Post.time], it[Post.id].value) }
                }
            )
        }
        get<Posts.GetTimeStamp> {
            call.respond(
                newSuspendedTransaction(Dispatchers.IO) {
                    Post
                        .select(Post.time greaterEq it.time)
                        .map { GetPostData(it[Post.content], it[Post.time], it[Post.id].value) }
                }
            )
        }
        get<Posts.GetPostId> {
            call.respond(
                newSuspendedTransaction(Dispatchers.IO) {
                    Post
                        .select( Post.id eq it.id)
                        .map {GetPostData(it[Post.content], it[Post.time], it[Post.id].value) }
                }
            )
        }

        delete<Posts.Delete> {
            val postId = it.id
            val rowsDeleted = newSuspendedTransaction(Dispatchers.IO) {
                Post.deleteWhere { Post.id eq postId }
            }

            if (rowsDeleted > 0) {
                call.respondText("Post with Id $postId has been deleted")
            } else {
                call.respondText("No post found with Id $postId")
            }

        }
        post<Posts> {
            val contentInput = call.receive<PostData>()
            newSuspendedTransaction(Dispatchers.IO, DBConfig.database) {
                Post.insert {
                    it[content] = contentInput.text
                    it[time] = System.currentTimeMillis()
                } get Post.id
            }
            call.respondText("Posted: $contentInput")
        }
    }
}

@Serializable
data class PostData(val text: String)

@Serializable
data class GetPostData(val post: String, val time: Long, val ID: Int)

@Resource("/posts")
class Posts {

    @Resource("{id}/id")
    class GetPostId(val parent: Posts = Posts(), val id: Int)

    @Resource("{id}/delete")
    class Delete(val parent: Posts = Posts(), val id: Int)

    @Resource("{time}/time")
    class GetTimeStamp(val parent: Posts = Posts(), val time: Long)

}