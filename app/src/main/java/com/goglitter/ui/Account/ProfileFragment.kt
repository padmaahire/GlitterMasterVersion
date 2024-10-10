package com.goglitter.ui.Account

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.goglitter.R
import com.goglitter.databinding.FragmentProfileBinding
import com.goglitter.domain.request.EmailRequest
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.Constants.IMAGE_PATH
import com.goglitter.utils.NetworkResult
import com.goglitter.utils.TokenManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.util.*
import javax.inject.Inject


/**
@author-Padma A
date-26/7/2023
updated -26/04/2024
 **/
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class ProfileFragment : Fragment() ,UploadRequestBody.UploadCallback{
    lateinit var binding: FragmentProfileBinding
    private var param1: String? = null
    private var param2: String? = null
    val viewModel: AuthViewModel by viewModels()
    private val calendar = Calendar.getInstance()

    @Inject
    lateinit var tokenManager: TokenManager
    var id: String = ""
    var mobile: String = ""
    private val user_id = ""
    private var picture_path: String? = ""
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        id = tokenManager.getSalesId().toString()
        getUser(id)
        binding.tvChangePhoto.setOnClickListener() {
            if (checkAndRequestPermissions(activity)) {
                binding.btnUpdate.isEnabled = true
                chooseImage(requireActivity())
            } else {
                binding.btnUpdate.isEnabled = true
                chooseImage(requireActivity())
            }
        }
        binding.btnUpdate.setOnClickListener() {
            if (!validateEmailAddr()) {
                return@setOnClickListener
            }
            val email = binding.edtEmailId.text.toString()

            if (!picture_path.isNullOrEmpty()) {
                // There's a new profile picture, update it
                UpdateProfileImage()
            } else {
                // No new picture, update the email address
                UpdateProfileEmail(id, email)
            }
        }
        binding.tvEditEmail.setOnClickListener() {
            binding.edtEmailId.setSelection(binding.edtEmailId.text.length)
            binding.edtEmailId.inputType = EditorInfo.TYPE_CLASS_TEXT
            binding.edtEmailId.requestFocus()

            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(
                binding.edtEmailId,
                InputMethodManager.SHOW_IMPLICIT
            )
            binding.btnUpdate.isEnabled = true
        }
        binding.edtEmailId.setOnClickListener(){
            binding.btnUpdate.isEnabled = true
        }
        binding.camera.setOnClickListener() {
            if (checkAndRequestPermissions(activity)) {
                binding.btnUpdate.isEnabled = true
                chooseImage(requireActivity())
            } else {
                binding.btnUpdate.isEnabled = true
                chooseImage(requireActivity())
            }
        }

        binding.btnUpdate.isEnabled = false
        return view
    }

    private fun UpdateProfileImage() {
        val file = File(picture_path)
        val body = UploadRequestBody(file, "image", this)

        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val imagePart = MultipartBody.Part.createFormData("profile_image", "profile_image", requestFile)
        Log.d("img",imagePart.toString())
        //  val regId = MultipartBody.Part.createFormData("regId", id)
        val regId: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), id)

        viewModel.uploadProfileImage(regId,imagePart)
        //viewModel.uploadProfileImage(RequestBody.create("multipart/form-data".toMediaTypeOrNull(), id.toString()), MultipartBody.Part.createFormData("profilePicture", file.name.toString().trim(), body))
        viewModel.profileImageResponse.observe(requireActivity()) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = true
                }
                is NetworkResult.Failure -> {

                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    viewModel.clear()
                    if (it.data.status.equals("success")) {
                        binding.progressCircular.isVisible = false
                        showSnackBar(it.data.msg.toString())
                        Handler().postDelayed({
                            Navigation.findNavController(requireView()).navigate(R.id.nav_account)
                        }, 2000)

                    }else{
                        showSnackBar(it.data.msg.toString())
                    }
                }
                else -> {}
            }
        }
    }

    private fun UpdateProfileEmail(id: String, email: String) {
        val request=EmailRequest(id,email)
        viewModel.uploadProfileEmail(request)
        //viewModel.uploadProfileImage(RequestBody.create("multipart/form-data".toMediaTypeOrNull(), id.toString()), MultipartBody.Part.createFormData("profilePicture", file.name.toString().trim(), body))
        viewModel.profileEmailResponse.observe(requireActivity()) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = true
                }
                is NetworkResult.Failure -> {

                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    viewModel.clear()
                    if (it.data.status.equals("success")) {
                        binding.progressCircular.isVisible = false
                        showSnackBar(it.data.msg.toString())
                        Handler().postDelayed({
                            Navigation.findNavController(requireView()).navigate(R.id.nav_account)
                        }, 2000)

                    }else{
                        showSnackBar(it.data.msg.toString())
                    }
                }
                else -> {}
            }
        }
    }
    private fun getUser(id: String) {
        viewModel.getUserResponse(id)
        viewModel.userProfileResponse.observe(requireActivity()) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = true
                }
                is NetworkResult.Failure -> {
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    try {
                        binding.progressCircular.isVisible = false
                        if (it.data.result != null) {
                            if (it.data.status.equals("success")) {
                                if (it.data.result.profile_image != null && !it.data.result.profile_image.equals("")) {
                                    val url = IMAGE_PATH + it.data.result.profile_image
                                    Log.e("TAG", "URL ====> $url")
                                    Glide.with(requireActivity())
                                        .load(url)
                                        .into(binding.ivProfileImage)
                                } else {
                                    binding.ivProfileImage.setImageResource(R.drawable.ic_avatar)
                                }
                                val fname = it.data.result!!.regName
                                val lname = it.data.result!!.regLname
                                binding.tvFullName.setText(fname + " " + lname)
                                var emailId=it.data.result!!.regEmail
                                if(!emailId.isNullOrEmpty()){
                                    binding.tvEmailId.setText(emailId)
                                    binding.layEmail.visibility=View.GONE
                                }else{
                                    binding.tvEmailId.visibility=View.GONE
                                    binding.layEmail.visibility=View.VISIBLE
                                }

                                val _phoneNo = tokenManager.getMobile()
                                val str = _phoneNo!!.length - 10
                                val substring: String = _phoneNo.toString().substring(str)
                                val str1 = _phoneNo!!
                                val strNew: String = str1.replace(substring, "")
                                //val code:String=_phoneNo.toString().substring(str1)
                                binding.tvPhoneNumber.setText("+91" + "-" + substring)
                               // binding.tvPhoneNumber.setText(substring)

                            } else { Log.e("TAG", "ERROR") }
                        } else { Log.e("TAG", "ERROR") }

                    } catch (e: Exception) { Log.e("TAG", e.toString()) }
                }
            }
        }
    }

    // function to check permission
    fun checkAndRequestPermissions(context: Activity?): Boolean {
        val WExtstorePermission =
            ContextCompat.checkSelfPermission(
                requireContext()!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val cameraPermission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        val readstorage =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (readstorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        //Permission work on OnePlus device
        if (readstorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_MEDIA_IMAGES)
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity()!!, listPermissionsNeeded
                    .toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    @SuppressLint("MissingInflatedId")
    private fun chooseImage(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
        val customLayout: View = layoutInflater.inflate(R.layout.custom, null)
        alertDialog.setView(customLayout)
        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        val photo = customLayout.findViewById<TextView>(R.id.tvCamera)
        val gallary = customLayout.findViewById<TextView>(R.id.tvGallery)
        val exit = customLayout.findViewById<TextView>(R.id.tvExit)
        photo.setOnClickListener {
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture, 0)
            alert.dismiss()
        }
        gallary.setOnClickListener {
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(pickPhoto, 1)
            alert.dismiss()
        }
        exit.setOnClickListener { alert.dismiss() }
        alert.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> if (ContextCompat.checkSelfPermission(
                    requireActivity()!!,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                showSnackBar("Glitter Requires Access to Camara.")
            } else if (ContextCompat.checkSelfPermission(
                    requireActivity()!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                showSnackBar("Glitter Requires Access to Your Storage.")

            }else {
                chooseImage(requireActivity()!!)
            }
        }
    }
    // Handled permission Result

    @SuppressLint("SuspiciousIndentation")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == Activity.RESULT_OK && data != null) {
                    //Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                    //binding.ivUserImage.setImageBitmap(selectedImage)
                    //picture_path =  RealPathUtil.getRealPath(requireActivity(),data.data)
                    onCaptureImageResult(data)
                }
                1 -> if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedImage = data.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    if (selectedImage != null) {
                        val cursor = requireActivity()!!.contentResolver.query(
                            selectedImage,
                            filePathColumn,
                            null,
                            null,
                            null
                        )
                        if (cursor != null) {
                            cursor.moveToFirst()
                            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                            //picture_path = cursor.getString(columnIndex)
                            picture_path =
                                RealPathUtil.getRealPath(requireActivity(), selectedImage)
                            Log.d("path",picture_path.toString())
                            binding.ivProfileImage.setImageBitmap(BitmapFactory.decodeFile(picture_path))

                            cursor.close()
                        }
                    }
                }
            }
        }
    }

    private fun onCaptureImageResult(data: Intent) {
        val thumbnail = data.extras!!["data"] as Bitmap?
        val bytes = ByteArrayOutputStream()
        thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val destination = File(
            Environment.getExternalStorageDirectory(),
            System.currentTimeMillis().toString() + ".jpg"
        )
        picture_path =
            RealPathUtil.getRealPath(requireActivity(), getImageUri(requireActivity(), thumbnail))
        val fo: FileOutputStream
        try {
            destination.createNewFile()
            fo = FileOutputStream(destination)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        binding.ivProfileImage.setImageBitmap(thumbnail)
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    open fun showSnackBar(message :String) {
        val rootView: View? = getRootView()
        if (rootView != null) Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show()
    }
    open fun getRootView(): View? {
        val contentViewGroup = activity?.findViewById<View>(android.R.id.content) as ViewGroup
        var rootView: View? = null
        if (contentViewGroup != null) rootView = contentViewGroup.getChildAt(0)
        if (rootView == null) rootView = activity?.window?.decorView?.rootView
        return rootView
    }

    override fun onProgressUpdate(percentage: Int) {
    }

    fun validateEmailAddr(): Boolean {
        val email = binding.edtEmailId.text.toString().trim()
        if (!TextUtils.isEmpty(email)) {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmailId.error = null
                // binding.tvErrEmail.visibility = View.GONE
                return true
            } else {
                //  binding.edtEmailId.requestFocus()
                binding.tvErrEmail.visibility = View.VISIBLE
                binding.tvErrEmail.setText("Please enter valid email address")
                return false
            }
        } else {
            binding.tvErrEmail.visibility = View.VISIBLE
            binding.tvErrEmail.setText(R.string.error_email)
            return false
        }
    }
}