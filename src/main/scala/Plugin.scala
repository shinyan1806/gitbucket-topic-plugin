import io.github.gitbucket.solidbase.migration.LiquibaseMigration
import io.github.gitbucket.solidbase.model.Version

class Plugin extends gitbucket.core.plugin.Plugin {
  override val pluginId: String = "topic"
  override val pluginName: String = "Topic Plugin"
  override val description: String = "Repository topic feature for GitBucket."
  override val versions: List[Version] = List(
    new Version("1.0.0",
      new LiquibaseMigration("update/gitbucket-topic_1.0.xml")
    )
  )
}
