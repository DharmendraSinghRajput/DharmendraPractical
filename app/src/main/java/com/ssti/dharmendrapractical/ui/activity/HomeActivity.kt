package com.ssti.dharmendrapractical.ui.activity

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ssti.dharmendrapractical.R
import com.ssti.dharmendrapractical.databinding.ActivityHomeBinding
import com.ssti.dharmendrapractical.ui.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {
    val mBinding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding.apply {
            setupNavController()

        }

    }

    private fun setupNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
    }
}