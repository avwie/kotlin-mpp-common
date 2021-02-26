package nl.avwie.common

interface ReadBus<Message> {
    fun subscribe(receiver: ReadBusReceiver<Message>): ReadBusSubscription
    fun unsubscribe(receiver: ReadBusReceiver<Message>)
}

fun interface WriteBus<Message> {
    fun write(message: Message)
}

fun interface ReadBusReceiver<Message> {
    fun receive(message: Message): Message
}

fun interface ReadBusSubscription {
    fun unsubscribe()
}

class InMemoryBus<Message> : ReadBus<Message>, WriteBus<Message> {
    private val receivers: MutableList<ReadBusReceiver<Message>> = mutableListOf()

    override fun write(message: Message) {
        receivers.fold(message) { msg, rcv -> rcv.receive(msg) }
    }

    override fun subscribe(receiver: ReadBusReceiver<Message>): ReadBusSubscription {
        receivers.add(receiver)
        return ReadBusSubscription {
            receivers.remove(receiver)
        }
    }

    override fun unsubscribe(receiver: ReadBusReceiver<Message>) {
        receivers.remove(receiver)
    }
}