package br.edu.ifsp.dmo1.listadecontatos.view

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo1.listadecontatos.R
import br.edu.ifsp.dmo1.listadecontatos.dao.ContactDAO
import br.edu.ifsp.dmo1.listadecontatos.databinding.ActivityMainBinding
import br.edu.ifsp.dmo1.listadecontatos.databinding.NewContactDialogBinding
import br.edu.ifsp.dmo1.listadecontatos.model.Contact

class MainActivity : AppCompatActivity(), OnItemClickListener {
    private val TAG = "CONTACTS"
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListContactAdapter
    private val listDataSource = ArrayList<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "Executando o onCreate()")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configListView(savedInstanceState)
        configClickListener()
    }

    override fun onStart() {
        Log.v(TAG, "Executando o onStart()")
        super.onStart()
    }

    override fun onResume() {
        Log.v(TAG, "Executando o onResume()")
        super.onResume()
    }

    override fun onPause() {
        Log.v(TAG, "Executando o onPause()")
        super.onPause()
    }

    override fun onStop() {
        Log.v(TAG, "Executando o onStop()")
        super.onStop()
    }

    override fun onRestart() {
        Log.v(TAG, "Executando o onRestart()")
        super.onRestart()
    }

    override fun onDestroy() {
        Log.v(TAG, "Executando o onDestroy()")
        Log.v(TAG, "Lista de contatos que será perdida")

        for (contact in ContactDAO.findAll()) {
            Log.v(TAG, contact.toString())
        }

        super.onDestroy()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedContact = binding.listViewContacts.adapter.getItem(position) as Contact
        val uri = "tel:${selectedContact.phone}"
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse(uri)
        startActivity(intent)
    }

    private fun configClickListener() {
        binding.buttonNewContact.setOnClickListener {
            handleNewContactDialog()
        }
    }

    private fun configListView(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            Log.v(TAG, "Carregando estado dos dados")
            listDataSource.addAll(
                savedInstanceState.getSerializable("contacts") as ArrayList<Contact>
            )
        }

        adapter = ListContactAdapter(this, listDataSource)
        binding.listViewContacts.adapter = adapter
        binding.listViewContacts.onItemClickListener = this
    }

    private fun updateListDataSource() {
        listDataSource.clear()
        listDataSource.addAll(ContactDAO.findAll().sortedBy { it.name })
        Log.v(TAG, "Lista ordenada -> $listDataSource")
        adapter.notifyDataSetChanged()
    }

    private fun handleNewContactDialog() {
        val bindingDialog = NewContactDialogBinding.inflate(layoutInflater)
        val builderDialog = AlertDialog.Builder(this)

        builderDialog.setView(bindingDialog.root)
            .setTitle(R.string.new_contact)
            .setPositiveButton(
                R.string.btn_dialog_save,
                DialogInterface.OnClickListener { dialog, which ->
                    Log.v(TAG, "Salvar contato")
                    ContactDAO.insert(
                        Contact(
                            bindingDialog.editTextName.text.toString(),
                            bindingDialog.editTextPhone.text.toString()
                        )
                    )
                    updateListDataSource()
                    dialog.dismiss()
                }
            )
            .setNegativeButton(
                R.string.btn_dialog_cancel,
                DialogInterface.OnClickListener { dialog, which ->
                    Log.v(TAG, "Cancelar novo contato")
                    dialog.cancel()
                }
            )

        builderDialog.create().show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.v(TAG, "Salvando estado")
        outState.putSerializable("contacts", listDataSource)
    }
}