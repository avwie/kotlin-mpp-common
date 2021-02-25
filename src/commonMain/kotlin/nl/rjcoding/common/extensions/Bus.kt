package nl.rjcoding.common.extensions

import nl.rjcoding.common.ReadBus
import nl.rjcoding.common.ReadBusReceiver
import nl.rjcoding.common.ReadBusSubscription

fun <Message> ReadBus<Message>.subscribe(block: (Message) -> Message): ReadBusSubscription {
    val receiver = ReadBusReceiver<Message> { block(it) }
    return this.subscribe(receiver)
}

fun <Message> ReadBus<Message>.addLogging() {
    this.subscribe {
        println("[$this] $it")
        it
    }
}