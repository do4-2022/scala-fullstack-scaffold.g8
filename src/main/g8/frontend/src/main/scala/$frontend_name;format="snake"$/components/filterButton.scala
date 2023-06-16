package $frontend_name;format="snake"$.components
import com.raquo.laminar.api.L.{given, *}

import $frontend_name;format="snake"$.state.{Filter, filterVar}

def filterButton(filter: Filter) =
  a(
    cls.toggle("selected") <-- filterVar.signal.map(_ == filter),
    onClick.preventDefault.mapTo(filter) --> filterVar.writer,
    filter.name
  )
