package com.gmwapp.slv_qr_card.ProfileFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gmwapp.slv_qr_card.activities.MainActivity
import com.gmwapp.slv_qr_card.databinding.FragmentWithdrawalHistoryBinding
import com.gmwapp.slv_qr_card.helper.Session

class WithdrawalHistoryFragment : Fragment() {


    lateinit var binding: FragmentWithdrawalHistoryBinding
    lateinit var session: Session

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWithdrawalHistoryBinding.inflate(inflater, container, false)
        session = Session(requireContext())

        (activity as MainActivity).binding.bottomNavigation.visibility = View.GONE



        binding.ibBack.setOnClickListener(View.OnClickListener { v: View? ->
            requireActivity().onBackPressed()
        })


        return binding.root

    }


}