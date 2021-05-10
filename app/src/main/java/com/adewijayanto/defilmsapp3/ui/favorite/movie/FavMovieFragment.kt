package com.adewijayanto.defilmsapp3.ui.favorite.movie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adewijayanto.defilmsapp3.R
import com.adewijayanto.defilmsapp3.data.entity.MovieCatalogueEntity
import com.adewijayanto.defilmsapp3.databinding.FragmentFavMovieBinding
import com.adewijayanto.defilmsapp3.databinding.FragmentFavTvShowBinding
import com.adewijayanto.defilmsapp3.databinding.FragmentMovieBinding
import com.adewijayanto.defilmsapp3.ui.ItemClickCallbackMovie
import com.adewijayanto.defilmsapp3.ui.detail.movie.DetailMovieActivity
import com.adewijayanto.defilmsapp3.ui.detail.tvshow.DetailTvShowActivity
import com.adewijayanto.defilmsapp3.ui.favorite.tvshow.FavTvShowAdapter
import com.adewijayanto.defilmsapp3.ui.favorite.tvshow.TvShowFavViewModel
import com.adewijayanto.defilmsapp3.ui.movie.MovieAdapter
import com.adewijayanto.defilmsapp3.ui.movie.MovieFragment
import com.adewijayanto.defilmsapp3.ui.movie.MovieViewModel
import com.adewijayanto.defilmsapp3.viewmodel.ViewModelFactory
import com.adewijayanto.utils.SortUtils
import com.adewijayanto.vo.StatusMessage
import com.google.android.material.snackbar.Snackbar

class FavMovieFragment : Fragment(), FavMovieAdapter.OnItemClickCallback {

    private var _fragmentTvShowBinding: FragmentMovieBinding? = null
    private val binding get() = _fragmentTvShowBinding

    private lateinit var viewModel: MovieFavViewModel
    private lateinit var adapter: FavMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentTvShowBinding = FragmentMovieBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavMovies().observe(viewLifecycleOwner, { favMovie ->
            if (favMovie != null) {
                adapter.submitList(favMovie)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemTouchHelper.attachToRecyclerView(binding?.rvMovie)

        if (activity != null) {
            val factory = ViewModelFactory.getInstance(requireContext())
            viewModel = ViewModelProvider(this, factory)[MovieFavViewModel::class.java]

            adapter = FavMovieAdapter()
            adapter.setOnItemClickCallback(this)

            viewModel.getFavMovies().observe(viewLifecycleOwner, { favMovie ->
                if (favMovie != null) {
                    adapter.submitList(favMovie)
                    Log.d("Favorite Movie", favMovie.toString())
                }
            })

            with(binding?.rvMovie) {
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
                tvShowEntity?.let { viewModel.setFavMovie(it) }
            }
        }
    })

    override fun onItemClicked(id: String) {
        val intent = Intent(context, DetailMovieActivity::class.java)
        intent.putExtra(DetailMovieActivity.EXTRA_ID, id)

        context?.startActivity(intent)
    }

}