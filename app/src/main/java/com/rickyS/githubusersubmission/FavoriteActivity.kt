package com.rickyS.githubusersubmission

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rickyS.githubusersubmission.adapter.UserAdapter
import com.rickyS.githubusersubmission.api.ItemsItem
import com.rickyS.githubusersubmission.database.favorite
import com.rickyS.githubusersubmission.databinding.ActivityFavoriteBinding
import com.rickyS.githubusersubmission.viewmodel.FavoriteViewModel
import com.rickyS.githubusersubmission.viewmodel.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private var list = ArrayList<ItemsItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getFav()
    }

    private fun getFav() {
        val viewModel = obtainViewModel(this@FavoriteActivity)
        viewModel.getAllFav().observe(this) { userData ->
            if (userData != null) {
                binding.rvFav.visibility = View.VISIBLE
                setDataFav(userData)
            }
        }
    }

    private fun setDataFav(userData: List<favorite>) {
        list.clear()
        for (data in userData) {
            val mFollowFragment = ItemsItem(
                data.login,
                data.avatar_url ?: ""
            )
            list.add(mFollowFragment)
        }
        showRecyclerList()
    }

    private fun showRecyclerList() {
        binding.apply {
            rvFav.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            val listFavAdapter = UserAdapter(list)
            rvFav.adapter = listFavAdapter
        }
    }

    private fun obtainViewModel(favoriteActivity: FavoriteActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(favoriteActivity.application)
        return ViewModelProvider(favoriteActivity, factory)[FavoriteViewModel::class.java]
    }
}