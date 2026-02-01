package com.ssti.dharmendrapractical.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.ssti.dharmendrapractical.R
import com.ssti.dharmendrapractical.databinding.DialogBoxProfileUpdateBinding
import com.ssti.dharmendrapractical.databinding.FragmentDetilsBinding
import com.ssti.dharmendrapractical.ui.adapter.ProfileAdapter
import com.ssti.dharmendrapractical.ui.common.BaseFragment
import com.ssti.dharmendrapractical.utils.GeneralFunctions
import com.ssti.dharmendrapractical.viewmodel.ProfileViewModel
import com.ssti.dharmendrapractical.utils.showAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetilsFragment : BaseFragment(R.layout.fragment_detils) {
    private lateinit var mBinding: FragmentDetilsBinding
    private val viewModel: ProfileViewModel by viewModels()

    private val profileAdapter by lazy {
        ProfileAdapter { position, viewId ->
            val user = viewModel.usersList.value[position]
            
            if (viewId == R.id.imgEdit) {
                showUpdateDialog(user)
            } else if (viewId == R.id.imgDelete) {
                context?.showAlertDialog(
                    title = "Alert",
                    message = "Are you sure want to delete ${user.name}?",
                    positiveButtonText = "Yes",
                    positiveOnClick = {
                        viewModel.deleteUser(user.id)
                    },
                    negativeButtonText = "No",
                    negativeOnClick = {},
                    neutralOnClick = {}
                )
            }
        }
    }

    private fun showUpdateDialog(user: com.ssti.dharmendrapractical.data.local.ProfileEntity) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogBoxProfileUpdateBinding.inflate(layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        user.imageUri?.let { image ->
            GeneralFunctions.loadImage(requireContext(), image, dialogBinding.imgProfile)
        }

        // Set current values
        dialogBinding.etName.setText(user.name)
        dialogBinding.etEmail.setText(user.email)
        dialogBinding.etPassword.setText(user.password)

        dialogBinding.btnUpdate.setOnClickListener {
            val name = dialogBinding.etName.text.toString().trim()
            val email = dialogBinding.etEmail.text.toString().trim()
            val password = dialogBinding.etPassword.text.toString().trim()

            if (name.isEmpty()) {
                dialogBinding.tilName.error = "Name is required"
                return@setOnClickListener
            } else {
                dialogBinding.tilName.error = null
            }

            if (email.isEmpty()) {
                dialogBinding.tilEmail.error = "Email is required"
                return@setOnClickListener
            } else {
                dialogBinding.tilEmail.error = null
            }

            if (password.isEmpty()) {
                dialogBinding.tilPassword.error = "Password is required"
                return@setOnClickListener
            } else {
                dialogBinding.tilPassword.error = null
            }

            // Update user
            viewModel.updateUser(user.id, name, email, password)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentDetilsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeUsers()
        viewModel.getUsers()
    }

    private fun setupRecyclerView() {
        mBinding.rvProfile.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = profileAdapter
        }
    }

    private fun observeUsers() {
        lifecycleScope.launch {
            viewModel.usersList.collect { users ->
                profileAdapter.setData(users)
            }
        }
    }
}