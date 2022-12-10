package com.woory.presentation.ui.gaming

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.woory.presentation.R
import com.woory.presentation.databinding.ActivityGameResultBinding
import com.woory.presentation.ui.BaseActivity
import com.woory.presentation.ui.gameresult.GameResultActivity
import com.woory.presentation.util.PROMISE_CODE_KEY
import com.woory.presentation.util.REQUIRE_PERMISSION_TEXT
import com.woory.presentation.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GamingActivity : BaseActivity<ActivityGameResultBinding>(R.layout.activity_gaming) {
    private val gameCode by lazy {
        intent?.getStringExtra(PROMISE_CODE_KEY)
    }

    private val viewModel: GamingViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionResults ->
        val isGranted = permissionResults.values.reduce { acc, b -> acc && b }
        if (isGranted.not()) {
            Toast.makeText(
                this,
                REQUIRE_PERMISSION_TEXT,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (gameCode == null) {
            showSnackBar(binding.root, "참여 코드가 없습니다.")
            finish()
        }

        setOnListenIsFinished()
        viewModel.setGameCode(requireNotNull(gameCode))
        viewModel.setUserId()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
            return
        }
    }

    private fun setOnListenIsFinished() {
        lifecycleScope.launch {
            viewModel.isFinished.collectLatest { isFinised ->
                if (isFinised && gameCode != null) {
                    GameResultActivity.startActivity(this@GamingActivity, requireNotNull(gameCode))
                    finish()
                }
            }
        }
    }

    companion object {
        fun startActivity(context: Context, promiseCode: String) =
            context.startActivity(Intent(context, GamingActivity::class.java).apply {
                putExtra(PROMISE_CODE_KEY, promiseCode)
            })
    }
}