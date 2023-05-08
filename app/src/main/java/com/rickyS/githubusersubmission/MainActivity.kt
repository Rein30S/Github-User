package com.rickyS.githubusersubmission

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView;
import com.rickyS.githubusersubmission.adapter.UserAdapter
import com.rickyS.githubusersubmission.api.ItemsItem
import com.rickyS.githubusersubmission.database.SettingPreferences
import com.rickyS.githubusersubmission.databinding.ActivityMainBinding
import com.rickyS.githubusersubmission.viewmodel.MainViewModel
import com.rickyS.githubusersubmission.viewmodel.SettingViewModel
import com.rickyS.githubusersubmission.viewmodel.SettingViewModelFactory
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var DarkMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Github User"

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        viewModel.user.observe(this){GitHubResponse ->
            setUser(GitHubResponse)}


        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, 0)
        binding.rvUser.addItemDecoration(itemDecoration)

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        settingViewModel.getThemeSettings().observe(this@MainActivity) { Dark: Boolean ->
            val theme = if (Dark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(theme)
            DarkMode = Dark
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val mainviewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        var searchJob: Job? = null

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                mainviewModel.searchUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchJob?.cancel()
                searchJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(300)
                    if(newText.isNotEmpty()){
                        mainviewModel.searchUser(newText)
                    }
                }
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menuFavorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menuSetting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return true
        }
    }

    private fun setUser(githubResponse: List<ItemsItem>) {
        val listUser = githubResponse.map {
            ItemsItem(it.login, it.avatarUrl)
        }
        val adapter = UserAdapter(listUser)
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}