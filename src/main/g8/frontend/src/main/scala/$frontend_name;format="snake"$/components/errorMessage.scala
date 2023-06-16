package $frontend_name;format="snake"$.components

import com.raquo.laminar.api.L.{given, *}
import $frontend_name;format="snake"$.state.errorVar

def errorMessage() =
  div(
    cls("error-message"),
    child.text <-- errorVar.signal
  )
