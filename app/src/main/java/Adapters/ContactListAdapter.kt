package Adapters

import Entities.Contact
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import cr.ac.utn.contactmanager.R

class ContactListAdapter(private val context: Context, private val resource: Int, private val datasource: List<Contact>): ArrayAdapter<Contact>(context, resource, datasource){

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return datasource.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.contact_item_list, parent, false)
        val  lbPhone = rowView.findViewById(R.id.lbItemPhone) as TextView
        val  lbFullname = rowView.findViewById(R.id.lbItemFullName) as TextView
        val  lbEmail = rowView.findViewById(R.id.lbItemEmail) as TextView


        val contact = datasource[position] as Contact
        lbPhone.text = contact.phone.toString()
        lbFullname.text = contact.fullName
        lbEmail.text = contact.email



        return rowView
    }
}