package livechart.components
import com.raquo.laminar.api.L.{given, *}

import livechart.state.itemsVar

def hideIfNoItems: Mod[HtmlElement] =
  display <-- itemsVar.signal.map { items =>
    if (items.nonEmpty) "" else "none"
  }
