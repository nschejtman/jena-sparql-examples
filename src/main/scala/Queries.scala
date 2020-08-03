import java.io.FileReader

import org.apache.jena.query.{QueryExecutionFactory, ResultSet}
import org.apache.jena.rdf.model.{Model, ModelFactory}
object Queries {

  def main(args: Array[String]): Unit = {
    val model = ModelFactory.createDefaultModel()
    model.read(new FileReader("./amf-team.jsonld"), null, "JSON-LD")

    select(model)
    construct(model)
    ask(model)
    describe(model)
  }

  def select(model: Model): Unit = {
    println("SELECT ----------------")
    val query =
      """
        | SELECT ?person
        | WHERE {
        |   ?person ?property "Manager"
        | }
        |
        |""".stripMargin

    val result: ResultSet = QueryExecutionFactory
      .create(query, model)
      .execSelect()

    result.forEachRemaining { result =>
      println(result.get("person"))
    }
  }

  def construct(model: Model): Unit = {
    println("CONSTRUCT ----------------")
    val query =
      """
        | PREFIX characteristics: <http://characteristics.com/>
        | CONSTRUCT {
        |   ?engineer characteristics:memberOf "Technical staff".
        |   ?manager  characteristics:memberOf "Management staff"
        | }
        | WHERE {
        |   ?manager  characteristics:role "Manager".
        |   ?engineer characteristics:role "Engineer"
        | }
        |
        |""".stripMargin

    val result: Model = QueryExecutionFactory
      .create(query, model)
      .execConstruct()

    result.write(System.out, "JSON-LD")
  }

  def ask(model: Model): Unit = {
    println("ASK ----------------")
    val query =
      """
        | PREFIX characteristics: <http://characteristics.com/>
        | ASK {
        |   ?person characteristics:nick "Gute"
        | }
        |""".stripMargin

    val result: Boolean = QueryExecutionFactory
      .create(query, model)
      .execAsk()

    println(result)
  }

  def describe(model: Model): Unit = {
    println("DESCRIBE 1 ----------------")
    val query1 =
      """
        | DESCRIBE <http://people.com/Nicolas>
        |""".stripMargin

    val result1: Model = QueryExecutionFactory
      .create(query1, model)
      .execDescribe()

    result1.write(System.out, "JSON-LD")

    println("DESCRIBE 2 ----------------")
    val query2 =
      """
        | DESCRIBE ?person
        | WHERE {
        |   ?person <http://characteristics.com/nick> "Gus"
        | }
        |""".stripMargin

    val result2: Model = QueryExecutionFactory
      .create(query2, model)
      .execDescribe()

    result2.write(System.out, "JSON-LD")
  }

}
