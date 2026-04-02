package io.github.shinyan1806.topic.controller

import gitbucket.core.controller.ControllerBase
import gitbucket.core.model.Profile.profile.api._
import gitbucket.core.service.{AccountService, RepositoryService}
import gitbucket.core.util.Implicits._
import gitbucket.core.util.WritableUsersAuthenticator
import io.github.shinyan1806.topic.html.topics
import io.github.shinyan1806.topic.model.Profile._
import io.github.shinyan1806.topic.model.Topic
import io.github.shinyan1806.topic.service.TopicService

class TopicController extends ControllerBase
	with RepositoryService
	with AccountService
	with TopicService
	with WritableUsersAuthenticator {

	val PageSize = 25

	private def toJsonStringArray(values: Seq[String]): String =
		values.map(v => "\"" + escapeJson(v) + "\"").mkString("[", ",", "]")

	private def escapeJson(value: String): String = {
		val sb = new StringBuilder(value.length)
		value.foreach {
			case '"'  => sb.append("\\\"")
			case '\\' => sb.append("\\\\")
			case '\b' => sb.append("\\b")
			case '\f' => sb.append("\\f")
			case '\n' => sb.append("\\n")
			case '\r' => sb.append("\\r")
			case '\t' => sb.append("\\t")
			case c if c < ' ' => sb.append(f"\\u${c.toInt}%04x")
			case c => sb.append(c)
		}
		sb.toString()
	}

	get("/topics") {
		val selectedTopic = params.get("topic").filter(_.nonEmpty)
		val page = params.getOrElse("page", "1").toIntOption.getOrElse(1).max(1)

		val allRepos = getVisibleRepositories(context.loginAccount, withoutPhysicalInfo = true)
		val filteredRepos = selectedTopic match {
		case Some(topicName) =>
			val matched = getRepositoryNamesByTopic(topicName).toSet
			allRepos.filter(r => matched.contains((r.owner, r.name)))
		case None =>
			allRepos
		}

		val totalCount = filteredRepos.size
		val pagedRepos = filteredRepos.slice((page - 1) * PageSize, page * PageSize)

		val repoTopics: Map[(String, String), Seq[Topic]] =
		pagedRepos.map(r => (r.owner, r.name) -> getTopics(r.owner, r.name)).toMap

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

	post("/topics/add/:owner/:repository")(writableUsersOnly { repository =>
		val topicName = params("topic").trim.toLowerCase
		if (topicName.nonEmpty) {
		addTopic(repository.owner, repository.name, topicName)
		}
		redirect("/topics")
	})

	post("/topics/remove/:owner/:repository")(writableUsersOnly { repository =>
		val topicName = params("topic").trim
		removeTopic(repository.owner, repository.name, topicName)
		redirect("/topics")
	})
}
