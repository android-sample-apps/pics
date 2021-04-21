package com.fedorskvortsov.pics.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.fedorskvortsov.pics.R
import com.fedorskvortsov.pics.databinding.FragmentImageListBinding
import com.fedorskvortsov.pics.ui.adapter.ImageAdapter
import com.fedorskvortsov.pics.ui.state.ImageListUiState
import com.fedorskvortsov.pics.ui.viewmodel.ImageListViewModel
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class ImageListFragment : Fragment(), ImageAdapter.ImageAdapterListener {

    private var _binding: FragmentImageListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ImageListViewModel by viewModels()

    private val adapter = ImageAdapter(this)

    override fun onAttach(context: Context) {
        Timber.d("onAttach()")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("onCreateView()")
        _binding = FragmentImageListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        binding.imageList.adapter = adapter
        subscribeUi(adapter)
        initSwipeRefreshLayout()
    }

    override fun onDestroyView() {
        Timber.d("onDestroyView()")
        super.onDestroyView()

        // Prevent adapter leaking
        binding.imageList.adapter = null
        _binding = null
    }

    private fun initSwipeRefreshLayout() {
        binding.swipeRefreshLayout.apply {
            setOnRefreshListener {
                viewModel.getImages()
            }
            setColorSchemeColors(R.attr.colorPrimary)
        }
    }

    private fun subscribeUi(adapter: ImageAdapter) {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is ImageListUiState.Loading -> {
                        binding.swipeRefreshLayout.isRefreshing = true
                    }
                    is ImageListUiState.Success -> {
                        if (uiState.images.isNotEmpty()) {
                            adapter.submitList(uiState.images)
                        }
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    is ImageListUiState.Error -> {
                        Timber.e(uiState.exception)
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }
    }

    override fun onImageClicked(view: View, url: String) {
        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
        val imageDetailTransitionName = getString(R.string.image_detail_transition_name)
        val extras = FragmentNavigatorExtras(view to imageDetailTransitionName)
        val action = ImageListFragmentDirections
            .actionImageListFragmentToImageDetailFragment(url)
        findNavController().navigate(action, extras)
    }
}
