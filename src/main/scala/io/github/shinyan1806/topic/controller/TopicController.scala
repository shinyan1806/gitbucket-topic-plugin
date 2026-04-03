package io.github.shinyan1806.topic.controller

import gitbucket.core.controller.ControllerBase
import gitbucket.core.service.AccountService
import gitbucket.core.service.RepositoryService
import gitbucket.core.util.Implicits._
import gitbucket.core.util.WritableUsersAuthenticator
import io.github.shinyan1806.topic.html.topics
import io.github.shinyan1806.topic.model.Topic
import io.github.shinyan1806.topic.service.TopicService

class TopicController
  extends ControllerBase
  with RepositoryService
  with AccountService
  with TopicService
  with WritableUsersAuthenticator
  with JsonHelper {

  val PageSize: Int = 25

  get("/topics") {
    val selectedTopic = params.get("topic").filter(_.nonEmpty)
    val page = params.getOrElse("page", "1").toIntOption.getOrElse(1).max(1)

    val allRepos = getVisibleRepositories(context.loginAccount, withoutPhysicalInfo = true)
    val filteredRepos = selectedTopic match {
      case Some(topicName) =>
        val matched = getRepositoryNamesByTopic(topicName).toSet
        allRepos.filter(
          r => matched.contains((r.owner, r.name)),
        )
      case None =>
        allRepos
    }

    val totalCount = filteredRepos.size
    val pagedRepos = filteredRepos.slice((page - 1) * PageSize, page * PageSize)

    val repoTopics: Map[(String, String), Seq[Topic]] =
      pagedRepos
        .map(
          r => (r.owner, r.name) -> getTopics(r.owner, r.name),
        )
        .toMap

    val allTopics = getAllTopics()

    topics(pagedRepos, repoTopics, allTopics, selectedTopic, page, totalCount, PageSize)(context)
  }

  get("/topics/search") {
    contentType = formats("json")
    val q = params.getOrElse("q", "").trim
    if (q.isEmpty) {
      "[]"
    } else {
      val results = searchTopics(q)
      toJsonStringArray(results.map(_.name))
    }
  }

  post("/topics/add/:owner/:repository")(writableUsersOnly {
    repository =>
      val topicName = params("topic").trim.toLowerCase
      if (topicName.nonEmpty) {
        addTopic(repository.owner, repository.name, topicName)
      }
      redirect("/topics")
  })

  post("/topics/remove/:owner/:repository")(writableUsersOnly {
    repository =>
      val topicName = params("topic").trim
      removeTopic(repository.owner, repository.name, topicName)
      redirect("/topics")
  })
}
