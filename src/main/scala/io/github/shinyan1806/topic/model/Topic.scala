package io.github.shinyan1806.topic.model

import slick.lifted.ProvenShape
trait TopicComponent { self: gitbucket.core.model.Profile =>
  import profile.api._

  lazy val topics: TableQuery[TopicsTable] = TableQuery[TopicsTable]

  class TopicsTable(tag: Tag) extends Table[Topic](tag, "TOPIC") {
    val topicId: Rep[Int] = column[Int]("TOPIC_ID", O.PrimaryKey, O.AutoInc)
    val name: Rep[String] = column[String]("NAME", O.Unique)
    def * : ProvenShape[Topic] = (topicId.?, name).mapTo[Topic]
  }
}

case class Topic(topicId: Option[Int], name: String)
