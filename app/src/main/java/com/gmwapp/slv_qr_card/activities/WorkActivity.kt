package com.gmwapp.slv_qr_card.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gmwapp.slv_qr_card.R
import com.gmwapp.slv_qr_card.helper.ApiConfig
import com.gmwapp.slv_qr_card.helper.Constant
import com.gmwapp.slv_qr_card.helper.NoPasteEditText
import com.gmwapp.slv_qr_card.helper.Session
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import org.json.JSONException
import org.json.JSONObject

class WorkActivity : AppCompatActivity() {

    private var btCreate: MaterialButton? = null
    private var btnSyncNow: MaterialButton? = null
    private var btnAutoFill: MaterialButton? = null
    private var cbCodes: CircularProgressIndicator? = null
    private var tvCodes: TextView? = null

    private var edCompanyName: NoPasteEditText? = null
    private var edCity: NoPasteEditText? = null
    private var edCountry: NoPasteEditText? = null
    private var edWebsite: NoPasteEditText? = null
    private var edEmailId: NoPasteEditText? = null
    private var edBusinessId: NoPasteEditText? = null
    private var zipBox1: NoPasteEditText? = null
    private var zipBox2: NoPasteEditText? = null
    private var zipBox3: NoPasteEditText? = null
    private var zipBox4: NoPasteEditText? = null
    private var zipBox5: NoPasteEditText? = null
    private var zipBox6: NoPasteEditText? = null

    private var llCreateLoader: LinearLayout? = null

    private var tvCompanyName: TextView? = null
    private var tvCity: TextView? = null
    private var tvCountry: TextView? = null
    private var tvWebsite: TextView? = null
    private var tvEmailId: TextView? = null
    private var tvZipCode: TextView? = null
    private var tvBusinessId: TextView? = null

    private var tvTodayCodes: TextView? = null
    private var tvTotalCodes: TextView? = null
    private var tvWorkedDays: TextView? = null

    private var codeCount = 0
    private val maxCodeCount = 50

