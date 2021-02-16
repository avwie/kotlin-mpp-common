package nl.rjcoding.common.extensions

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import nl.rjcoding.common.InMemoryBus
import nl.rjcoding.common.ReadBus
import nl.rjcoding.common.ReadBusReceiver
import nl.rjcoding.common.ReadBusSubscription
import nl.rjcoding.ecs.InMemoryECS
import nl.rjcoding.ecs.MultiUserEvent

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

fun ReadBus<MultiUserEvent>.asJson(builderAction: JsonBuilder.() -> Unit): ReadBus<String> {
    val bus = InMemoryBus<String>()
    val json = Json(builderAction = builderAction)

    this.subscribe { event ->
        val data = json.encodeToString(event)
        bus.write(data)
        event
    }
    return bus
}