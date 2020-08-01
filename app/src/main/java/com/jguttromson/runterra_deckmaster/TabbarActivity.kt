package com.jguttromson.runterra_deckmaster

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.tabbar_layout.*


class TabbarActivity: FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.tabbar_layout)

        bottomNavigation.setupWithNavController(Navigation.findNavController(this, R.id.my_nav_host_fragment))
    }
}