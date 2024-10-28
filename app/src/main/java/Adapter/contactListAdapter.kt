package Adapter

import Entities.Contact
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import cr.ac.utn.R

class contactListAdapter(private val context: Context,
                         private val resource: Int,
                         private val datasource: List<Contact>):
    ArrayAdapter<Contact>(context, resource, datasource) {
    private val inflater: LayoutInflater = context.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return datasource.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView = inflater.inflate(R.layout.activity_custom_list, parent, false)
        var lbid = rowView.findViewById(R.id.customListID) as TextView
        var lbname = rowView.findViewById(R.id.customListname) as TextView
        var lbfullname = rowView.findViewById(R.id.customListfName) as TextView
        var lbPhone = rowView.findViewById(R.id.customListphone) as TextView
        var lbEmail = rowView.findViewById(R.id.customListEmail) as TextView
        var lbAddress = rowView.findViewById(R.id.customListIDAddress) as TextView
        var lbImageView = rowView.findViewById(R.id.customImage) as ImageView

        val contact = datasource[position] as Contact
        lbid.text = contact.id
        lbname.text = contact.name
        lbfullname.text = contact.fullName
        lbPhone.text = contact.phone.toString()
        lbEmail.text = contact.email
        lbAddress.text = contact.address

        return rowView
    }
}