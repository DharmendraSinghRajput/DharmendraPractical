package com.ssti.dharmendrapractical.ui.adapter

import android.R.attr.onClick
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssti.dharmendrapractical.data.model.CartsResponse
import com.ssti.dharmendrapractical.databinding.CartItemsBinding
import com.ssti.dharmendrapractical.utils.GeneralFunctions

class CartAdapter(
    private val onItemClick: (CartsResponse.Cart.Product) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var productList = listOf<CartsResponse.Cart.Product>()

    inner class CartViewHolder(
        val binding: CartItemsBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = productList[position]

        holder.binding.apply {
            GeneralFunctions.loadImage(root.context, product.thumbnail, imgCard)
            tvTitle.text = product.title
            tvPrice.text = product.price.toString()
        }

        holder.itemView.setOnClickListener {
            onItemClick(product) // ✅ correct
        }
    }

    override fun getItemCount(): Int = productList.size

    fun setData(products: List<CartsResponse.Cart.Product>) {
        productList = products
        notifyDataSetChanged()
    }
}
