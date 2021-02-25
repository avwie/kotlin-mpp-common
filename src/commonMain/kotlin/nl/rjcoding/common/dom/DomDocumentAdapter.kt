package nl.rjcoding.common.dom

interface DomDocumentAdapter {
    interface Element {
        fun setAttribute(key: String, value: String)
        fun getAttribute(key: String): String?
        fun appendChild(element: Element)

        fun setText(text: String)
        fun setHTML(html: String)
    }

    fun createElement(name: String): Element
    fun createElementNS(namespace: String, name: String): Element
}