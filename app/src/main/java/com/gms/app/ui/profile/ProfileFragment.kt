package com.gms.app.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.opengl.ETC1.encodeImage
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gms.app.R
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.remote.model.profile.ProfileBody
import com.gms.app.data.storage.remote.model.profile.UserDataModel
import com.gms.app.databinding.ProfileFragmentBinding
import com.gms.app.repo.profile.UploadImageRepo
import com.gms.app.ui.main.home.HomeVM
import com.gms.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    lateinit var binding: ProfileFragmentBinding
    private val viewModel: ProfileVM by viewModels()
    private val updateProfileVM: UpdateProfileVM by viewModels()
    private val uploadImageVM: UploadImageVM by viewModels()

    private var dialogBuilder: AlertDialog.Builder? = null
    private var alertDialog: AlertDialog? = null
    private var cameraImageUri: Uri? = null

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observeEvent(viewLifecycleOwner) { onResponse(it) }
        updateProfileVM.uiState.observeEvent(viewLifecycleOwner) { onUpdateResponse(it) }
        uploadImageVM.uiState.observeEvent(viewLifecycleOwner, { onImageUploaded(it) })
        setUpToolbar()
        setUpViewsClicks()
        viewModel.getUserData()
    }

    private fun setUpToolbar() {
        binding.toolbar.titleTvToolbar.text =
            requireActivity().resources.getString(R.string.profile_label)
    }

    private fun setUpViewsClicks() {
        binding.toolbar.backImgToolbar.setOnClickListener { findNavController().navigateUp() }
       // binding.profileImg.setOnClickListener { chooseImages() }
        binding.editBtn.setOnClickListener { validation() }
    }

    private fun validation() {
        val userDate = viewModel.userDataModel
        val userName = binding.userNameEt.text.trim().toString()
        val password = binding.passwordEt.text.trim().toString()
        val email = binding.userEmailEt.text.trim().toString()
        val address = binding.addressEt.text.trim().toString()
        val phone = binding.phoneEt.text.trim().toString()


        if (userName.isBlank()) {
            binding.userNameEt.error = getString(R.string.required_field)
            return
        }
        if (password.isBlank()) {
            binding.passwordEt.error = getString(R.string.required_field)
            return
        }
//        if (password.length < 8) {
//            binding.passwordEt.error = getString(R.string.password_validation)
//            return
//        }
        if (email.isBlank()) {
            binding.userEmailEt.error = getString(R.string.required_field)
            return
        }
        if (!email.isValidEmail()) {
            binding.userEmailEt.error = getString(R.string.email_validation)
            return
        }
        if (address.isBlank()) {
            binding.addressEt.error = getString(R.string.required_field)
            return
        }
        if (phone.isBlank()) {
            binding.phoneEt.error = getString(R.string.required_field)
            return
        }
        val body = ProfileBody(
            userId = preferencesHelper.userId,
            name = userName,
            birthDate = userDate?.dateOfBirth!!,
            email = email,
            phone = phone,
            contact = userDate.contact,
            genderId = userDate.genderId,
            nationalityId = userDate.nationalityId,
            countryId = userDate.countryId,
            livingId = userDate.livingId,
            education = userDate.education,
            university = userDate.university,
            graduationYear = userDate.graduationYear.toInt(),
            address = address,
            note = userDate.note,
            password = password,
            img = (if (uploadImageVM.imageName == null) userDate.picture else uploadImageVM.imageName)!!,
        )
        updateProfileVM.updateProfile(body)
    }

    private fun fillData() {
        val model = viewModel.userDataModel
        if (model != null) {
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_user_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)

            Glide.with(requireActivity()).load(Constants.ImagesUrl + model.picture)
                .apply(options)
                .into(binding.profileImg)
            binding.userNameEt.setText(model.fullName)
            binding.passwordEt.setText(model.password)
            binding.addressEt.setText(model.address)
            binding.userEmailEt.setText(model.email)
            binding.phoneEt.setText(model.phone)
            binding.dateOfBirthEt.setText(model.dateOfBirth)
            binding.livingEt.setText(model.living)
            binding.countryEt.setText(model.country)
        }

    }

    @AfterPermissionGranted(Constants.RC_PERMISSION_STORAGE_CAMERA)
    private fun chooseImages() {
        val perms = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (EasyPermissions.hasPermissions(requireActivity(), *perms)) {
            showChooseDialog()
        } else {
            EasyPermissions.requestPermissions(
                this, resources.getString(R.string.permissions_needed),
                Constants.RC_PERMISSION_STORAGE_CAMERA, *perms
            )
        }
    }

    private fun showChooseDialog() {
        dialogBuilder = AlertDialog.Builder(requireActivity())
        val layoutView = layoutInflater.inflate(R.layout.gallery_camera_choose_view, null)
        val cameraBtn = layoutView.findViewById<View>(R.id.camera_btn)
        val galleryBtn = layoutView.findViewById<View>(R.id.gallery_btn)
        dialogBuilder!!.setView(layoutView)
        alertDialog = dialogBuilder!!.create()
        alertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog!!.show()
        cameraBtn.setOnClickListener {
            cameraImageUri = openCamera()
            alertDialog!!.dismiss()
        }
        galleryBtn.setOnClickListener {
            openGallery(false)
            alertDialog!!.dismiss()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.RC_CAPTURE_IMAGE -> {
                    cameraImageUri?.let {
                        onImageSelected(it)
                    }
                }
                Constants.RC_IMAGES -> {
                    val uri = data?.data
                    when {
                        uri != null -> onImageSelected(uri)
                        else -> Toast.makeText(
                            activity,
                            getString(R.string.error_general),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }
    }


    private fun onImageSelected(imageUri: Uri) {
        val path = requireContext().getFilePathForN(imageUri)
        if (path != null) {
            Glide.with(requireContext()).load(path).into(binding.profileImg)
            val bm = BitmapFactory.decodeStream(FileInputStream(File(path)))
            uploadImageVM.imageByte = requireActivity().encodeImage(bm)
            uploadImageVM.imageName = path.substring(path.lastIndexOf("/") + 1)
            uploadImageVM.uploadImage()
        } else
            activity?.toast(getString(R.string.error_select_image))
    }


    private fun onResponse(states: UiStates?) {
        when (states) {
            UiStates.Loading -> {
                onLoading()
            }
            UiStates.Success -> {
                fillData()
                viewInputs()
            }
            UiStates.Empty -> {
                viewInputs()
            }
            UiStates.NotFound -> {
                onNotFound()
            }
            UiStates.NoConnection -> {
                onNoConnection()
            }
        }
    }

    private fun onUpdateResponse(states: UiStates?) {
        when (states) {
            UiStates.Loading -> {
                onLoading()
            }
            UiStates.Success -> {
                onSuccess()
                findNavController().navigateUp()
            }
            UiStates.Error -> {
                activity?.snackBar(updateProfileVM.message, binding.viewRoot)
                viewInputs()
            }
            UiStates.NotFound -> {
                onNotFound()
            }
            UiStates.NoConnection -> {
                onNoConnection()
            }
        }
    }

    private fun onImageUploaded(states: UiStates?) {
        when (states) {
            UiStates.Loading -> {
                onLoading()
            }
            UiStates.Success -> {
                onSuccess()
            }
            UiStates.Error -> {
                activity?.snackBar(
                    requireActivity().resources.getString(R.string.error_upload_image),
                    binding.viewRoot
                )
                viewInputs()
            }
            UiStates.NotFound -> {
                activity?.snackBar(
                    requireActivity().resources.getString(R.string.error_upload_image),
                    binding.viewRoot
                )
                viewInputs()
            }
        }
    }

    private fun onSuccess() {
        activity?.snackBar(requireActivity().getString(R.string.success), binding.viewRoot)
        requireActivity().hideKeyboard()
        //viewModel.getUserData()
    }

    private fun onLoading() {
        binding.viewContainer.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.VISIBLE
    }

    private fun viewInputs() {
        binding.viewContainer.visibility = View.VISIBLE
        binding.loadingLayout.root.visibility = View.GONE
    }

    private fun onNoConnection() {
        binding.viewContainer.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.GONE
        binding.loadingLayout.noConnection.visibility = View.VISIBLE
    }

    private fun onNotFound() {
        binding.viewContainer.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.GONE
        binding.loadingLayout.error.visibility = View.VISIBLE
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        activity?.snackBar(resources.getString(R.string.permissions_needed), binding.viewRoot)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}