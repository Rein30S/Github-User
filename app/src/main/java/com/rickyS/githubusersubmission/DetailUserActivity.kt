package com.rickyS.githubusersubmission

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rickyS.githubusersubmission.adapter.SectionPagerAdapter
import com.rickyS.githubusersubmission.api.DetailGitHubResponse
import com.rickyS.githubusersubmission.database.SettingPreferences
import com.rickyS.githubusersubmission.databinding.ActivityDetailUserBinding
import com.rickyS.githubusersubmission.viewmodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailGitHubViewModel
    private var username: String? = null
    var _fav = false
    private var DarkMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailGitHubViewModel::class.java]

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        username = intent.getStringExtra(EXTRADATA)
        if (username != null) {
            if (savedInstanceState == null) {
                viewModel.setDetailUser(username.toString())
            }
        }

        viewModel.detailUser.observe(this){ item ->
            setDetail(item)
        }

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        sectionsPagerAdapter.username = username ?: ""

        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        settingViewModel.getThemeSettings().observe(this@DetailUserActivity) { Dark: Boolean ->
            val theme = if (Dark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(theme)
            DarkMode = Dark
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setDetail(responseBody:DetailGitHubResponse) {
        viewModel.detailUser.observe(this) { item ->
            if (item != null) {
                binding.tvName.text = item.name
                binding.tvUsername.text = item.login
                binding.tvFollowing.text = item.following.toString() + " Following"
                binding.tvFollower.text = item.followers.toString() + " Followers"
                Glide.with(this@DetailUserActivity)
                    .load(item.avatarUrl)
                    .into(binding.ImgAvatar)
            }
        }
        getFav(responseBody.login.toString(),responseBody.avatarUrl.toString())
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getFav(login: String, avatar: String) {
        val username = intent.getStringExtra(EXTRADATA).toString()
        val FavViewModel = obtainViewModel(this@DetailUserActivity)
        CoroutineScope(Dispatchers.IO).launch {
            val vCheckUser = FavViewModel.checkUserFav(username)
            withContext(Dispatchers.Main) {
                if (vCheckUser.isNotEmpty()) {
                    _fav = true
                    binding.fabFavorite.isSelected = true
                    binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
                } else {
                    _fav = false
                    binding.fabFavorite.isSelected = false
                    binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                }
            }
        }
        binding.fabFavorite.setOnClickListener {
            if (_fav) {
                FavViewModel.delete(login)
                _fav = false
                binding.fabFavorite.isSelected = false
                binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
            } else {
                FavViewModel.insert(login, avatar)
                binding.fabFavorite.isSelected = true
                binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
            }
        }
    }

    private fun obtainViewModel(detailUserActivity: DetailUserActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(detailUserActivity.application)
        return ViewModelProvider(detailUserActivity, factory)[FavoriteViewModel::class.java]
    }

    companion object {
        const val EXTRADATA = "extra_data"

        @StringRes
        val TAB_TITLES = intArrayOf(
            R.string.follower_text,
            R.string.following_text
        )
    }
}