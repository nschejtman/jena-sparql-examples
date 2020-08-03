import java.io.FileWriter

import org.apache.jena.rdf.model.ModelFactory
object WriteJsonLD {

  def main(args: Array[String]): Unit = {
    val people          = "http://people.com"
    val characteristics = "http://characteristics.com"

    val model = ModelFactory.createDefaultModel()

    val gustavo = model.createResource(s"$people/Gustavo")
    val martin  = model.createResource(s"$people/Martin")
    val nicolas = model.createResource(s"$people/Nicolas")

    val role = model.createProperty(s"$characteristics/role")
    val nick = model.createProperty(s"$characteristics/nick")

    gustavo
      .addLiteral(role, "Manager")
      .addLiteral(nick, "Gus")

    martin
      .addLiteral(role, "Manager")
      .addLiteral(nick, "Gute")

    nicolas
      .addLiteral(role, "Engineer")
      .addLiteral(nick, "Jorge")

    model.write(new FileWriter("./amf-team.jsonld"), "JSON-LD")
    model.write(System.out, "JSON-LD")
  }

}
