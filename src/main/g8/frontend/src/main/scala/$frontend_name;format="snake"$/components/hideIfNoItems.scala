package $frontend_name;format="snake"$.components
import com.raquo.laminar.api.L.{given, *}

import $frontend_name;format="snake"$.state.itemsVar

def hideIfNoItems: Mod[HtmlElement] =
  display <-- itemsVar.signal.map { items =>
    if (items.nonEmpty) "" else "none"
  }
