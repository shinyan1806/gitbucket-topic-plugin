package io.github.shinyan1806.topic.model

import slick.lifted.PrimaryKey
import slick.lifted.ProvenShape
trait RepositoryTopicComponent { self: gitbucket.core.model.Profile =>
  import profile.api._

  lazy val repositoryTopics: TableQuery[RepositoryTopicsTable] = TableQuery[RepositoryTopicsTable]

  class RepositoryTopicsTable(tag: Tag) extends Table[RepositoryTopic](tag, "REPOSITORY_TOPIC") {
    val userName: Rep[String] = column[String]("USER_NAME")
    val repositoryName: Rep[String] = column[String]("REPOSITORY_NAME")
    val topicId: Rep[Int] = column[Int]("TOPIC_ID")
    def pk: PrimaryKey = primaryKey("PK_REPOSITORY_TOPIC", (userName, repositoryName, topicId))
    def * : ProvenShape[RepositoryTopic] =
      (userName, repositoryName, topicId).mapTo[RepositoryTopic]
  }
}

case class RepositoryTopic(userName: String, repositoryName: String, topicId: Int)
