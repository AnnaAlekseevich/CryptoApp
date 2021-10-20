package com.test.cryptoapp.ui.fragments.settings

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.test.cryptoapp.BuildConfig
import com.test.cryptoapp.R
import com.test.cryptoapp.databinding.FragmentSettingsBinding
import com.test.cryptoapp.domain.db.DatabaseBuilder
import com.test.cryptoapp.domain.db.DatabaseHelperImpl
import com.test.cryptoapp.ui.factories.SettingFragmentViewModelFactory
import pl.tajchert.nammu.Nammu
import pl.tajchert.nammu.PermissionCallback
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class FragmentSettings : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var settingViewModel: FragmentSettingsViewModel
    private lateinit var itemSave: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        setupViewModel()

        with(binding) {
            imageProfile.setOnClickListener {
                Log.d("AddPhoto", "binding.imageProfile.setOnClickListener")
                showPopupPhoto()
            }
            birthDay.setOnClickListener {
                showInputPicker()
            }
            FirstName.addTextChangedListener(textWatcherFirstName)
            LastName.addTextChangedListener(textWatcherLastName)
        }

        setUserData()
        Nammu.init(requireContext())
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings_menu, menu)
        itemSave = menu.findItem(R.id.save)
        settingViewModel.savingEnabledLiveData.observe(viewLifecycleOwner, {
            itemSave.isEnabled = it
        })
        super.onCreateOptionsMenu(menu, inflater)
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
        builder.setTitle(resources.getString(R.string.popup_menu_download_photo))
        val choosingImage = arrayOf(
            resources.getString(R.string.popup_menu_take_a_picture),
            resources.getString(R.string.popup_menu_pick_from_gallery)
        )
        builder.setItems(choosingImage) { _, which ->
            when (which) {
                0 -> { /* camera */
                    openCameraForCreatingPhoto()
                }
                1 -> { /* gallery */
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
        }
        settingViewModel.setPhotoUser(pictureUri.toString())
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

    private fun showInputPicker() {
        val builder: MaterialDatePicker.Builder<Long> =
            MaterialDatePicker.Builder.datePicker()
        builder.setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
        builder.setTitleText(resources.getString(R.string.media_data_picker_date_of_birth))
        val picker: MaterialDatePicker<*> = builder.build()
        picker.show(childFragmentManager, picker.toString())
        picker.addOnNegativeButtonClickListener {
            Toast.makeText(context,"Cancel", Toast.LENGTH_SHORT).show()
        }
        picker.addOnPositiveButtonClickListener {

            val date = it //  single select date
            onDateSelected(date as Long)

        }
    }

    private fun onDateSelected(dateTimeStampInMillis: Long) {
        val dateTime: LocalDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(dateTimeStampInMillis),
                ZoneId.systemDefault()
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val dateAsFormattedText: String = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        settingViewModel.setDateOfBirth(dateAsFormattedText)
        binding.birthDay.setText(dateAsFormattedText)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                settingViewModel.onSave()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewModel() {
        settingViewModel = ViewModelProviders.of(
            this,
            SettingFragmentViewModelFactory(
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(FragmentSettingsViewModel::class.java)
    }

    private val textWatcherFirstName = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            settingViewModel.setFirstName(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    private val textWatcherLastName = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            settingViewModel.setLastName(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }

    private fun setUserData() {
        settingViewModel.isUserExistLiveData.observe(viewLifecycleOwner, {
            if (it == true) {
                val userData = settingViewModel.getUser()
                with(binding) {
                    setBigImage(userData.authorPhotoUrl?.toUri())
                    FirstName.setText(userData.firstName, TextView.BufferType.EDITABLE)
                    LastName.setText(userData.lastName, TextView.BufferType.EDITABLE)
                    birthDay.setText(userData.dateOfBirth, TextView.BufferType.EDITABLE)
                }
            }
        })
    }

}


