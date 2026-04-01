package io.github.shinyan1806.topic.model

trait TopicComponent { self: gitbucket.core.model.Profile =>
  import profile.api._

  lazy val topics = TableQuery[TopicsTable]

  class TopicsTable(tag: Tag) extends Table[Topic](tag, "TOPIC") {
    val topicId = column[Int]("TOPIC_ID", O.PrimaryKey, O.AutoInc)
    val name    = column[String]("NAME", O.Unique)
    def * = (topicId.?, name).mapTo[Topic]
  }
}

case class Topic(topicId: Option[Int], name: String)
