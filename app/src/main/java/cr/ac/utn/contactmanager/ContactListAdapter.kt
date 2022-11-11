package cr.ac.utn.contactmanager

import Entity.Contact
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import Interface.OnItemClickListener
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class ContactListAdapter (
    private var contactList: List<Contact>,
    val itemClickListener: OnItemClickListener):
    RecyclerView.Adapter<CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup
                                    , viewType: Int):
            CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_contactlist
            , parent, false)
        return CustomViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomViewHolder
                                  , position: Int) {
        var item = contactList[position]
        holder.bind(item, itemClickListener)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }
}

class CustomViewHolder(view: View):
        RecyclerView.ViewHolder (view){

    var txtName = view.findViewById<TextView>(R.id.txtContactName_List)
    var txtEmail = view.findViewById<TextView>(R.id.txtContactEmail_List)
    var txtPhone = view.findViewById<TextView>(R.id.txtContactPhone_List)
    var imgPhoto = view.findViewById<ImageView>(R.id.imgContactPhoto)

     fun bind(item: Contact, clickListener: OnItemClickListener){
         txtName.text = item.FullName
         txtEmail.text = item.Email
         txtPhone.text = item.Phone.toString()
         val photo = BitmapFactory.decodeByteArray(item.Photo
             , 0, item.Photo!!.size)
         imgPhoto.setImageBitmap(photo)

         itemView.setOnClickListener{
             clickListener.onItemClicked(item)
         }
     }
}






