package com.sun.android.ex19

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.sun.android.R
import com.sun.android.adapters.ContactListAdapter
import com.sun.android.databinding.ActivityContactListBinding
import com.sun.android.models.ContactData

class ContactListActivity : AppCompatActivity() {
    private val binding by lazy { ActivityContactListBinding.inflate(layoutInflater) }

    private lateinit var contactPermissionLauncher : ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        contactPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                fetchContact()
            } else {
                if (shouldShowRequestPermissionRationale(READ_CONTACTS_PERMISSION_NAME))
                {
                    contactPermissionLauncher.launch(READ_CONTACTS_PERMISSION_NAME)
                } else {
                    userResponse()
                }
            }
        }

        contactPermissionLauncher.launch(READ_CONTACTS_PERMISSION_NAME)
    }

    private fun userResponse() {
        Toast.makeText(this, R.string.permission_denied_message, Toast.LENGTH_SHORT).show()
    }

    private fun fetchContact() {
        val contactList = getContacts()

        val adapter = ContactListAdapter().apply {
            setContactList(contactList)
        }
        binding.rvContact.adapter = adapter
    }

    private fun getContacts() : MutableList<ContactData> {
        val contacts = mutableListOf<ContactData>()

        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER)

        contentResolver.query(uri, projection,null,null,null)?.use {
            if (it.count > 0) {
                val displayNameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val phoneNumberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                while (it.moveToNext()) {
                    val contactName = it.getString(displayNameIndex)
                    val phoneNumber = it.getString(phoneNumberIndex)

                    contacts.add(ContactData(contactName, phoneNumber))
                }
            }
        }

        return contacts
    }

    companion object {
        private val READ_CONTACTS_PERMISSION_NAME = Manifest.permission.READ_CONTACTS
    }
}
