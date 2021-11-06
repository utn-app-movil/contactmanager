package cr.ac.utn.appmovil.interfaces

import cr.ac.utn.appmovil.identities.Contact

interface OnItemClickListener {
    fun onItemClicked (contact: Contact)
}