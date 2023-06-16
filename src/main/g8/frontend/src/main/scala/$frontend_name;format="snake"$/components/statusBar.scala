package $frontend_name;format="snake"$.components
import com.raquo.laminar.api.L.{given, *}

import $frontend_name;format="snake"$.state.{ShowCompleted, itemsVar, filters}
import $frontend_name;format="snake"$.events.commandObserver

def statusBar =
  footerTag(
    hideIfNoItems,
    cls("footer"),
    span(
      cls("todo-count"),
      child.text <-- itemsVar.signal
        .map(_.count(!_.completed))
        .map(pluralize(_, "item left", "items left"))
    ),
    ul(
      cls("filters"),
      filters.map(filter => li(filterButton(filter)))
    ),
  )

def pluralize(num: Int, singular: String, plural: String): String =
  s"\$num \${if (num == 1) singular else plural}"
