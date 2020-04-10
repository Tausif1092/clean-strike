package readers

import scala.util.Try

class ActionFileInputReader extends InputReader[Try[List[String]]] {

  override def read(path: String): Try[List[String]] =
    Try(scala.io.Source.fromFile(path).getLines().toList)
}
