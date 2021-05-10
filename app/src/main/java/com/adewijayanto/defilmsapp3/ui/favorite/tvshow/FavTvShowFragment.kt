package com.adewijayanto.defilmsapp3.ui.favorite.tvshow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adewijayanto.defilmsapp3.R
import com.adewijayanto.defilmsapp3.data.entity.TvShowCatalogueEntity
import com.adewijayanto.defilmsapp3.databinding.FragmentFavTvShowBinding
import com.adewijayanto.defilmsapp3.ui.ItemClickCallbackTvShow
import com.adewijayanto.defilmsapp3.ui.detail.tvshow.DetailTvShowActivity
import com.adewijayanto.defilmsapp3.ui.favorite.movie.MovieFavViewModel
import com.adewijayanto.defilmsapp3.ui.tvshow.TvShowAdapter
import com.adewijayanto.defilmsapp3.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class FavTvShowFragment : Fragment(), FavTvShowAdapter.OnItemClickCallback {

    private var _fragmentTvShowBinding: FragmentFavTvShowBinding? = null
    private val binding get() = _fragmentTvShowBinding

    private lateinit var viewModel: TvShowFavViewModel
    private lateinit var adapter: FavTvShowAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentTvShowBinding = FragmentFavTvShowBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavTvShows().observe(viewLifecycleOwner, { favTvShow ->
            if (favTvShow != null) {
                adapter.submitList(favTvShow)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemTouchHelper.attachToRecyclerView(binding?.rvFavTvshow)

        if (activity != null) {
            val factory = ViewModelFactory.getInstance(requireContext())
            viewModel = ViewModelProvider(this, factory)[TvShowFavViewModel::class.java]

            adapter = FavTvShowAdapter()
            adapter.setOnItemClickCallback(this)

            viewModel.getFavTvShows().observe(viewLifecycleOwner, { favTvShow ->
                if (favTvShow != null) {
                    adapter.submitList(favTvShow)
                    Log.d("Favorite Tv Show", favTvShow.toString())
                }
            })

            with(binding?.rvFavTvshow) {
                this?.layoutManager = LinearLayoutManager(context)
                this?.setHasFixedSize(true)
                this?.adapter = adapter
            }
        }
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int =
            makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (view != null) {
                val swipedPosition = viewHolder.adapterPosition
                val tvShowEntity = adapter.getSwipedData(swipedPosition)
                tvShowEntity?.let { viewModel.setFavTvShow(it) }
            }
        }
    })

    override fun onItemClicked(id: String) {
        val intent = Intent(context, DetailTvShowActivity::class.java)
        intent.putExtra(DetailTvShowActivity.EXTRA_ID, id)

        context?.startActivity(intent)
    }

}