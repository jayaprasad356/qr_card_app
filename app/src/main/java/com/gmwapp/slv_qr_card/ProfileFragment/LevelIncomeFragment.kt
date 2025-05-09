package com.gmwapp.slv_qr_card.ProfileFragment

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmwapp.slv_qr_card.Adapter.LevelDataAdapter
import com.gmwapp.slv_qr_card.Adapter.OutsourceLevelDataAdapter
import com.gmwapp.slv_qr_card.Adapter.TypeAdapter
import com.gmwapp.slv_qr_card.activities.MainActivity
import com.gmwapp.slv_qr_card.databinding.FragmentLevelIncomeBinding
import com.gmwapp.slv_qr_card.helper.ApiConfig
import com.gmwapp.slv_qr_card.helper.Constant
import com.gmwapp.slv_qr_card.helper.Session
import com.gmwapp.slv_qr_card.model.LevelIncomeList
import com.gmwapp.slv_qr_card.model.TypeData
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class LevelIncomeFragment : Fragment() {

    private lateinit var binding: FragmentLevelIncomeBinding
    private lateinit var session: Session

    private val itemList: MutableList<LevelIncomeList> = mutableListOf()
    private lateinit var adapter: LevelDataAdapter
    private lateinit var outsourceAdapter: OutsourceLevelDataAdapter

    private val levelTypeList = listOf(
        TypeData("b", "Level 1 (5%)"),
        TypeData("c", "Level 2 (2%)"),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLevelIncomeBinding.inflate(inflater, container, false)
        session = Session(requireContext())

        // Hide bottom navigation
        (activity as MainActivity).binding.bottomNavigation.visibility = View.GONE

        setupReferButton(binding.btnRefer, binding.btnReferText)

        // Back button functionality
        binding.ibBack.setOnClickListener { requireActivity().onBackPressed() }

        binding.llWaiting.setVisibility(View.VISIBLE)
        binding.frame.setVisibility(View.GONE)
        Handler().postDelayed({
            binding.llWaiting.setVisibility(View.GONE)
            binding.frame.setVisibility(View.VISIBLE)
        }, 2000)

        // Set default level if not already set
        if (session.getData(Constant.LEVEL).isNullOrEmpty()) {
            session.setData(Constant.LEVEL, "b") // Default to Level 1
        }

        // Setup RecyclerView for level types
        setupRecyclerView()

        // Initialize RecyclerView for Level Income List
        binding.rvDetail.layoutManager = LinearLayoutManager(requireContext())
        adapter = LevelDataAdapter(itemList)
        binding.rvDetail.adapter = adapter

        // Initialize RecyclerView for Level Income List
//        binding.rvOutsourceDetail.layoutManager = LinearLayoutManager(requireContext())
//        outsourceAdapter = OutsourceLevelDataAdapter(itemList)
//        binding.rvOutsourceDetail.adapter = outsourceAdapter

        // Load initial data
        teamList()
//        outsourceTeamList()

        return binding.root
    }

    private fun setupReferButton(btnRefer: MaterialButton, btnReferText: MaterialButton) {
        val referCode = arrayOf(session.getData(Constant.REFER_CODE))
        val baseUrl = Constant.PLAY_STORE_URL

        //        String baseUrl = "https://aidiapp.in/";
        if (referCode[0] != null) {
            btnRefer.setOnClickListener { v: View? ->
                shareTextAndUrl(
                    """Click this link to join QR Card App ☺️
Use My Refer Code ${referCode[0]} While Creating Account.""", baseUrl
                )
            }
            btnReferText.text = referCode[0]
        } else {
            btnRefer.setOnClickListener { v: View? ->
                shareTextAndUrl(
                    "Click this link to join QR Card App ☺️\nUse My Refer Code ID123 While Creating Account.",
                    baseUrl
                )
            }
            btnReferText.text = "123456"
        }

        btnReferText.setOnClickListener { v: View? ->
            if (referCode[0] == null || referCode[0]!!.isEmpty()) {
                referCode[0] = "123456" // Default refer code
            }
            val clipboard =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Refer Code", referCode[0])
            clipboard.setPrimaryClip(clip)
        }
    }

    private fun shareTextAndUrl(message: String, url: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(
            Intent.EXTRA_TEXT, """
     $message
     $url
     """.trimIndent()
        )
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun teamList() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)
        params[Constant.LEVEL] = session.getData(Constant.LEVEL)

        ApiConfig.RequestToVolley({ result: Boolean, response: String? ->
            if (result) {

                itemList.clear()

                try {
                    val jsonObject = JSONObject(response!!)

                    if (jsonObject.getBoolean("success")) {
                        val jsonArray = jsonObject.getJSONArray(Constant.DATA)
                        val g = Gson()

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1: JSONObject = jsonArray.getJSONObject(i)
                            val group: LevelIncomeList = g.fromJson(
                                jsonObject1.toString(),
                                LevelIncomeList::class.java
                            )
                            itemList.add(group)
                        }

                        // Initialize RecyclerView for Level Income List
                        binding.rvDetail.layoutManager = LinearLayoutManager(requireContext())
                        adapter = LevelDataAdapter(itemList)
                        binding.rvDetail.adapter = adapter

                    } else {
//                        Toast.makeText(
//                            requireContext(),
//                            jsonObject.getString("message"),
//                            Toast.LENGTH_SHORT
//                        ).show()
                        Log.d("TEAM_LIST_URL", "TEAM_LIST_URL TEST: " + jsonObject.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                Log.d("TEAM_LIST_URL", "TEAM_LIST_URL TEST: " + Constant.TEAM_LIST_URL)

                binding.llWaiting.setVisibility(View.VISIBLE)
                binding.frame.setVisibility(View.GONE)
                if(itemList.isNotEmpty()){
                    adapter.notifyDataSetChanged()
                    binding.llWaiting.setVisibility(View.GONE)
                    binding.frame.setVisibility(View.VISIBLE)
                }

//                binding.llWaiting.setVisibility(View.VISIBLE)
//                binding.frame.setVisibility(View.GONE)
//                Handler().postDelayed({
//                    adapter.notifyDataSetChanged()
//                    binding.llWaiting.setVisibility(View.GONE)
//                    binding.frame.setVisibility(View.VISIBLE)
//                }, 2000)

                if (itemList.isEmpty()) {
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.VISIBLE
                } else {
                    binding.tvNoData.visibility = View.GONE
                }
            }
        }, activity, Constant.TEAM_LIST_URL, params, true)

        Log.d("TEAM_LIST_URL", "TEAM_LIST_URL: " + Constant.TEAM_LIST_URL)
        Log.d("TEAM_LIST_URL", "TEAM_LIST_URL params: $params")
    }

//    @SuppressLint("NotifyDataSetChanged")
//    private fun outsourceTeamList() {
//        val params: MutableMap<String, String> = HashMap()
//        params[Constant.USER_ID] = session.getData(Constant.USER_ID)
//        params[Constant.LEVEL] = session.getData(Constant.LEVEL)
//
//        ApiConfig.RequestToVolley({ result: Boolean, response: String? ->
//            if (result) {
//
//                itemList.clear()
//
//                try {
//                    val jsonObject = JSONObject(response!!)
//
//                    if (jsonObject.getBoolean("success")) {
//                        val jsonArray = jsonObject.getJSONArray(Constant.DATA)
//                        val g = Gson()
//
//                        for (i in 0 until jsonArray.length()) {
//                            val jsonObject1: JSONObject = jsonArray.getJSONObject(i)
//                            val group: LevelIncomeList = g.fromJson(
//                                jsonObject1.toString(),
//                                LevelIncomeList::class.java
//                            )
//                            itemList.add(group)
//                        }
//
//                        // Initialize RecyclerView for Level Income List
//                        binding.rvOutsourceDetail.layoutManager = LinearLayoutManager(requireContext())
//                        outsourceAdapter = OutsourceLevelDataAdapter(itemList)
//                        binding.rvOutsourceDetail.adapter = outsourceAdapter
//
//                    } else {
////                        Toast.makeText(
////                            requireContext(),
////                            jsonObject.getString("message"),
////                            Toast.LENGTH_SHORT
////                        ).show()
//
//                        Log.d("OUTSOURCE_TEAM_LIST_URL", "OUTSOURCE_TEAM_LIST_URL: " + jsonObject.getString("message"))
//                    }
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//
//                binding.llWaiting.setVisibility(View.VISIBLE)
//                binding.frame.setVisibility(View.GONE)
//                Handler().postDelayed({
//                    outsourceAdapter.notifyDataSetChanged()
//                    binding.llWaiting.setVisibility(View.GONE)
//                    binding.frame.setVisibility(View.VISIBLE)
//                }, 2000)
//
//                if (itemList.isEmpty()) {
//                    binding.tvNoData.visibility = View.VISIBLE
//                    binding.tvNoData.visibility = View.VISIBLE
//                } else {
//                    binding.tvNoData.visibility = View.GONE
//                }
//            }
//        }, activity, Constant.OUTSOURCE_TEAM_LIST_URL, params, true)
//
//        Log.d("OUTSOURCE_TEAM_LIST_URL", "OUTSOURCE_TEAM_LIST_URL: " + Constant.OUTSOURCE_TEAM_LIST_URL)
//        Log.d("OUTSOURCE_TEAM_LIST_URL", "OUTSOURCE_TEAM_LIST_URL params: $params")
//    }

    private fun setupRecyclerView() {
        val typeAdapter = TypeAdapter(levelTypeList, object : TypeAdapter.OnLevelSelectedListener {
            override fun onLevelSelected(levelId: String) {
                // Update the session and reload data
                session.setData(Constant.LEVEL, levelId)
                teamList()
//                outsourceTeamList()
            }
        })

        binding.rvLevelType.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvLevelType.adapter = typeAdapter
    }
}