    private var session: Session? = null
    private var planId: String? = null
    private val sampleDataList = mutableListOf<Map<String, String>>()  // to store data from CSV


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work)

        session = Session(this)

        // 🔥 Load saved code count from session
        codeCount = session!!.getData("CURRENT_CODE_COUNT").toIntOrNull() ?: 0

        // 🔥 Get the plan ID from intent
        planId = intent.getStringExtra("plan_id")
        Log.d("WorkActivity", "Received plan_id: $planId")
        loadCsvData()

        initViews()
        setRandomSamples()
        setupZipCodeAutoMove()
        updateProgress()




        tvTodayCodes!!.text = session!!.getData(Constant.TODAY_CODES)
        tvTotalCodes!!.text = session!!.getData(Constant.TOTAL_CODES)
        tvWorkedDays!!.text = session!!.getData(Constant.WORKED_DAYS)

        // 🔒 Hide Sync if Trial Plan
        if (planId == "1") {
            btnSyncNow!!.visibility = View.GONE
        }

        btCreate!!.setOnClickListener {
            handleCreate()
        }

        btnSyncNow!!.setOnClickListener {
            if (codeCount < maxCodeCount) {
                Toast.makeText(this, "You need to create 50 codes before syncing.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            llCreateLoader!!.visibility = View.VISIBLE
            generateCodes()
        }

        btnAutoFill!!.setOnClickListener {
            autoFillInputs()
        }
    }

    private fun initViews() {
        btCreate = findViewById(R.id.btCreate)
        btnSyncNow = findViewById(R.id.btnSyncNow)
        cbCodes = findViewById(R.id.cbCodes)
        tvCodes = findViewById(R.id.tvCodes)

        edCompanyName = findViewById(R.id.edCompanyName)
        edCity = findViewById(R.id.edCity)
        edCountry = findViewById(R.id.edCountry)
        edWebsite = findViewById(R.id.edWebsite)
        edEmailId = findViewById(R.id.edEmailId)
        edBusinessId = findViewById(R.id.edBusinessId)

        zipBox1 = findViewById(R.id.zip_code_box1)
        zipBox2 = findViewById(R.id.zip_code_box2)
        zipBox3 = findViewById(R.id.zip_code_box3)
        zipBox4 = findViewById(R.id.zip_code_box4)
        zipBox5 = findViewById(R.id.zip_code_box5)
        zipBox6 = findViewById(R.id.zip_code_box6)

        llCreateLoader = findViewById(R.id.llCreateLoader)

        tvCompanyName = findViewById(R.id.tvCompanyName)
        tvCity = findViewById(R.id.tvCity)
        tvCountry = findViewById(R.id.tvCountry)
        tvWebsite = findViewById(R.id.tvWebsite)
        tvEmailId = findViewById(R.id.tvEmailId)
        tvZipCode = findViewById(R.id.tvZipCode)
        tvBusinessId = findViewById(R.id.tvBusinessId)
        btnAutoFill = findViewById(R.id.btnAutoFill)

        tvTodayCodes = findViewById(R.id.tvTodayCodes)
        tvTotalCodes = findViewById(R.id.tvTotalCodes)
        tvWorkedDays = findViewById(R.id.tvWorkedDays)
    }

    private fun setupZipCodeAutoMove() {
        autoMove(zipBox1!!, null, zipBox2)
        autoMove(zipBox2!!, zipBox1, zipBox3)
        autoMove(zipBox3!!, zipBox2, zipBox4)
        autoMove(zipBox4!!, zipBox3, zipBox5)
        autoMove(zipBox5!!, zipBox4, zipBox6)
        autoMove(zipBox6!!, zipBox5, null)
    }
    private fun loadCsvData() {
        try {
            val inputStream = assets.open("companies_data.csv")
            val reader = inputStream.bufferedReader()
            val lines = reader.readLines()
            val header = lines.first().split(",")

            for (line in lines.drop(1)) {
                val values = line.split(",")
                if (values.size >= 7) {
                    val dataMap = mapOf(
                        "company_name" to values[0].trim(),
                        "city" to values[1].trim(),
                        "county_name" to values[2].trim(),   // ✅ new line for country_name
                        "parent_zip" to values[3].trim(),
                        "business_id" to values[4].trim(),
                        "website" to values[5].trim(),
                        "support_email" to values[6].trim()
                    )

                    sampleDataList.add(dataMap)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun autoMove(current: NoPasteEditText, previous: NoPasteEditText?, next: NoPasteEditText?) {
        current.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length == 1 && next != null) {
                    next.requestFocus()
                } else if (s != null && s.isEmpty() && previous != null) {
                    previous.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun handleCreate() {
        val company = edCompanyName!!.text.toString().trim()
        val city = edCity!!.text.toString().trim()
        val country = edCountry!!.text.toString().trim()
        val website = edWebsite!!.text.toString().trim()
        val email = edEmailId!!.text.toString().trim()
        val businessId = edBusinessId!!.text.toString().trim()
        val zipCode = zipBox1!!.text.toString().trim() +
                zipBox2!!.text.toString().trim() +
                zipBox3!!.text.toString().trim() +
                zipBox4!!.text.toString().trim() +
                zipBox5!!.text.toString().trim() +
                zipBox6!!.text.toString().trim()

        if (company.isEmpty() || city.isEmpty() || country.isEmpty() ||
            website.isEmpty() || email.isEmpty() || businessId.isEmpty() || zipCode.length != 6
        ) {
            Toast.makeText(this, "Please fill in all fields properly", Toast.LENGTH_SHORT).show()
            return
        }

        // 🔍 Validation checks
        if (!company.equals(tvCompanyName!!.text.toString().trim(), ignoreCase = true)) {
            showError("Company name is not matching")
        } else if (!city.equals(tvCity!!.text.toString().trim(), ignoreCase = true)) {
            showError("City is not matching")
        } else if (!country.equals(tvCountry!!.text.toString().trim(), ignoreCase = true)) {
            showError("Country is not matching")
        } else if (!website.equals(tvWebsite!!.text.toString().trim(), ignoreCase = true)) {
            showError("Website is not matching")
        } else if (!email.equals(tvEmailId!!.text.toString().trim(), ignoreCase = true)) {
            showError("Email is not matching")
        } else if (!zipCode.equals(tvZipCode!!.text.toString().trim(), ignoreCase = true)) {
            showError("Zip code is not matching")
        } else if (!businessId.equals(tvBusinessId!!.text.toString().trim(), ignoreCase = true)) {
            showError("Business ID is not matching")
        } else {
            if (codeCount < maxCodeCount) {
                val increment = if (planId == "3") 2 else 1
                codeCount += increment
                if (codeCount > maxCodeCount) {
                    codeCount = maxCodeCount
                }

                // 🔥 Save updated code count
                session!!.setData("CURRENT_CODE_COUNT", codeCount.toString())

                updateProgress()
                clearInputs()
                setRandomSamples()

                if (codeCount >= maxCodeCount) {
                    Toast.makeText(this, "You reached max 50 codes. Please sync now.", Toast.LENGTH_SHORT).show()
                } else {
                    val msg = if (planId == "3") {
                        "Premium plan: 2 codes added successfully!"
                    } else {
                        "Code created successfully!"
                    }
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    // ✅ Show dialog popup
                    showQrSuccessDialog()

                }
            } else {
                Toast.makeText(this, "You already created 50 codes. Please sync.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateCodes() {
        val params: MutableMap<String, String> = HashMap()
        params["user_id"] = session!!.getData(Constant.USER_ID)
        params["code_count"] = codeCount.toString()

        ApiConfig.RequestToVolley(
            { result: Boolean, response: String ->
                llCreateLoader!!.visibility = View.GONE
                Log.d("CODE_GEN_RES", response)
                if (result) {
                    handleCodeGenResponse(response)
                } else {
                    Toast.makeText(this, "API failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            },
            this@WorkActivity, Constant.CODE_GENERATE_URL, params, true
        )
    }

    private fun handleCodeGenResponse(response: String) {
        try {
            val jsonObject = JSONObject(response)
            if (jsonObject.getBoolean("success")) {
                val message = jsonObject.getString("message")
                val todayCodes = jsonObject.optInt("today_codes", 0)
                val totalCodes = jsonObject.optInt("total_codes", 0)
                session!!.setData(Constant.TODAY_CODES, todayCodes.toString())
                session!!.setData(Constant.TOTAL_CODES, totalCodes.toString())

                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                tvTodayCodes!!.text = session!!.getData(Constant.TODAY_CODES)
                tvTotalCodes!!.text = session!!.getData(Constant.TOTAL_CODES)

                // ✅ Reset counter after sync
                codeCount = 0
                session!!.setData("CURRENT_CODE_COUNT", "0")
                updateProgress()
                setRandomSamples()

            } else {
                val message = jsonObject.getString("message")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Toast.makeText(this, "Parsing error: " + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearInputs() {
        edCompanyName!!.text!!.clear()
        edCity!!.text!!.clear()
        edCountry!!.text!!.clear()
        edWebsite!!.text!!.clear()
        edEmailId!!.text!!.clear()
        edBusinessId!!.text!!.clear()
        zipBox1!!.text!!.clear()
        zipBox2!!.text!!.clear()
        zipBox3!!.text!!.clear()
        zipBox4!!.text!!.clear()
        zipBox5!!.text!!.clear()
        zipBox6!!.text!!.clear()
    }

    private fun updateProgress() {
        tvCodes!!.text = codeCount.toString()
        val percentage = (codeCount * 100) / maxCodeCount
        cbCodes!!.progress = percentage

        btCreate!!.isEnabled = codeCount < maxCodeCount

        // ✅ Enable Sync button if codes are 50 or more (not for Trial plan)
        if (planId != "1") {
            btnSyncNow!!.isEnabled = codeCount >= maxCodeCount
        }

        val apiTodayCount = session!!.getData(Constant.TODAY_CODES).toIntOrNull() ?: 0
        val displayToday = if (codeCount > 0) "$apiTodayCount + $codeCount" else "$apiTodayCount"
        tvTodayCodes!!.text = displayToday

        val apiTotalCount = session!!.getData(Constant.TOTAL_CODES).toIntOrNull() ?: 0
        val displayTotal = if (codeCount > 0) "$apiTotalCount + $codeCount" else "$apiTotalCount"
        tvTotalCodes!!.text = displayTotal
    }
    private fun showQrSuccessDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_qr_success, null)
        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)  // Cannot close manually
            .create()

        dialog.show()

        // Auto dismiss after 2 seconds
        dialogView.postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }, 2000)
    }


    private fun autoFillInputs() {
        edCompanyName!!.setText(tvCompanyName!!.text.toString())
        edCity!!.setText(tvCity!!.text.toString())
        edCountry!!.setText(tvCountry!!.text.toString())
        edWebsite!!.setText(tvWebsite!!.text.toString())
        edEmailId!!.setText(tvEmailId!!.text.toString())
        edBusinessId!!.setText(tvBusinessId!!.text.toString())

        val zip = tvZipCode!!.text.toString()
        if (zip.length == 6) {
            zipBox1!!.setText(zip[0].toString())
            zipBox2!!.setText(zip[1].toString())
            zipBox3!!.setText(zip[2].toString())
            zipBox4!!.setText(zip[3].toString())
            zipBox5!!.setText(zip[4].toString())
            zipBox6!!.setText(zip[5].toString())
        }
    }
    private fun setRandomSamples() {
        if (sampleDataList.isNotEmpty()) {
            val randomData = sampleDataList.random()

            tvCompanyName!!.text = randomData["company_name"]
            tvCity!!.text = randomData["city"]
            tvCountry!!.text = randomData["county_name"]  // ✅ use county_name now
            tvWebsite!!.text = randomData["website"]
            tvEmailId!!.text = randomData["support_email"]
            tvZipCode!!.text = randomData["parent_zip"]
            tvBusinessId!!.text = randomData["business_id"]

        } else {
            Toast.makeText(this, "No sample data found!", Toast.LENGTH_SHORT).show()
        }
    }


//    private fun setRandomSamples() {
//        val companySamples = arrayOf(
//            "TechNova", "InnoWorks", "FutureCorp", "Skyline Ltd", "QuantumSoft",
//            "BrightPath", "NeonWave", "Visionary Inc", "AlphaOmega", "GlobeTech"
//        )
//        val citySamples = arrayOf(
//            "Mumbai", "Chennai", "Bangalore", "Hyderabad", "Delhi",
//            "Kolkata", "Pune", "Coimbatore", "Ahmedabad", "Jaipur"
//        )
//        val countrySamples = arrayOf(
//            "India", "USA", "UK", "Canada", "Australia",
//            "Germany", "France", "Japan", "Singapore", "Brazil"
//        )
//        val websiteSamples = arrayOf(
//            "www.technova.com", "www.innoworks.io", "www.futurecorp.net", "www.skyline.co",
//            "www.quantumsoft.org", "www.brightpath.in", "www.neonwave.app", "www.visionary.com",
//            "www.alphaomega.biz", "www.globetech.io"
//        )
//        val emailSamples = arrayOf(
//            "contact@technova.com",
//            "info@innoworks.io",
//            "hello@futurecorp.net",
//            "support@skyline.co",
//            "admin@quantumsoft.org",
//            "team@brightpath.in",
//            "mail@neonwave.app",
//            "reach@visionary.com",
//            "sales@alphaomega.biz",
//            "help@globetech.io"
//        )
//        val zipCodeSamples = arrayOf(
//            "110001", "400001", "560001", "600001", "700001",
//            "500001", "380001", "682001", "302001", "751001"
//        )
//        val businessIdSamples = arrayOf(
//            "00001-ABCDE", "12345-XYZAB", "99999-HELLO", "54321-ALPHA",
//            "22222-BRAVO", "11111-DELTA", "33333-ECHO", "44444-FOXTROT",
//            "55555-GOLF", "66666-HOTEL"
//        )
//
//        tvCompanyName!!.text = companySamples.random()
//        tvCity!!.text = citySamples.random()
//        tvCountry!!.text = countrySamples.random()
//        tvWebsite!!.text = websiteSamples.random()
//        tvEmailId!!.text = emailSamples.random()
//        tvZipCode!!.text = zipCodeSamples.random()
//        tvBusinessId!!.text = businessIdSamples.random()
//    }
}
