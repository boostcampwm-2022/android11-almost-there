package com.woory.almostthere.ui.creatingpromise

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.woory.almostthere.R
import com.woory.almostthere.databinding.FragmentCreatingPromiseBinding
import com.woory.almostthere.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import java.util.*

@AndroidEntryPoint
class CreatingPromiseFragment :
    BaseFragment<FragmentCreatingPromiseBinding>(R.layout.fragment_creating_promise) {

    private val viewModel: CreatingPromiseViewModel by lazy {
        ViewModelProvider(requireActivity())[(CreatingPromiseViewModel::class.java)]
    }

    private val promiseDatePickerDialog by lazy {
        DatePickerDialog(
            requireContext(), promiseDateSetListener, DEFAULT_NUMBER, DEFAULT_NUMBER, DEFAULT_NUMBER
        )
    }

    private val promiseTimePickerDialog by lazy {
        TimePickerDialog(
            requireContext(), promiseTimePickerListener, DEFAULT_NUMBER, DEFAULT_NUMBER, true
        )
    }

    private val gameTimePickerDialog by lazy {
        Dialog(requireContext()).apply {
            setContentView(R.layout.layout_game_time_picker_dialog)
            findViewById<NumberPicker>(R.id.numberpicker_hour)?.run {
                minValue = MIN_SELECT_HOUR
                maxValue = MAX_SELECT_HOUR
            }
            findViewById<NumberPicker>(R.id.numberpicker_minute)?.run {
                minValue = MIN_SELECT_MINUTE
                maxValue = MAX_SELECT_MINUTE
            }
        }
    }

    private val promiseDateSetListener by lazy {
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            viewModel.setPromiseDate(LocalDate.of(year, month, dayOfMonth))
        }
    }

    private val promiseTimePickerListener by lazy {
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            viewModel.setPromiseTime(LocalTime.of(hourOfDay, minute))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpCollector()
        setUpListener()
    }

    private fun setUpCollector() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.promiseLocation.collectLatest {
                    binding.etPromiseLocation.setText(it?.location ?: "")
                }
            }

            launch {
                viewModel.promiseDate.collectLatest {
                    binding.etPromiseDate.setText(it?.toString() ?: "")
                }
            }

            launch {
                viewModel.promiseTime.collectLatest {
                    binding.etPromiseTime.setText(it?.toString() ?: "")
                }
            }

            launch {
                viewModel.gameTime.collectLatest { gameTime ->
                    binding.etGameTime.setText(
                        if (gameTime != null) {
                            String.format(
                                getString(R.string.before_time),
                                gameTime.toHours(),
                                gameTime.toMinutes() % 60
                            )
                        } else ""
                    )
                }
            }

            launch {
                viewModel.isEnabled.collect {
                    binding.btnPromiseCreate.isEnabled = it
                }
            }
        }
    }

    private fun setUpListener() {
        binding.ibPromiseLocation.setOnClickListener {
            findNavController().navigate(R.id.nav_location_find_frag)
        }

        binding.ibPromiseDate.setOnClickListener {
            showPromiseDatePickerDialog()
        }

        binding.ibPromiseTime.setOnClickListener {
            showPromiseTimePickerDialog()
        }

        binding.ibGameTime.setOnClickListener {
            showGameTimePickerDialog()
        }

        gameTimePickerDialog.findViewById<Button>(R.id.btn_cancel)?.setOnClickListener {
            gameTimePickerDialog.cancel()
        }

        gameTimePickerDialog.findViewById<Button>(R.id.btn_submit)?.setOnClickListener {
            val hour =
                gameTimePickerDialog.findViewById<NumberPicker>(R.id.numberpicker_hour)?.value
            val minute =
                gameTimePickerDialog.findViewById<NumberPicker>(R.id.numberpicker_minute)?.value
            viewModel.setGameTime(
                if (hour != null && minute != null) {
                    Duration.ofMinutes(60L * hour + minute)
                } else {
                    null
                }
            )
            gameTimePickerDialog.cancel()
        }

        binding.btnPromiseCreate.setOnClickListener {
            viewModel.createPromise()
        }
    }

    private fun showPromiseDatePickerDialog() {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 1)
        }

        viewModel.promiseDate.value.let {
            val year = it?.year ?: calendar.get(Calendar.YEAR)
            val month = it?.monthValue ?: calendar.get(Calendar.MONTH)
            val dayOfMonth = it?.dayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH)

            promiseDatePickerDialog.updateDate(year, month, dayOfMonth)
            promiseDatePickerDialog.datePicker.minDate = calendar.timeInMillis
            promiseDatePickerDialog.show()
        }

    }

    private fun showPromiseTimePickerDialog() {
        val calendar = Calendar.getInstance()

        viewModel.promiseTime.value.let {
            val hour = it?.hour ?: calendar.get(Calendar.HOUR)
            val minute = it?.minute ?: calendar.get(Calendar.MINUTE)

            promiseTimePickerDialog.updateTime(hour, minute)
            promiseTimePickerDialog.show()
        }
    }

    private fun showGameTimePickerDialog() {
        viewModel.gameTime.value?.let {
            gameTimePickerDialog.findViewById<NumberPicker>(R.id.numberpicker_hour).value = it.toHours().toInt()
            gameTimePickerDialog.findViewById<NumberPicker>(R.id.numberpicker_minute).value = (it.toMinutes() % 60).toInt()
        }

        gameTimePickerDialog.show()
    }

    companion object {
        private const val MIN_SELECT_HOUR = 1
        private const val MAX_SELECT_HOUR = 23

        private const val MIN_SELECT_MINUTE = 0
        private const val MAX_SELECT_MINUTE = 59

        private const val DEFAULT_NUMBER = 1
    }
}