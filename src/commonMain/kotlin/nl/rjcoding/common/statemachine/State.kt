package nl.rjcoding.common.statemachine

open class State<E, C> {
    private var stateMachine: StateMachine<E, C>? = null

    val context : C get() = stateMachine!!.context

    fun setStateMachine(stateMachine: StateMachine<E, C>) {
        this.stateMachine = stateMachine
    }

    fun transition(newState: State<E, C>) {
        stateMachine!!.transition(newState)
    }

    open fun onEnter(previousState: State<E, C>) {}
    open fun onExit(newState: State<E, C>) {}
    open fun handleEvent(event: E) {}
}