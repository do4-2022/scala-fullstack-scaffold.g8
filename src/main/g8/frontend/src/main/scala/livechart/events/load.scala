package livechart.events

def loadTodos: Unit = {
  commandObserver.onNext(Reload)
}
