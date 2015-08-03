package sample.stream.data_transfer.utils

object UrlExtensions {
  def absoluteUrl(uri: String) = s"http://localhost:8081/$uri"
}
