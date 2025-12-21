package com.ssti.dharmendrapractical.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ssti.dharmendrapractical.data.local.ProfileEntity
import com.ssti.dharmendrapractical.databinding.ActivityRegisterBinding
import com.ssti.dharmendrapractical.ui.common.BaseActivity
import com.ssti.dharmendrapractical.utils.Resource
import com.ssti.dharmendrapractical.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: ProfileViewModel by viewModels()
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupImagePicker()
        observeImageUri()
        setupRegisterButton()
        observeRegistrationStatus()
    }

    private fun setupImagePicker() {
        binding.imgProfile.setOnClickListener {
            showImagePickerDialog()
        }
    }

    private fun observeImageUri() {
        imageUri.observe(this) { uri ->
            uri?.let {
                selectedImageUri = it
                loadImageIntoView(it)
            }
        }
    }

    private fun loadImageIntoView(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .circleCrop()
            .placeholder(com.ssti.dharmendrapractical.R.drawable.icon_person)
            .error(com.ssti.dharmendrapractical.R.drawable.icon_person)
            .into(binding.imgProfile)
    }

    private fun setupRegisterButton() {
        binding.btnRegister.setOnClickListener {
            val user = ProfileEntity(
                name = binding.etName.text.toString(),
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString(),
                address = binding.etAddress.text.toString(),
                imageUri = selectedImageUri?.toString()
            )
            viewModel.registerUser(this, user)
        }
    }

    private fun observeRegistrationStatus() {

        lifecycleScope.launchWhenStarted {
            viewModel.registerStatus.collect { resource ->
                when (resource) {
                    is Resource.Idle -> {
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registered successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
                        finish()
                    }
                    is Resource.Error -> {
                        Toast.makeText(
                            this@RegisterActivity,
                            resource.message ?: "Registration failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Resource.NoInternet -> {
                        Toast.makeText(
                            this@RegisterActivity,
                            resource.message ?: "No internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}

