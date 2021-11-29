package com.example.sample_app.ui.homemain.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.example.sample_app.R
import com.example.sample_app.databinding.ActivityHomeBinding
import com.example.sample_app.ui.home.view.HomeFragment
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.sample_app.ui.history.view.HistoryFragment
import androidx.appcompat.app.AlertDialog
import com.example.sample_app.app.App
import android.content.SharedPreferences





class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = "Hello Toolbar"
        val pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putString(App.isLogin,"1")

        // Initialize the action bar drawer toggle instance
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ) {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                //toast("Drawer closed")
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                //toast("Drawer opened")
            }
        }


        // Configure the drawer layout to add listener and show icon on toolbar
        drawerToggle.isDrawerIndicatorEnabled = true
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val homeFragment = HomeFragment()
        loadFragment(homeFragment)

        binding.navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.action_home -> {
                    val homeFragment = HomeFragment()
                    loadFragment(homeFragment)
                }
                R.id.action_history -> {
                    val historyFragment = HistoryFragment()
                    loadFragment(historyFragment)
                }

                R.id.action_logout -> {
                    alertLogout()
                }
            }

            false
        })

    }

    private fun alertLogout() {

        val builder = AlertDialog.Builder(this)

        // Create the AlertDialog

        //set title for alert dialog
        builder.setTitle(R.string.dialogTitle)
        //set message for alert dialog
        builder.setMessage(R.string.dialogMessage)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            dialogInterface.dismiss()

            finishAffinity()
        }

        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()

        }
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    fun loadFragment(frag: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, frag) // replace a Fragment with Frame Layout
        transaction.commit() // commit the changes
        binding.drawerLayout.closeDrawers()
    }
}