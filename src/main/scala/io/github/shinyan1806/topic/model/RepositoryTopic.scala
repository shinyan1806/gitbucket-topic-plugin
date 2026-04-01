package io.github.shinyan1806.topic.model

trait RepositoryTopicComponent { self: gitbucket.core.model.Profile =>
  import profile.api._

  lazy val repositoryTopics = TableQuery[RepositoryTopicsTable]

  class RepositoryTopicsTable(tag: Tag) extends Table[RepositoryTopic](tag, "REPOSITORY_TOPIC") {
    val userName       = column[String]("USER_NAME")
    val repositoryName = column[String]("REPOSITORY_NAME")
    val topicId        = column[Int]("TOPIC_ID")
    def pk = primaryKey("PK_REPOSITORY_TOPIC", (userName, repositoryName, topicId))
    def * = (userName, repositoryName, topicId).mapTo[RepositoryTopic]
  }
}

case class RepositoryTopic(userName: String, repositoryName: String, topicId: Int)
