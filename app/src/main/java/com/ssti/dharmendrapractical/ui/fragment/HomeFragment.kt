package com.ssti.dharmendrapractical.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssti.dharmendrapractical.R
import com.ssti.dharmendrapractical.databinding.FragmentHomeBinding
import com.ssti.dharmendrapractical.ui.adapter.CartAdapter
import com.ssti.dharmendrapractical.utils.Resource
import com.ssti.dharmendrapractical.viewmodel.HomeViewModel
import com.ssti.dharmendrapractical.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val cartAdapter by lazy {
        CartAdapter { product ->
            findNavController().navigate(R.id.action_homeFragment_to_detilsFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        setupRecyclerView()
        setupSwipeRefresh()
        observeData()
        viewModel.fetchCartList()
    }

    private fun setupRecyclerView() {
        binding.rvCarts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchCartList()
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.cartList.collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Show shimmer or progress
                        if (!binding.swipeRefreshLayout.isRefreshing) {
                            binding.swipeRefreshLayout.isRefreshing = true
                        }
                    }
                    is Resource.Success -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        resource.data?.let { cartsResponse ->
                            // Flatten all products from all carts into a single list
                            val allProducts = cartsResponse.carts.flatMap { cart ->
                                cart.products
                            }
                            cartAdapter.setData(allProducts)
                            Log.d("HomeFragment", "Loaded ${allProducts.size} products from ${cartsResponse.carts.size} carts")
                        }
                    }
                    is Resource.Error -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        requireContext().showToast("Error: ${resource.message}")
                    }
                    else -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}