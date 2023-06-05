import com.raquo.laminar.api.L._
import org.scalajs.dom

object MyApp {
  def main(args: Array[String]): Unit = {
    val rootElement = dom.document.getElementById("app")
    render(rootElement, app)
  }

  def app: HtmlElement = {
    val counterVar = Var(0)
    div(
      h1("Scala.js with Laminar"),
      button(
        "Click me",
        onClick --> (_ => counterVar.update(_ + 1))
      ),
      child.text <-- counterVar.signal.map(_.toString)
    )
  }
}
