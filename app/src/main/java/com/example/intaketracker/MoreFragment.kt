package com.example.intaketracker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class MoreFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_more, container, false)

        val recipeCard = v.findViewById<CardView>(R.id.userProfileCard)
        val bmiCalculatorCard = v.findViewById<CardView>(R.id.bmiCalculatorCard)
        val bodyFatCalculatorCard = v.findViewById<CardView>(R.id.bodyFatCalculatorCard)
        val bmrCalculatorCard = v.findViewById<CardView>(R.id.bmrCalculatorCard)
        val macroCalculatorCard = v.findViewById<CardView>(R.id.macroCalculatorCard)
       // val notificationsCard = v.findViewById<CardView>(R.id.notificationsCard)

        // Add click listeners to the CardView elements
        recipeCard.setOnClickListener {
            val intent = Intent(requireContext(), recyclerview::class.java)
            startActivity(intent)
        }
        bmiCalculatorCard.setOnClickListener {
            val intent = Intent(requireContext(), BMI::class.java)
            startActivity(intent)
        }
        bodyFatCalculatorCard.setOnClickListener {
            val intent = Intent(requireContext(), BodyFat::class.java)
            startActivity(intent)
        }
        bmrCalculatorCard.setOnClickListener {
            val intent = Intent(requireContext(), BMR::class.java)
            startActivity(intent)
        }
        macroCalculatorCard.setOnClickListener{
            val intent = Intent(requireContext(),Macro::class.java)
            startActivity(intent)
        }
//        notificationsCard.setOnClickListener {
//            val intent = Intent(requireContext(),notifs::class.java)
//            startActivity(intent)
//        }
        return v
    }
}