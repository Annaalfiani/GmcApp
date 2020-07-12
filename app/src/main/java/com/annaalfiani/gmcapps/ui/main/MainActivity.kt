package com.annaalfiani.gmcapps.ui.main

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.annaalfiani.gmcapps.ui.intro.IntroActivity
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.ui.main.home.HomeFragment
import com.annaalfiani.gmcapps.ui.main.profile.ProfileFragment
import com.annaalfiani.gmcapps.ui.main.ticket.TicketFragment
import com.annaalfiani.gmcapps.utils.Utilities
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{ var navStatus = -1 }
    private var fragment : Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        if(savedInstanceState == null){
            navigation.selectedItemId =
                R.id.navigation_home
        }
        Thread(Runnable {
            if (Utilities.isFirstTime(this@MainActivity)) {
                runOnUiThread { startActivity(Intent(this@MainActivity, IntroActivity::class.java).also {
                    finish()
                })}
            }
        }).start()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.navigation_home -> {
                if(navStatus != 0){
                    fragment =
                        HomeFragment()
                    navStatus = 0
                }
            }
            R.id.navigation_ticket -> {
                if(navStatus != 1){
                    fragment =
                        TicketFragment()
                    navStatus = 1
                }
            }
            R.id.navigation_profil -> {
                if(navStatus != 2){
                    fragment =
                        ProfileFragment()
                    navStatus = 2
                }
            }
        }
        if(fragment == null){
            navStatus = 0
            fragment = HomeFragment()
        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.screen_container, fragment!!)
        fragmentTransaction.commit()
        true
    }
}
