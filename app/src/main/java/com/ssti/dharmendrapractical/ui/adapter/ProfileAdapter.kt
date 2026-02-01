package com.ssti.dharmendrapractical.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssti.dharmendrapractical.R
import com.ssti.dharmendrapractical.data.local.ProfileEntity
import com.ssti.dharmendrapractical.databinding.ItemProfileBinding
import com.ssti.dharmendrapractical.utils.GeneralFunctions

class ProfileAdapter(
    private val onClick: (position: Int, viewId: Int) -> Unit
) : RecyclerView.Adapter<ProfileAdapter.WorkViewHolder>() {

    private var allWorkPlaceDataResponse = listOf<ProfileEntity>()

    inner class WorkViewHolder(val binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
        val binding = ItemProfileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WorkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkViewHolder, position: Int) {
        val item = allWorkPlaceDataResponse[position]

        holder.binding.apply {
            tvName.text = item.name
            tvAddress.text = item.address.ifEmpty { item.email }
            item.imageUri?.let { image ->
                GeneralFunctions.loadImage(root.context, image, imgProfile)
            }

            imgEdit.setOnClickListener {
                onClick(position, R.id.imgEdit)
            }

            imgDelete.setOnClickListener {
                onClick(position, R.id.imgDelete)
            }
        }
    }

    override fun getItemCount(): Int = allWorkPlaceDataResponse.size

    fun setData(list: List<ProfileEntity>) {
        allWorkPlaceDataResponse = list
        notifyDataSetChanged()
    }
}
