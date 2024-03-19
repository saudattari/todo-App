package com.example.todoapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.todoapp.databinding.FragmentAddPopUp2Binding

class AddPopUp2 : DialogFragment() {
    private lateinit var binding: FragmentAddPopUp2Binding
    private lateinit var listener: ShareData

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as ShareData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPopUp2Binding.inflate(inflater,container,false)
        binding.add.setOnClickListener {
            val data = binding.addNote.text.toString()
            if(data.isNotEmpty()){
                listener.passData(data)
                binding.addNote.setText("")
            }
            else{
                Toast.makeText(requireContext(), "Fields Can't be empty", Toast.LENGTH_SHORT).show()
            }
        }
        isCancelable = false
        binding.close.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    companion object {

    }
    interface ShareData{
        fun passData(data: String)
    }
}