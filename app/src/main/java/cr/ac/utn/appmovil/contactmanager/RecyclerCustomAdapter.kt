package cr.ac.utn.appmovil.contactmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.identities.Contact

internal class RecyclerCustomAdapter(private var itemList: List<Contact>): RecyclerView.Adapter<RecyclerCustomAdapter.CustomViewHolder>() {
    internal inner class CustomViewHolder (view: View): RecyclerView.ViewHolder(view){
        var txtFullName: TextView = view.findViewById(R.id.txtContactNameItem_recycler)
        var txtAddress: TextView = view.findViewById(R.id.txtAddressItem_recycler)
        var txtPhone: TextView = view.findViewById(R.id.txtPhoneItem_recycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_contact, parent, false)
        return CustomViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        var item = itemList[position]
        holder.txtFullName.text = item.FullName
        holder.txtAddress.text = item.Address
        holder.txtPhone.text = item.Phone.toString()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}