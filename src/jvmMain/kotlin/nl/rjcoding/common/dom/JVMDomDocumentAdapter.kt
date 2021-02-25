package nl.rjcoding.common.dom

import org.w3c.dom.Document
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

class JVMDomDocumentAdapter(val document: Document) : DomDocumentAdapter {

    class JVMDomElement(val inner: Element) : DomDocumentAdapter.Element {
        override fun setAttribute(key: String, value: String) {
            inner.setAttribute(key, value)
        }

        override fun getAttribute(key: String): String? {
            return inner.getAttribute(key)
        }

        override fun appendChild(element: DomDocumentAdapter.Element) {
            inner.appendChild((element as JVMDomElement).inner)
        }

        override fun setText(text: String) {
            inner.textContent = text
        }

        override fun setHTML(html: String) {
            inner.textContent = html
        }
    }

    override fun createElement(name: String): DomDocumentAdapter.Element {
        val element = document.createElement(name)
        return JVMDomElement(element)
    }

    override fun createElementNS(namespace: String, name: String): DomDocumentAdapter.Element {
        val element = document.createElementNS(namespace, name)
        return JVMDomElement(element)
    }

    companion object {
        fun create(): JVMDomDocumentAdapter {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc = builder.newDocument()
            return JVMDomDocumentAdapter(doc)
        }
    }
}