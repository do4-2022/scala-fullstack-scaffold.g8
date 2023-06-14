package livechart.components
import com.raquo.laminar.api.L.{given, *}

import livechart.state.{Filter, filterVar}

def filterButton(filter: Filter) =
  a(
    cls.toggle("selected") <-- filterVar.signal.map(_ == filter),
    onClick.preventDefault.mapTo(filter) --> filterVar.writer,
    filter.name
  )
