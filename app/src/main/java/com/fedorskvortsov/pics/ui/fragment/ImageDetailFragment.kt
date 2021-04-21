package com.fedorskvortsov.pics.ui.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fedorskvortsov.pics.R
import com.fedorskvortsov.pics.databinding.FragmentImageDetailBinding
import com.fedorskvortsov.pics.domain.util.themeColor
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import timber.log.Timber

class ImageDetailFragment : Fragment() {
    private var _binding: FragmentImageDetailBinding? = null
    private val binding get() = _binding!!

    private val args: ImageDetailFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        Timber.d("onAttach()")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.navHostFragment
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("onCreateView()")
        _binding = FragmentImageDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
        Glide.with(binding.root.context)
            .load(args.url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.root)

        controlWindowInsets(true)
        var hide = false
        binding.root.setOnClickListener {
            hide = if (hide) {
                controlWindowInsets(true)
                false
            } else {
                controlWindowInsets(false)
                true
            }
        }
    }

    override fun onDestroyView() {
        Timber.d("onDestroyView()")
        super.onDestroyView()
        controlWindowInsets(false)
        _binding = null
    }

    private fun controlWindowInsets(hide: Boolean) {
        val window = requireActivity().window
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        val behavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        val type = WindowInsetsCompat.Type.systemBars()
        insetsController.systemBarsBehavior = behavior

        if (hide) {
            (activity as AppCompatActivity).supportActionBar?.hide()
            insetsController.hide(type)
        } else {
            (activity as AppCompatActivity).supportActionBar?.show()
            insetsController.show(type)
        }
    }
}
