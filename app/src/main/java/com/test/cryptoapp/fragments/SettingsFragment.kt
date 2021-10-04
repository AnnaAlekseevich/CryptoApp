package com.test.cryptoapp.fragments

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.test.cryptoapp.BuildConfig
import com.test.cryptoapp.R
import com.test.cryptoapp.databinding.FragmentSettingsBinding
import pl.tajchert.nammu.Nammu
import pl.tajchert.nammu.PermissionCallback
import java.io.File


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSettingsBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        binding.imageProfile.setOnClickListener {
            Log.d("AddPhoto", "binding.imageProfile.setOnClickListener")
            showPopupPhoto()
        }

        Nammu.init(requireContext())

        return binding.root
    }

    private fun onPictureClicked() {
        if (Nammu.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            chooseImageForAvatar()
        } else {
            Nammu.askForPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, object :
                PermissionCallback {
                override fun permissionGranted() {
                    chooseImageForAvatar()
                }

                override fun permissionRefused() {
                    Toast.makeText(
                        context,
                        R.string.permission_write_never_askagain,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun showPopupPhoto() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getResources().getString(R.string.popup_menu_download_photo))
        val choosingImage = arrayOf(
            getResources().getString(R.string.popup_menu_take_a_picture),
            getResources().getString(R.string.popup_menu_pick_from_gallery)
        )
        builder.setItems(choosingImage) { dialog, which ->
            when (which) {
                0 -> { /* camera */
                    openCameraForCreatingPhoto()
                }
                1 -> { /* gallery   */
                    Log.d("AddPhoto", "onPictureClicked()")
                    onPictureClicked()
                }
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    fun chooseImageForAvatar() = selectImageFromGalleryResult.launch("image/*")


    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                setBigImage(uri)
            }
        }

    private fun setBigImage(pictureUri: Uri?) {
        pictureUri?.let {
            Glide
                .with(binding.imageProfile.context)
                .load(it)
                .into(binding.imageProfile)
            binding.iconPhoto.visibility = View.GONE
            binding.addPhoto.visibility = View.GONE
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun openCameraForCreatingPhoto() {
        if (Nammu.checkPermission(Manifest.permission.CAMERA)) {
            takeImage()
        } else {
            Nammu.askForPermission(activity, Manifest.permission.CAMERA, object :
                PermissionCallback {
                override fun permissionGranted() {
                    takeImage()
                }

                override fun permissionRefused() {
                    Toast.makeText(
                        context,
                        R.string.permission_write_never_askagain,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private var latestTmpUri: Uri? = null
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    setBigImage(uri)
                }
            }
        }

    private fun takeImage() {
        getTmpFileUri().let { uri ->
            latestTmpUri = uri
            takeImageResult.launch(uri)
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", activity?.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            requireActivity().applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

}


