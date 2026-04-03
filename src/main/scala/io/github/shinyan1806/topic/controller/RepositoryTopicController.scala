package io.github.shinyan1806.topic.controller

import gitbucket.core.controller.ControllerBase
import gitbucket.core.service.AccountService
import gitbucket.core.service.RepositoryService
import gitbucket.core.util.Implicits._
import gitbucket.core.util.ReferrerAuthenticator
import gitbucket.core.util.WritableUsersAuthenticator
import io.github.shinyan1806.topic.service.TopicService

class RepositoryTopicController
  extends ControllerBase
  with TopicService
  with AccountService
  with RepositoryService
  with ReferrerAuthenticator
  with WritableUsersAuthenticator
  with JsonHelper {

  get("/:owner/:repository/topics")(referrersOnly {
    repository =>
      contentType = formats("json")
      val topics = getTopics(repository.owner, repository.name)
      toJsonTopicObjectArray(topics.map(_.name))
  })

  post("/:owner/:repository/topics/add")(writableUsersOnly {
    repository =>
      contentType = formats("json")
      val topicName = params("topic").trim.toLowerCase
      if (topicName.nonEmpty) {
        addTopic(repository.owner, repository.name, topicName)
        val topics = getTopics(repository.owner, repository.name)
        toJsonTopicObjectArray(topics.map(_.name))
      } else {
        halt(400, """{"error":"Topic name is empty"}""")
      }
  })

  post("/:owner/:repository/topics/remove")(writableUsersOnly {
    repository =>
      contentType = formats("json")
      val topicName = params("topic").trim
      if (topicName.nonEmpty) {
        removeTopic(repository.owner, repository.name, topicName)
      }
      val topics = getTopics(repository.owner, repository.name)
      toJsonTopicObjectArray(topics.map(_.name))
  })
}
