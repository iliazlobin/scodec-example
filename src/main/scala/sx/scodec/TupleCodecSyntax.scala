package sx.scodec

import shapeless._
import scodec.Codec

object TupleCodecSyntax {
  implicit class Tuple2CodecSupportSyntax[A, B](val self: Codec[(A, B)]) {
    def thlist: Codec[A :: B :: HNil] = self.xmap(t => t._1 :: (t._2 :: HNil), h => (h.head, h.tail.head))
  }
  implicit class Tuple3CodecSupportSyntax[A, B, C](val self: Codec[((A, B), C)]) {
    def thlist: Codec[A :: B :: C :: HNil] = self.xmap(t => t._1._1 :: (t._1._2 :: (t._2 :: HNil)), h => ((h.head, h.tail.head), h.tail.tail.head))
  }
}
