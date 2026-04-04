import gitbucket.core.controller.Context
import gitbucket.core.controller.ControllerBase
import gitbucket.core.plugin._
import gitbucket.core.service.RepositoryService.RepositoryInfo
import io.github.gitbucket.solidbase.migration.LiquibaseMigration
import io.github.gitbucket.solidbase.model.Version
import io.github.shinyan1806.topic.controller.RepositoryTopicController
import io.github.shinyan1806.topic.controller.TopicController
import io.github.shinyan1806.topic.html.topicsdropdown
import play.twirl.api.Html

class Plugin extends gitbucket.core.plugin.Plugin {
  override val pluginId: String = "topic"
  override val pluginName: String = "Topic Plugin"
  override val description: String = "Repository topic feature for GitBucket."
  override val versions: List[Version] = List(
    new Version("1.0.0", new LiquibaseMigration("update/gitbucket-topic_1.0.xml")),
  )
  override val controllers: Seq[(String, ControllerBase)] = Seq(
    "/topics" -> new TopicController(),
    "/*" -> new RepositoryTopicController(),
  )
  override val globalMenus: Seq[Context => Option[Link]] = Seq(
    (_: Context) => Some(Link("topics", "Repositories by Topic", "topics")),
  )
  override val assetsMappings: Seq[(String, String)] = Seq(
    "/topic" -> "/gitbucket/topic/assets",
  )
  override val repositoryHeaders: Seq[(RepositoryInfo, Context) => Option[Html]] = Seq(
    (repository, context) => Some(topicsdropdown(repository)(context)),
  )
}
