package nl.avwie.common.extensions

import nl.avwie.common.ReadBus
import nl.avwie.common.ReadBusReceiver
import nl.avwie.common.ReadBusSubscription

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