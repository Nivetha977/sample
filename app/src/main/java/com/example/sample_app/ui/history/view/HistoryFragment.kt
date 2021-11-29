package com.example.sample_app.ui.history.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sample_app.R
import com.example.sample_app.app.App
import com.example.sample_app.databinding.FragmentHistoryBinding
import com.example.sample_app.ui.history.adapter.HistoryAdapter


class HistoryFragment : Fragment() {
    private lateinit var storiesAdapter: HistoryAdapter
    private lateinit var binding: FragmentHistoryBinding

    var resId = R.anim.recyclerview_layout_animation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        var animation = AnimationUtils.loadLayoutAnimation(requireContext(), resId)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvAddress.layoutManager = layoutManager
        binding.rvAddress.layoutAnimation = animation
        storiesAdapter = HistoryAdapter(this,App.mAppDatabase!!.getAddressDao().getAddress())
        binding.rvAddress.adapter = storiesAdapter
        binding.rvAddress.scheduleLayoutAnimation()

        return binding.root


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {

            }
    }
}