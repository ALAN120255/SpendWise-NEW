package com.example.spendwise

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter(
    private var items: List<TransactionItem>,
    private val currencySymbol: String,
    private val onDelete: (TransactionItem) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIcon     = view.findViewById<TextView>(R.id.tv_icon)
        val tvCategory = view.findViewById<TextView>(R.id.tv_category)
        val tvDesc     = view.findViewById<TextView>(R.id.tv_description)
        val tvAmount   = view.findViewById<TextView>(R.id.tv_amount)
        val tvDate     = view.findViewById<TextView>(R.id.tv_date)
        val tvHasPhoto = view.findViewById<TextView?>(R.id.tv_has_photo)
        val ivReceipt  = view.findViewById<ImageView?>(R.id.iv_receipt_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val sdf = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault())

        holder.tvIcon.text     = item.icon
        holder.tvCategory.text = item.category
        holder.tvDesc.text     = item.description.ifBlank { item.category }
        holder.tvDate.text     = sdf.format(Date(item.date))
        holder.tvAmount.text   = "${if (item.isExpense) "-" else "+"}$currencySymbol%.2f".format(item.amount)
        holder.tvAmount.setTextColor(
            if (item.isExpense) 0xFFE57373.toInt() else 0xFF81C784.toInt()
        )

        // Receipt photo toggle
        if (item.photoUrl.isNotEmpty()) {
            holder.tvHasPhoto?.visibility = View.VISIBLE
            holder.ivReceipt?.let { iv ->
                holder.tvHasPhoto?.setOnClickListener {
                    iv.visibility = if (iv.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                    if (iv.visibility == View.VISIBLE) iv.setImageURI(Uri.parse(item.photoUrl))
                }
            }
        } else {
            holder.tvHasPhoto?.visibility = View.GONE
            holder.ivReceipt?.visibility = View.GONE
        }

        holder.itemView.setOnLongClickListener { onDelete(item); true }
    }

    fun updateItems(newItems: List<TransactionItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
