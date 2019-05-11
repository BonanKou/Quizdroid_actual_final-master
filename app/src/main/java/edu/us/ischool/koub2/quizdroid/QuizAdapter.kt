package edu.us.ischool.koub2.quizdroid

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class QuizAdapter(var listOfCategory: List<String>): RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {
    var onQuizClickedListener: ((position: Int, name: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewHolderType: Int): QuizViewHolder {
        // Creates ViewHolder to hold reference of the views
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.test, parent, false)
        return QuizViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        // Size of items to load
        return listOfCategory.size
    }

    override fun onBindViewHolder(viewHolder: QuizViewHolder, position: Int) {
        // Sets data on view
        viewHolder.bindView(listOfCategory[position], position)
    }

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(quizName: String, position: Int) {
            val button = itemView.findViewById<Button>(R.id.category)
            button.text = quizName

            button.setOnClickListener {
                onQuizClickedListener?.invoke(position, quizName)
            }
        }
    }

    fun updateList(newListOfCategory: List<String>) {
        this.listOfCategory = newListOfCategory

        // Call this when you change the data of Recycler View to refresh the items
        notifyDataSetChanged()
    }
}