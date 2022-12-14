package com.woory.almostthere.presentation.ui.creatingpromise

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.fragment.NavHostFragment
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.databinding.ActivityCreatingPromiseBinding
import com.woory.almostthere.presentation.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatingPromiseActivity :
    BaseActivity<ActivityCreatingPromiseBinding>(R.layout.activity_creating_promise) {

    private val navController by lazy {
        val container =
            supportFragmentManager.findFragmentById(R.id.fragment_creating_promise) as NavHostFragment
        container.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar(binding.containerToolbar.toolbar, getString(R.string.promise_creation))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (navController.currentDestination?.id == R.id.nav_profile_frag) {
                    finish()
                } else {
                    navController.popBackStack()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}