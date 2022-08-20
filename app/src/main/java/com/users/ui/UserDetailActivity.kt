package com.users.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.users.R
import com.users.database.UserDao
import com.users.database.UserDatabase
import com.users.databinding.ActivityUserDetailBinding
import com.users.model.Data
import com.users.service.TimerService
import com.users.utils.Utility
import com.users.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private var id: Int = 0
    private lateinit var userViewModel: UserViewModel
    private var timerService: TimerService? = null
    private lateinit var userDao: UserDao
    private val TAG: String = UserDetailActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDao = UserDatabase.getInstance(this).userDao()
        initToolbar()
        getIntentValues()
        initViewModel()
    }

    /**
     * This is method is used to initialize the toolbar.
     */
    private fun initToolbar() {
        setSupportActionBar(binding.commonToolbar.toolBar)
        binding.commonToolbar.tvTitle.text = getString(R.string.userDetails)
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
    private fun initViewModel() {
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        if (Utility.isNetworkAvailable(this))
            userViewModel.getUserDetails(id)

        userViewModel.isLoading.observe(this) {
            if (it)
                binding.progressCircular.visibility = View.VISIBLE
            else
                binding.progressCircular.visibility = View.GONE

        }

        userViewModel.errorData.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        userViewModel.userData.observe(this) {
            updateData(it.data)
        }

    }

    /**
     * This is method is used to update the data on views.
     */
    private fun updateData(data: Data) {
        if (data.avatar.isNotEmpty()) {
            Glide.with(this)
                .load(data.avatar)
                .into(binding.ivImageView)
        }
        binding.tvId.text = String.format("%s %s", "ID: ", data.id)
        binding.tvEmail.text = String.format("%s %s", "Email: ", data.email)
        binding.tvName.text =
            String.format("%s %s", "Name: ", data.first_name + " " + data.last_name)
        timerService?.startTask()
        getCurrentValue()
    }

    /**
     * This is method is used to get the data from bound service.
     */
    private fun getCurrentValue() {
        val handler = Handler(Looper.myLooper()!!)
        val runnable: Runnable = object : Runnable {

            override fun run() {
                handler.postDelayed(this, 1000)
                binding.tvTimer.text = timerService?.getCurrentValue().toString()
                if (timerService?.getCurrentValue() == 0) {
                    handler.removeCallbacks(this)
                    finish()
                }
            }

        }
        handler.postDelayed(runnable, 1000)
    }

    /**
     * This is method is used to get the Intent values.
     */
    private fun getIntentValues() {
        id = intent.getIntExtra("ID", 0)
    }

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "onServiceConnected: ")
            val binder = service as TimerService.TimerBinder
            timerService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "onServiceDisconnected: ")
        }

    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
    }

    override fun onStart() {
        super.onStart()
        val boundServiceIntent = Intent(this, TimerService::class.java)
        startService(boundServiceIntent)
        bindService(boundServiceIntent, serviceConnection, BIND_AUTO_CREATE)
    }
}