package com.okre.stopwatch

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.okre.stopwatch.databinding.FragmentRecyclerBinding

class RecyclerFragment : Fragment() {

    private lateinit var binding: FragmentRecyclerBinding

    companion object {
        fun newInstance() = RecyclerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = LinearLayoutManager(activity as Activity, LinearLayoutManager.VERTICAL, false)

        with(binding.rv) {
            layoutManager = manager
            //adapter = RecyclerViewAdapter
        }
    }


}