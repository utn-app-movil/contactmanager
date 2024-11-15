package cr.ac.utn.appmovil.contactmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import cr.ac.utn.appmovil.identities.Contact

class ContactAdapter(private val mCtx: Context, private val resource:Int, private val dataSource: List<Contact>): //BaseAdapter()

    ArrayAdapter<Contact>(mCtx, resource, dataSource)
{
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val rowView = inflater.inflate(R.layout.list_item_contact, parent, false)
        val txtContactNameItem = rowView.findViewById(R.id.txtContactNameItem) as TextView
        val txtAddressItem = rowView.findViewById(R.id.txtAddressItem) as TextView
        val txtPhoneItem = rowView.findViewById(R.id.txtPhoneItem) as TextView
        val imgPhotoItem = rowView.findViewById(R.id.imgPhotoItem) as ImageView

        val contact = dataSource[position] as Contact

        txtContactNameItem.text = contact.FullName
        txtAddressItem.text = contact.Address
        txtPhoneItem.text = contact.Phone.toString()
        imgPhotoItem.setImageBitmap(contact.Photo)

        return rowView
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}