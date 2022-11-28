package com.woory.presentation.ui.creatingpromise

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.woory.presentation.R
import com.woory.presentation.background.alarm.AlarmFunctions
import com.woory.presentation.databinding.FragmentCreatingPromiseBinding
import com.woory.presentation.model.PromiseAlarm
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.ui.promiseinfo.PromiseInfoActivity
import com.woory.presentation.util.asCalendar
import com.woory.presentation.util.asLocalDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.threeten.bp.Duration
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import java.util.Calendar

@AndroidEntryPoint
class CreatingPromiseFragment :
    BaseFragment<FragmentCreatingPromiseBinding>(R.layout.fragment_creating_promise) {

    private val viewModel: CreatingPromiseViewModel by activityViewModels()

    private val alarmFunctions by lazy {
        AlarmFunctions(requireContext())
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
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            viewModel.setPromiseDate(calendar.asLocalDate())
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.promiseLocation.collectLatest {
                        binding.etPromiseLocation.setText(it?.address ?: "")
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
                    viewModel.readyDuration.collectLatest { gameTime ->
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

                launch {
                    viewModel.promiseSettingEvent.collectLatest { promiseAlarm ->
//                        alarmFunctions.registerAlarm(promiseAlarm)

                        // Todo :: 테스트용 코드{
                        alarmFunctions.registerAlarm(
                            PromiseAlarm(
                                alarmCode = promiseAlarm.alarmCode,
                                promiseCode = promiseAlarm.promiseCode,
                                state = promiseAlarm.state,
                                startTime = OffsetDateTime.now().plusSeconds(10),
                                endTime = OffsetDateTime.now().plusSeconds(30)
                            )
                        )

                        PromiseInfoActivity.startActivity(
                            requireContext(),
                            promiseAlarm.promiseCode
                        )
                        requireActivity().finish()
                    }
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
            viewModel.setReadyDuration(
                if (hour != null && minute != null) {
                    Duration.ofMinutes(60L * hour + minute)
                } else {
                    null
                }
            )
            gameTimePickerDialog.cancel()
        }

        binding.btnPromiseCreate.setOnClickListener {
            viewModel.setPromise()
        }
    }

    private fun showPromiseDatePickerDialog() {
        val minPickCalendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 1)
        }
        val lastPickCalendar = viewModel.promiseDate.value?.asCalendar() ?: minPickCalendar
        val year = lastPickCalendar.get(Calendar.YEAR)
        val month = lastPickCalendar.get(Calendar.MONTH)
        val dayOfMonth = lastPickCalendar.get(Calendar.DAY_OF_MONTH)

        promiseDatePickerDialog.updateDate(year, month, dayOfMonth)
        promiseDatePickerDialog.datePicker.minDate = minPickCalendar.timeInMillis
        promiseDatePickerDialog.show()
    }

    private fun showPromiseTimePickerDialog() {
        val localTime = viewModel.promiseTime.value ?: LocalTime.now()
        val hour = localTime.hour
        val minute = localTime.minute

        promiseTimePickerDialog.updateTime(hour, minute)
        promiseTimePickerDialog.show()
    }

    private fun showGameTimePickerDialog() {
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