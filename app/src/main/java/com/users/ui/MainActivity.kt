package com.users.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.users.R
import com.users.adapter.UserAdapter
import com.users.database.UserDao
import com.users.database.UserDatabase
import com.users.databinding.ActivityMainBinding
import com.users.model.UserResponse
import com.users.utils.Utility
import com.users.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), UserAdapter.SelectedItem {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userDao: UserDao
    private lateinit var thread: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDao = UserDatabase.getInstance(this).userDao()
        initToolbar()
        initLiveData()
    }

    /**
     * This is method is used to initialize the toolbar.
     */
    private fun initToolbar() {
        setSupportActionBar(binding.commonToolbar.toolBar)
        binding.commonToolbar.tvTitle.text = getString(R.string.userList)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val toolbar = binding.commonToolbar.toolBar
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    /**
     * This is method is used to initialize the viewmodel and observe the data.
     */
    private fun initLiveData() {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        if (Utility.isNetworkAvailable(this))
            mainViewModel.getUsers()
        else{
           mainViewModel.getUserList(userDao).observe(this){
               binding.rvUsers.layoutManager = LinearLayoutManager(this)
               binding.rvUsers.adapter = UserAdapter(it, this)
           }
        }

        mainViewModel.isLoading.observe(this) {
            if (it) {
                binding.progressCircular.visibility = View.VISIBLE
            } else {
                binding.progressCircular.visibility = View.GONE
            }
        }

        mainViewModel.errorData.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        mainViewModel.userData.observe(this) {
            binding.rvUsers.layoutManager = LinearLayoutManager(this)
            binding.rvUsers.adapter = UserAdapter(it.data, this)
            mainViewModel.updateDatabase(userDao,it)
        }
    }

    /**
     * This is method is used to handle click listener of the adapter.
     */
    override fun onItemSelect(id: Int) {
        if(Utility.isNetworkAvailable(this)){
            Intent(this, UserDetailActivity::class.java).also {
                it.putExtra("ID", id)
                startActivity(it)
            }
        }
        else{
            Toast.makeText(this,getString(R.string.connectionError),Toast.LENGTH_SHORT).show()
        }


    }
}