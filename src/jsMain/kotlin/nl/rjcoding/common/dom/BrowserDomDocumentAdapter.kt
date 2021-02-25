package nl.rjcoding.common.dom

import kotlinx.browser.document
import org.w3c.dom.Element

class BrowserDomDocumentAdapter : DomDocumentAdapter {

    class BrowserDomElement(val inner: Element) : DomDocumentAdapter.Element {
        override fun setAttribute(key: String, value: String) {
            inner.setAttribute(key, value)
        }

        override fun getAttribute(key: String): String? {
            return inner.getAttribute(key)
        }

        override fun appendChild(element: DomDocumentAdapter.Element) {
            inner.appendChild((element as BrowserDomElement).inner)
        }

        override fun setText(text: String) {
            inner.textContent = text
        }

        override fun setHTML(html: String) {
            inner.innerHTML = html
        }
    }

    override fun createElement(name: String): DomDocumentAdapter.Element {
        val element = document.createElement(name)
        return BrowserDomElement(element)
    }

    override fun createElementNS(namespace: String, name: String): DomDocumentAdapter.Element {
        val element = document.createElementNS(namespace, name)
        return BrowserDomElement(element)
    }
}