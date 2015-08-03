package sample.stream.data_transfer.utils

import akka.util.ByteString

trait CommonMarshallers extends SourceMarshallers {
  implicit val stringMarshaller = marshaller[String](x => ByteString(x))
  implicit val stringUnmarshaller = unmarshaller[String](x => x.utf8String)

  implicit val bytesMarshaller = marshaller[ByteString](identity)
  implicit val bytesUnmarshaller = unmarshaller[ByteString](identity)
}
