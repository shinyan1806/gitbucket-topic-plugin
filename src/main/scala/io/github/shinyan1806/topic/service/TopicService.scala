package io.github.shinyan1806.topic.service

import io.github.shinyan1806.topic.model.{RepositoryTopic, Topic}
import io.github.shinyan1806.topic.model.Profile._
import profile.blockingApi._

trait TopicService {

  def getTopics(userName: String, repositoryName: String)
               (implicit session: Session): Seq[Topic] =
    (repositoryTopics.filter(rt =>
      rt.userName === userName.bind && rt.repositoryName === repositoryName.bind
    ) join topics on (_.topicId === _.topicId))
      .map(_._2)
      .sortBy(_.name)
      .list

  def getAllTopics()(implicit session: Session): Seq[Topic] =
    topics.sortBy(_.name).list

  def searchTopics(query: String)(implicit session: Session): Seq[Topic] =
    topics.filter(_.name like s"%${query.toLowerCase}%")
      .sortBy(_.name)
      .list

  def getRepositoryNamesByTopic(topicName: String)
                                (implicit session: Session): Seq[(String, String)] =
    (repositoryTopics join topics on (_.topicId === _.topicId))
      .filter(_._2.name === topicName.bind)
      .map(x => (x._1.userName, x._1.repositoryName))
      .list

  def addTopic(userName: String, repositoryName: String, topicName: String)
              (implicit session: Session): Unit = {
    val topicId: Int =
      topics.filter(_.name === topicName.bind).map(_.topicId).firstOption
        .getOrElse {
          (topics returning topics.map(_.topicId) += Topic(None, topicName)).run
        }
    val exists =
      repositoryTopics.filter(rt =>
        rt.userName === userName.bind &&
        rt.repositoryName === repositoryName.bind &&
        rt.topicId === topicId.bind
      ).exists.run
    if (!exists) {
      repositoryTopics += RepositoryTopic(userName, repositoryName, topicId)
    }
  }

  def removeTopic(userName: String, repositoryName: String, topicName: String)
                 (implicit session: Session): Unit =
    topics.filter(_.name === topicName.bind).map(_.topicId).firstOption.foreach { topicId =>
      repositoryTopics.filter(rt =>
        rt.userName === userName.bind &&
        rt.repositoryName === repositoryName.bind &&
        rt.topicId === topicId.bind
      ).delete
    }
}
