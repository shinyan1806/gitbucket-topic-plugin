name := "gitbucket-topic-plugin"
organization := "io.github.shinyan1806"
version := "1.0.0"
scalaVersion := "2.13.18"
gitbucketVersion := "4.46.0"

ThisBuild / semanticdbEnabled := true

scalacOptions ++= Seq(
  "-deprecation",
  "-Xsource:3-cross",
  "-Wunused:imports",
)

addCommandAlias("fmt", ";scalafmtAll;scalafmtSbt")
addCommandAlias("fmtCheck", ";scalafmtCheckAll;scalafmtSbtCheck")
addCommandAlias("fix", ";scalafixAll")
addCommandAlias("fixCheck", ";scalafixAll --check")
addCommandAlias("tidy", ";fmt;reload;fix")
addCommandAlias("lint", ";fmtCheck;fixCheck;compile")
addCommandAlias("codeCheck", ";reload;tidy;lint")
