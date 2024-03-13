package com.example.intaketracker

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeAdapter(
    private val context: Context,
    private val recipeModalArrayList: ArrayList<RecipeModal>,
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<RecipeAdapter.ViewHolder>(), View.OnClickListener {
    private var selectedRecipe: RecipeModal? = null
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        val viewHolder = ViewHolder(view)
        view.setOnClickListener(this)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: RecipeModal = recipeModalArrayList[position]
        holder.recipeName.text = model.getRecipe_name()
        holder.recipecalories.text = context.getString(R.string.recipeadapter) + model.getRecipe_calories()

        // Convert the ByteArray to a Bitmap and set it in the ImageView
        val bitmap = BitmapFactory.decodeByteArray(model.getRecipe_image(), 0, model.getRecipe_image().size)
        holder.recipeImage.setImageBitmap(bitmap)

        holder.itemView.tag = position
        holder.itemView.setOnClickListener {
            selectedRecipe = model // Set the selected recipe
            itemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return recipeModalArrayList.size
    }

    override fun onClick(view: View) {
        val position = view.tag as Int
        itemClickListener.onItemClick(position)
    }

    // View holder class for initializing your views such as TextView and ImageView.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImage: ImageView
        val recipeName: TextView
        val recipecalories: TextView

        init {
            recipeImage = itemView.findViewById(R.id.idIVCourseImage)
            recipeName = itemView.findViewById(R.id.idrecipeName)
            recipecalories = itemView.findViewById(R.id.idcal)
        }
    }
}
