package com.apus.verbs

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList

class MyAdapter(val myDataset: ArrayList<Verb>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>(), Filterable {

    private var verbFilter: Filter? = filter
    var filteredList: ArrayList<Verb> = myDataset

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.card_verb, parent, false)){

        private var verbView: TextView? = null
        private var pastView: TextView? = null
        private var pastPView: TextView? = null
        private var spanishView: TextView? = null

        init {
            verbView = itemView.findViewById(R.id.verb)
            pastView = itemView.findViewById(R.id.simple_past)
            pastPView = itemView.findViewById(R.id.past_participle)
            spanishView = itemView.findViewById(R.id.spanish)
        }

        fun bind(verb: Verb) {
            verbView?.text = verb.verb
            pastView?.text = verb.simple_past
            pastPView?.text = verb.past_participle
            spanishView?.text = verb.spanish
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val verb: Verb = filteredList[position]
        holder.bind(verb)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = filteredList.size


    override fun getFilter(): Filter? {
        if(verbFilter == null){
            verbFilter = VerbFilter()
        }
        return verbFilter
    }

    private inner class VerbFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            if (constraint != null && constraint.isNotEmpty()) {
                val tempList = ArrayList<Verb>()

                // search content in friend list
                for (verb in myDataset) {
                    val lowerConstrain = constraint.toString().toLowerCase(Locale.getDefault())
                    val valVerb = verb.verb.toLowerCase(Locale.getDefault()).contains(lowerConstrain)
                    val valPast = verb.simple_past.toLowerCase(Locale.getDefault()).contains(lowerConstrain)
                    val valPastP = verb.past_participle.toLowerCase(Locale.getDefault()).contains(lowerConstrain)
                    val valSpan = verb.spanish.toLowerCase(Locale.getDefault()).contains(lowerConstrain)
                    if (valVerb || valPast || valPastP || valSpan) {
                        tempList.add(verb)
                    }
                }

                filterResults.count = tempList.size
                filterResults.values = tempList
            } else {
                filterResults.count = myDataset.size
                filterResults.values = myDataset
            }

            return filterResults
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            filteredList = results.values as ArrayList<Verb>
            notifyDataSetChanged()
        }
    }

}