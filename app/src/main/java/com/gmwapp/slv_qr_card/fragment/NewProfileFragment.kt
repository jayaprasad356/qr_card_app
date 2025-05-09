package com.gmwapp.slv_qr_card.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.gmwapp.slv_qr_card.ProfileFragment.ExtraIncomeFragment
import com.gmwapp.slv_qr_card.ProfileFragment.InviteFragment
import com.gmwapp.slv_qr_card.ProfileFragment.LevelIncomeFragment
import com.gmwapp.slv_qr_card.ProfileFragment.MyBankFragment
import com.gmwapp.slv_qr_card.ProfileFragment.MyReferFragment
import com.gmwapp.slv_qr_card.ProfileFragment.SetPasswordFragment
import com.gmwapp.slv_qr_card.ProfileFragment.TransactionHistoryFragment
import com.gmwapp.slv_qr_card.ProfileFragment.UpdateBankFragment
import com.gmwapp.slv_qr_card.ProfileFragment.UpdateProfileFragment
import com.gmwapp.slv_qr_card.ProfileFragment.WithdrawalFragment
import com.gmwapp.slv_qr_card.ProfileFragment.WithdrawalHistoryFragment
import com.gmwapp.slv_qr_card.R
import com.gmwapp.slv_qr_card.helper.Constant
import com.gmwapp.slv_qr_card.helper.Session
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zoho.commons.InitConfig
import com.zoho.livechat.android.listeners.InitListener
import com.zoho.salesiqembed.ZohoSalesIQ

class NewProfileFragment : Fragment() {
    var session: Session? = null
    var activity: Activity? = null


    var tvName: TextView? = null
    var tvMobile: TextView? = null

//    var rlwithdrawhistory: RelativeLayout? = null
    var rlTransectionHistory: RelativeLayout? = null
    var rlApplyLeave: RelativeLayout? = null
    var rlUpdateprofile: RelativeLayout? = null
    var rlChangepassword: RelativeLayout? = null
//    var rlmyBank: RelativeLayout? = null
    var rlInvite: RelativeLayout? = null
    var rlRefers: RelativeLayout? = null
    var rlGrowWithUs: RelativeLayout? = null
    var rlLevelIncome: RelativeLayout? = null
    var rlLogout: RelativeLayout? = null
    var rlWithdraw: RelativeLayout? = null
    var rlUpdateBank: RelativeLayout? = null
    var tvPlanActivate: TextView? = null
    var btnFloatingAction: FloatingActionButton? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_new_profile, container, false)
        activity = requireActivity()
        session = Session(activity)

        tvName = root.findViewById(R.id.tvName)
        tvMobile = root.findViewById(R.id.tvMobile)

        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val inviteFragment = InviteFragment()
        val withdrawalFragment = WithdrawalFragment()
        val myReferFragment = MyReferFragment()
        val withdrawalHistoryFragment = WithdrawalHistoryFragment()
        val transactionHistoryFragment = TransactionHistoryFragment()
        val myProfileFragment = UpdateProfileFragment()
        val setPasswordFragment = SetPasswordFragment()
        val updateBankFragment = UpdateBankFragment()
        val levelIncomeFragment = LevelIncomeFragment()
        val extraIncomeFragment = ExtraIncomeFragment()
        val myBankFragment = MyBankFragment()
//        val applyLeaveFragment = ApplyLeaveFragment()

        tvName!!.setText(session!!.getData(Constant.NAME))
        tvMobile!!.setText(session!!.getData(Constant.MOBILE))

//        rlwithdrawhistory = root.findViewById(R.id.rlwithdrawhistory)
        rlTransectionHistory = root.findViewById(R.id.rlTransectionHistory)
//        rlApplyLeave = root.findViewById(R.id.rlApplyLeave)
        rlUpdateprofile = root.findViewById(R.id.rlUpdateprofile)
        rlChangepassword = root.findViewById(R.id.rlChangepassword)
//        rlmyBank = root.findViewById(R.id.rlmyBank)
        rlLogout = root.findViewById(R.id.rlLogout)
        rlInvite = root.findViewById(R.id.rlInvite)
        rlRefers = root.findViewById(R.id.rlRefers)
        rlGrowWithUs = root.findViewById(R.id.rlGrowWithUs)
        rlLevelIncome = root.findViewById(R.id.rlLevelIncome)
        rlWithdraw = root.findViewById(R.id.rlWithdraw)
        rlUpdateBank = root.findViewById(R.id.rlUpdateBank)
//        tvPlanActivate = root.findViewById(R.id.tvPlanActivate)
        btnFloatingAction = root.findViewById(R.id.btnFloatingAction)

