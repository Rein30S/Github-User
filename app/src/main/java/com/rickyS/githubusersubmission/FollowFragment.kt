package com.rickyS.githubusersubmission

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rickyS.githubusersubmission.adapter.UserAdapter
import com.rickyS.githubusersubmission.api.ItemsItem
import com.rickyS.githubusersubmission.databinding.FragmentFollowBinding
import com.rickyS.githubusersubmission.viewmodel.DetailGitHubViewModel
import com.rickyS.githubusersubmission.viewmodel.MainViewModel
import kotlin.math.log

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var username: String

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailGitHubViewModel::class.java)
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvFollow)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        arguments?.let {
            val position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()

            if (position == 1) {
                viewModel.getFollowerUser(username)
                viewModel.user.observe(viewLifecycleOwner) { GitHubResponse ->
                    setUserData(GitHubResponse)
                }
                Log.d(TAG, "onViewCreated: " + viewModel.user.observe(viewLifecycleOwner) { GitHubResponse ->
                    setUserData(GitHubResponse)
                })

                viewModel.isLoading.observe(viewLifecycleOwner, ::showLoading)
            } else {
                viewModel.getFollowingUser(username)
                viewModel.user.observe(viewLifecycleOwner) { GitHubResponse ->
                    setUserData(GitHubResponse)
                }
                Log.d(TAG, "onViewCreated: " + viewModel.user.observe(viewLifecycleOwner) { GitHubResponse ->
                    setUserData(GitHubResponse)
                })

                viewModel.isLoading.observe(viewLifecycleOwner, ::showLoading)
            }
        }
    }

    private fun setUserData(GitHubResponse: List<ItemsItem>) {
        val listUser = GitHubResponse.map {
            ItemsItem(it.login, it.avatarUrl)
        }
        val adapter = UserAdapter(listUser)
        binding.rvFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}