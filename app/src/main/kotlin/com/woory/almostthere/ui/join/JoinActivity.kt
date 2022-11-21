package com.woory.almostthere.ui.join

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.woory.almostthere.R
import com.woory.almostthere.databinding.ActivityJoinBinding
import com.woory.almostthere.ui.ActivityViewBindingDelegate

class JoinActivity : AppCompatActivity() {

    private val binding: ActivityJoinBinding by ActivityViewBindingDelegate(R.layout.activity_join)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as? NavHostFragment
                ?: return@with
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.dest_invite, R.id.dest_profile, R.id.dest_promise_detail)
        )

        toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbar.title = destination.label
        }

        setSupportActionBar(toolbar)
    }
}