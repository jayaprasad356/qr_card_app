package com.gmwapp.slv_qr_card.ProfileFragment

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmwapp.slv_qr_card.Adapter.PaymentScreenshotListAdapter
import com.gmwapp.slv_qr_card.activities.MainActivity
import com.gmwapp.slv_qr_card.databinding.FragmentUploadCertificateBinding
import com.gmwapp.slv_qr_card.helper.ApiConfig
import com.gmwapp.slv_qr_card.helper.Constant
import com.gmwapp.slv_qr_card.helper.Session
import com.gmwapp.slv_qr_card.model.PaymentScreenshot
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class UploadCertificateFragment : Fragment() {
    lateinit var binding: FragmentUploadCertificateBinding
    lateinit var session: Session
    lateinit var paymentScreenshotListAdapter: PaymentScreenshotListAdapter
    var paymentScreenshot: MutableList<PaymentScreenshot> = mutableListOf()
    private var selectedImageUri: Uri? = null
    private lateinit var progressDialog: ProgressDialog

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            binding.ivImage.setImageURI(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadCertificateBinding.inflate(inflater, container, false)
        session = Session(requireContext())

        (activity as MainActivity).binding.bottomNavigation.visibility = View.GONE

        binding.ibBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        setupListeners()

        setupRecyclerView()

        loadTransactions()

        return binding.root
    }

    private fun setupListeners() {
        binding.ibBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnUploadImage.setOnClickListener {
            askMediaPermissions()
        }

        binding.btnSubmit.setOnClickListener {
            uploadImageToServer()
        }
    }

    private fun askMediaPermissions() {
        val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (requiredPermissions.all { ContextCompat.checkSelfPermission(requireActivity(), it) == PackageManager.PERMISSION_GRANTED }) {
            onMediaPermissionsGranted()
        } else {
            requestPermissionLauncher.launch(requiredPermissions)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.all { it.value }

            if (allGranted) {
                onMediaPermissionsGranted()
            } else {
                if (permissions.any { !ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), it.key) }) {
                    Toast.makeText(requireActivity(), "Permission permanently denied. Enable it in settings.", Toast.LENGTH_LONG).show()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + requireActivity().packageName))
                    requireActivity().startActivity(intent)
                } else {
                    Toast.makeText(requireActivity(), "Permissions denied. Unable to access media.", Toast.LENGTH_LONG).show()
                }
            }
        }

    private fun onMediaPermissionsGranted() {
//        Toast.makeText(requireActivity(), "Media permissions granted!", Toast.LENGTH_LONG).show()
        pickImageLauncher.launch("image/*")
    }

    private fun uploadImageToServer() {
        if (selectedImageUri == null) {
            Toast.makeText(requireContext(), "Please select an image first", Toast.LENGTH_SHORT).show()
            return
        }

        val file = uriToFile(selectedImageUri!!)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        // Show progress dialog
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Uploading, please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        // Create parameters map
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)

        // Create file parameters for the image upload
        val fileParams: MutableMap<String, String> = HashMap()
        fileParams["image"] = file.absolutePath

        // Make API request to upload image
        ApiConfig.RequestToVolley({ result: Boolean, response: String? ->
            progressDialog.dismiss()
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("success")) {
                        Toast.makeText(requireContext(), "Upload successful", Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressed()
                    } else {
                        Toast.makeText(requireContext(), "Upload failed: ${jsonObject.getString("message")}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }, requireActivity(), Constant.UPLOAD_PAYMENT_SCREENSHOT, params, fileParams)

        Log.d("UPLOAD_PAYMENT_SCREENSHOT", "UPLOAD_PAYMENT_SCREENSHOT: " + Constant.UPLOAD_PAYMENT_SCREENSHOT)
        Log.d("UPLOAD_PAYMENT_SCREENSHOT", "UPLOAD_PAYMENT_SCREENSHOT params: $params")
        Log.d("UPLOAD_PAYMENT_SCREENSHOT", "UPLOAD_PAYMENT_SCREENSHOT fileParams: $fileParams")
    }

    // Convert URI to file to upload
    private fun uriToFile(uri: Uri): File {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val file = File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }

    private fun setupRecyclerView() {
        paymentScreenshotListAdapter = PaymentScreenshotListAdapter(activity, requireContext(), paymentScreenshot)
        binding.rvTransaction.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransaction.adapter = paymentScreenshotListAdapter
    }

    private fun loadTransactions() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)

        ApiConfig.RequestToVolley({ result: Boolean, response: String? ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray = jsonObject.getJSONArray(Constant.DATA)
                        val gson = Gson()

                        paymentScreenshot.clear()

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            // Change PlanListModel to Transanction
                            val transaction = gson.fromJson(
                                jsonObject1.toString(),
                                PaymentScreenshot::class.java
                            )
                            paymentScreenshot.add(transaction)
                        }

                        paymentScreenshotListAdapter.notifyDataSetChanged()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, activity, Constant.PAYMENT_SCREENSHOT_LIST, params, true)

        Log.d("PAYMENT_SCREENSHOT_LIST", "PAYMENT_SCREENSHOT_LIST: " + Constant.TRNSACTION_LIST_URL)
        Log.d("PAYMENT_SCREENSHOT_LIST", "PAYMENT_SCREENSHOT_LIST params: $params")
    }

}