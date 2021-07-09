import org.apache.jena.query.{QueryExecutionFactory, QueryFactory, ResultSet}
import org.apache.jena.rdf.model.{Model, ModelFactory}

object Playground {

  def main(args: Array[String]): Unit = {
    // Configure these: data & query
    val data  = Data("./src/main/resources/data.ttl", Ttl)
    val query = Query("./src/main/resources/select.sparql", Select)

    // Everything else is taken care of here:
    run(data, query)
  }

  private def run(data: Data, query: Query): Unit = {
    val model = ModelFactory.createDefaultModel()
    model.read(data.path, data.lang.name)

    val qf  = QueryFactory.read(query.path)
    val qef = QueryExecutionFactory.create(qf, model)

    query.`type` match {
      case Select =>
        print(qef.execSelect())
      case Construct =>
        print(qef.execConstruct(), data.lang)
      case Ask =>
        println(qef.execAsk())
      case Describe =>
        print(qef.execDescribe(), data.lang)
    }
  }

  def print(model: Model, lang: Lang): Unit = model.write(System.out, lang.name)

  def print(resultSet: ResultSet): Unit = {
    val vars = resultSet.getResultVars
    var i    = 0
    println(DIVIDER)
    resultSet.forEachRemaining { result =>
      println(s"Result: $i")
      vars.forEach { variable =>
        val value = result.get(variable)
        println(s"$variable: $value")
      }
      i += 1
      println(DIVIDER)
    }
  }

  val DIVIDER =
    "-----------------------------------------------------------------------------------------------------------------"

  sealed trait QueryType
  case object Select    extends QueryType
  case object Construct extends QueryType
  case object Ask       extends QueryType
  case object Describe  extends QueryType

  sealed abstract class Lang(val name: String)
  case object Ttl          extends Lang("TTL")
  case object Turtle       extends Lang("TURTLE")
  case object JsonLd       extends Lang("JSON-LD")
  case object N3           extends Lang("N3")
  case object NTriple      extends Lang("N-TRIPLE")
  case object RdfXml       extends Lang("RDF/XML")
  case object RdfXmlAbbrev extends Lang("RDF/XML-ABBREV")

  case class Data(path: String, lang: Lang)
  case class Query(path: String, `type`: QueryType)

}
