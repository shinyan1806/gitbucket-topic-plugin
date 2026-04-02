package io.github.shinyan1806.topic.controller

trait JsonHelper {

  protected def escapeJson(value: String): String = {
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
      case c    => sb.append(c)
    }
    sb.toString()
  }

  protected def toJsonStringArray(values: Seq[String]): String =
    values.map(v => "\"" + escapeJson(v) + "\"").mkString("[", ",", "]")

  protected def toJsonTopicObjectArray(names: Seq[String]): String =
    names.map(n => "{\"name\":\"" + escapeJson(n) + "\"}").mkString("[", ",", "]")
}