//        var planName = session!!.getData(Constant.PLAN_NAME)
//
//        if(planName.isEmpty()) {
//            tvPlanActivate!!.text = "Trail"
//        } else {
//            tvPlanActivate!!.text = planName
//        }


//        rlwithdrawhistory!!.setOnClickListener(View.OnClickListener {
//            // Replace current fragment with withdrawalHistoryFragment
//            transaction.replace(R.id.Container, withdrawalHistoryFragment)
//            transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
//            transaction.commit()
//        })

        rlUpdateBank!!.setOnClickListener(View.OnClickListener {
            // Replace current fragment with updateBankFragment
            transaction.replace(R.id.Container, updateBankFragment)
            transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
            transaction.commit()
        })

//        rlApplyLeave!!.setOnClickListener(View.OnClickListener {
//            // Replace current fragment with updateBankFragment
//            transaction.replace(R.id.Container, applyLeaveFragment)
//            transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
//            transaction.commit()
//        })

        rlWithdraw!!.setOnClickListener(View.OnClickListener {
            // Replace current fragment with withdrawalFragment
            transaction.replace(R.id.Container, withdrawalFragment)
            transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
            transaction.commit()
        })

        rlRefers!!.setOnClickListener(View.OnClickListener {
            // Replace current fragment with withdrawalFragment
            transaction.replace(R.id.Container, myReferFragment)
            transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
            transaction.commit()
        })

        rlLevelIncome!!.setOnClickListener(View.OnClickListener {
            // Replace current fragment with withdrawalFragment
            transaction.replace(R.id.Container, levelIncomeFragment)
            transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
            transaction.commit()
        })

        rlGrowWithUs!!.setOnClickListener(View.OnClickListener {
            // Replace current fragment with withdrawalFragment
            transaction.replace(R.id.Container, extraIncomeFragment)
            transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
            transaction.commit()
        })

        rlInvite!!.setOnClickListener(View.OnClickListener { v: View? ->
            // Replace current fragment with inviteFragment
            transaction.replace(R.id.Container, inviteFragment)
            transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
            transaction.commit()

        })

        //        rlInvite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               Intent intent =  new Intent(activity, InviteActivity.class);
//               activity.startActivity(intent);
//            }
//        });
        rlTransectionHistory!!.setOnClickListener(View.OnClickListener {
            // Replace current fragment with transactionHistoryFragment
            transaction.replace(R.id.Container, transactionHistoryFragment)
            transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
            transaction.commit()
        })


        rlUpdateprofile!!.setOnClickListener(View.OnClickListener {
            // Replace current fragment with myProfileFragment
            transaction.replace(R.id.Container, myProfileFragment)
            transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
            transaction.commit()
        })
//        rlmyBank!!.setOnClickListener(View.OnClickListener {
//            // Replace current fragment with myBankFragment
//            transaction.replace(R.id.Container, myBankFragment)
//            transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
//            transaction.commit()
//        })

        rlChangepassword!!.setOnClickListener(View.OnClickListener {
            // Replace current fragment with setPasswordFragment
            transaction.replace(R.id.Container, setPasswordFragment)
            transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
            transaction.commit()
        })


        rlLogout!!.setOnClickListener(View.OnClickListener { session!!.logoutUser(activity) })

        initializeZohoSalesIQ()

        btnFloatingAction!!.setOnClickListener {
            ZohoSalesIQ.Chat.show()
        }

        return root
    }

    private fun initializeZohoSalesIQ() {
        val initConfig = InitConfig()
        ZohoSalesIQ.init(
            requireActivity().application,
            Constant.ZOHO_API_KEY,
            Constant.ZOHO_ACCESS_KEY,
        initConfig,
            object :
                InitListener {
                override fun onInitSuccess() {
                    // Initialization successful
                }

                override fun onInitError(errorCode: Int, errorMessage: String) {
                    // Handle initialization errors
                }
            }
        )
    }

//    private fun initializeZohoSalesIQ() {
//        ZohoSalesIQ.init(
//            requireActivity().getApplication(),
//            "5spwCGjIKo%2Fz6ssVNakmHbMTvtsszyor90%2BhrhHmnNgJcnpMvghcPXmu4dO6kxpO_in",
//            "4%2Fd2z2OovwP9rRaj3CO5TQtzMKPKxu%2FFaEkvD5l3RKcCLPKYaPjW%2B%2BzKEVzDx8I3UedpF6j3RR3PecllV1z3JrF3PMI%2BXoxRDSvLRDVerhOt%2FtApSWo%2FVw%3D%3D"
//        )
//
//        ZohoSalesIQ.Chat.showOperatorImageInLauncher(true)
//        btnFloatingAction!!.setOnClickListener {
//            ZohoSalesIQ.Chat.show()
//        }
//    }
}