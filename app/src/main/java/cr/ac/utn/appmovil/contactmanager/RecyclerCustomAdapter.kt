package cr.ac.utn.appmovil.contactmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.OnItemClickListener

class CustomViewHolder (view: View): RecyclerView.ViewHolder(view){
    var txtFullName: TextView = view.findViewById(R.id.txtContactNameItem_recycler)
    var txtAddress: TextView = view.findViewById(R.id.txtAddressItem_recycler)
    var txtPhone: TextView = view.findViewById(R.id.txtPhoneItem_recycler)
    var imgPhoto: ImageView = view.findViewById(R.id.imgPhoto_ItemRecycler)

    fun bind (item: Contact, clickListener: OnItemClickListener){
        txtFullName.text = item.FullName
        txtAddress.text = item.Address
        txtPhone.text = item.Phone.toString()
        imgPhoto.setImageBitmap(item.Photo)

        itemView.setOnClickListener{
            clickListener.onItemClicked(item)
        }
    }
}

class RecyclerCustomAdapter(private var itemList: List<Contact>, val itemClickListener: OnItemClickListener): RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_contact, parent, false)
        return CustomViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        var item = itemList[position]
        holder.bind(item, itemClickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}