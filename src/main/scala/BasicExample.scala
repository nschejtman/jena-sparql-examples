import org.apache.jena.rdf.model.ModelFactory

object BasicExample {
  def main(args: Array[String]): Unit = {
    val people          = "http://people.com"
    val characteristics = "http://characteristics.com"

    /**
      * A model is basically a graph. You can create a dataset with multiple graphs but you must have at least one
      * default graph
      */
    val model = ModelFactory.createDefaultModel()

    val jorge          = model.createResource(s"$people/Jorge")
    val favoriteNumber = model.createProperty(s"$characteristics/favorite-number")
    val three          = model.createLiteral("3")
    jorge.addProperty(favoriteNumber, three)

    model.write(System.out, "JSON-LD")
  }
}
