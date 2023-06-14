package livechart.components

import com.raquo.laminar.api.L.{given, *}
import livechart.state.errorVar

def errorMessage() =
  div(
    cls("error-message"),
    child.text <-- errorVar.signal
  )
