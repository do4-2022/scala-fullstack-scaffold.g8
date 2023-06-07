package livechart.events

import org.scalajs.dom
import com.raquo.laminar.api.L.{given, *}

val onEnterPress =
  onKeyPress.filter(_.keyCode == dom.ext.KeyCode.Enter)
