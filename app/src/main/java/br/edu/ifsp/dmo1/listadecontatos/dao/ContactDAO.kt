package br.edu.ifsp.dmo1.listadecontatos.dao

import br.edu.ifsp.dmo1.listadecontatos.model.Contact
import java.util.LinkedList

object ContactDAO {
    private val dataset = LinkedList<Contact>()

    fun insert(contact: Contact) {
        dataset.add(Contact(contact.name, contact.phone))
    }

    fun findAll(): List<Contact> {
        return ArrayList<Contact>(dataset)
    }
}