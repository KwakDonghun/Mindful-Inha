package com.example.mindfulinha

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.random.Random

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //val quoteTextView = view.findViewById<TextView>(R.id.quoteTextView)
        //val quotes = resources.getStringArray(R.array.quotes)
        //val randomIndex = Random.nextInt(quotes.size)
        //quoteTextView.text = quotes[randomIndex]

        return view
    }
}
