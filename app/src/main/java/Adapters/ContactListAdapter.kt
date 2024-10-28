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

class ContactListAdapter(private val  context: Context, private val resource: Int,
                         private val datasource: List<Contact>):
    ArrayAdapter<Contact>(context,resource,datasource) {

        private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getCount(): Int {
            return datasource.size
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.contact_item_list, parent, false)
        val lbFullName = rowView.findViewById(R.id.lbItem_FullName) as TextView
        val lbEmail = rowView.findViewById(R.id.lbItem_Email) as TextView
        val lbPhone = rowView.findViewById(R.id.lbItem_Phone) as TextView
        val imgPhoto = rowView.findViewById(R.id.imgItem_Photo) as ImageView

        val contact = datasource[position] as Contact
        lbFullName.text = contact.fullName
        lbPhone.text = contact.phone.toString()
        lbEmail.text = contact.email

        return rowView




        return super.getView(position, convertView, parent)
    }
}