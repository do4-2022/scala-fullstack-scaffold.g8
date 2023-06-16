package $frontend_name;format="snake"$.events

def loadTodos: Unit = {
  commandObserver.onNext(Reload)
}
