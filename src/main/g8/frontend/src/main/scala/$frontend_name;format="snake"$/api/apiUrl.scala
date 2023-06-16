package $frontend_name;format="snake"$.api
import scala.scalajs.js

private val env_api_url = js.Dynamic.global.window.VITE_API_URL

val API_URL = {
  if (env_api_url != null) {
    env_api_url.asInstanceOf[String]
  } else {
    "http://localhost:3000"
  }
}
