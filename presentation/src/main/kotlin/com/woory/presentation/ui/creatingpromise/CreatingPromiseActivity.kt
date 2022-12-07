package com.woory.presentation.ui.creatingpromise

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.fragment.NavHostFragment
import com.woory.presentation.R
import com.woory.presentation.databinding.ActivityCreatingPromiseBinding
import com.woory.presentation.ui.BaseActivity
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
        setUpAppBar()
    }

    private fun setUpAppBar() {
        setSupportActionBar(binding.containerToolbar.toolbar)
        binding.containerToolbar.toolbar.title = getString(R.string.promise_creation)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right)
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