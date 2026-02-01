package com.ssti.dharmendrapractical.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ssti.dharmendrapractical.databinding.ActivityMainBinding
import com.ssti.dharmendrapractical.ui.common.BaseActivity
import com.ssti.dharmendrapractical.utils.Resource
import com.ssti.dharmendrapractical.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        observeLoginState()
        setupClickListeners()
    }

    // ✅ Observe StateFlow safely
    private fun observeLoginState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collect { resource ->
                    when (resource) {
                        is Resource.Idle -> {
                          //  binding.progressBar.hide()
                        }


                        is Resource.Loading -> {
                          //  binding.progressBar.show()
                        }

                        is Resource.Success -> {
                          //  binding.progressBar.hide()
                            val user = resource.data
                            Toast.makeText(
                                this@MainActivity,
                                "Welcome ${user?.name}",
                                Toast.LENGTH_SHORT
                            ).show()

                            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                            finish()
                        }

                        is Resource.Error -> {
                         //   binding.progressBar.hide()
                            Toast.makeText(
                                this@MainActivity,
                                resource.message ?: "Login failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is Resource.NoInternet -> {
                          //  binding.progressBar.hide()
                            Toast.makeText(
                                this@MainActivity,
                                "No Internet Connection",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    // ✅ Trigger login from UI
    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email & password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(email, password)
        }
    }
}
