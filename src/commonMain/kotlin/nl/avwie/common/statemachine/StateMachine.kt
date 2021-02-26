package nl.avwie.common.statemachine

class StateMachine<E, C>(initialState: State<E, C>, val context: C) {
    private var currentState = initialState

    init {
        currentState.setStateMachine(this)
    }

    fun transition(newState: State<E, C>) {
        newState.setStateMachine(this)
        currentState.onExit(newState)
        newState.onEnter(currentState)
        currentState = newState
    }

    fun handleEvent(event: E) {
        currentState.handleEvent(event)
    }
}