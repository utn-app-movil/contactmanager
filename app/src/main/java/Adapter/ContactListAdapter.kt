package Adapter

import Entities.Contact
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import cr.ac.utn.contactmanager.R

class ContactListAdapter(
    private val context: Context,
    private var resource: Int,
    private var datasource: List<Contact>
) : ArrayAdapter<Contact>(context, resource, datasource) {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return datasource.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.contact_item_list, parent, false)
        val lbFullName = rowView.findViewById(R.id.txt_fullName) as TextView
        val lbEmail = rowView.findViewById(R.id.txt_email) as TextView
        val lbPhone = rowView.findViewById(R.id.txt_phone) as TextView
        val imgPhoto = rowView.findViewById(R.id.imgitem_foto) as ImageView

        val contact = datasource[position] as Contact
        lbFullName.text = contact.fullName
        lbEmail.text = contact.email
        lbPhone.text = contact.phone.toString()

        return rowView
    }
}