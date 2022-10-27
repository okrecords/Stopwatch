package com.okre.stopwatch

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.okre.stopwatch.databinding.FragmentRecyclerBinding

class RecyclerFragment : Fragment() {

    private lateinit var binding: FragmentRecyclerBinding

    companion object {
        fun newInstance() = RecyclerFragment()
        var myData = mutableListOf<RecordData>()
        var mAdapter: RecyclerViewAdapter = RecyclerViewAdapter(myData)
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

        //val manager = LinearLayoutManager(activity as Activity, LinearLayoutManager.VERTICAL, true)
        val manager = LinearLayoutManagerWrapper(requireContext(), LinearLayoutManager.VERTICAL, true)

        with(binding.rv) {
            layoutManager = manager
            adapter = mAdapter
        }
    }
}