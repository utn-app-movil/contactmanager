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


class ContaAdapter(private  val context: Context,private val resource: Int,private val  dataSource: List<Contact>)
    :ArrayAdapter<Contact>(context,resource,dataSource){
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as  LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                var view = inflater.inflate(R.layout.activity_custom_menu, parent,false)
                var viewid = view.findViewById(R.id.CustomLID) as TextView
                var viewname = view.findViewById(R.id.CustomLName) as TextView
                var viewlastname = view.findViewById(R.id.CustomLILastName) as TextView
                var viewPhone = view.findViewById(R.id.CustomLIPhone) as TextView
                var viewEmail = view.findViewById(R.id.CustomLiEmail) as TextView
                var viewAddres = view.findViewById(R.id.CustomLIAdress) as TextView
                var viewimage = view.findViewById(R.id.imageView3) as ImageView



        val Contact = dataSource[position] as Contact
        viewid.text = Contact.id
        viewname.text = Contact.name
        viewlastname.text = Contact.fullName
        viewPhone.text = Contact.toString()
        viewEmail.text = Contact.email
        viewAddres.text = Contact.address

        return view

    }
}   

