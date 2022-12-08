package com.woory.presentation.ui.creatingpromise

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import com.woory.presentation.R
import com.woory.presentation.background.alarm.AlarmFunctions
import com.woory.presentation.databinding.FragmentCreatingPromiseBinding
import com.woory.presentation.model.PromiseAlarm
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.ui.promiseinfo.PromiseInfoActivity
import com.woory.presentation.util.TimeConverter.asCalendar
import com.woory.presentation.util.TimeConverter.asOffsetDateTime
import com.woory.presentation.util.animRightToLeftNavOption
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

    private val promiseDatePicker by lazy {
        val minPickCalendar = Calendar.getInstance()

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(System.currentTimeMillis())
                .setValidator(DateValidatorPointForward.now())

        val lastPickCalendar = viewModel.promiseDate.value?.asCalendar() ?: minPickCalendar

        MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.hint_select_promise_date))
            .setSelection(lastPickCalendar.timeInMillis)
            .setCalendarConstraints(constraintsBuilder.build())
            .build()
    }

    private val promiseTimePickerBuilder by lazy {
        MaterialTimePicker.Builder()
            .setInputMode(INPUT_MODE_KEYBOARD)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText(getString(R.string.hint_select_promise_time))
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreatePromise.btnSubmit.isEnabled = false
        binding.vm = viewModel

        setUpCollector()
        setUpListener()
    }

    private fun setUpCollector() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.promiseLocation.collectLatest {
                        binding.btnPromiseLocation.text = it?.address ?: DEFAULT_STRING
                    }
                }

                launch {
                    viewModel.promiseDate.collectLatest {
                        binding.btnPromiseDate.text = it?.toString() ?: DEFAULT_STRING
                    }
                }

                launch {
                    viewModel.promiseTime.collectLatest {
                        binding.btnPromiseTime.text = it?.toString() ?: DEFAULT_STRING
                    }
                }

                launch {
                    viewModel.readyDuration.collectLatest { gameTime ->
                        binding.btnGameTime.text = if (gameTime != null) {
                            String.format(
                                getString(R.string.before_time),
                                gameTime.toHours(),
                                gameTime.toMinutes() % 60
                            )
                        } else DEFAULT_STRING
                    }
                }

                launch {
                    viewModel.isEnabled.collect {
                        binding.btnCreatePromise.btnSubmit.isEnabled = it
                    }
                }

                launch {
                    viewModel.promiseSettingEvent.collectLatest { promiseAlarm ->
//                        alarmFunctions.registerAlarm(promiseAlarm)

                         //Todo :: 테스트용 코드{
                        alarmFunctions.registerAlarm(
                            PromiseAlarm(
                                alarmCode = promiseAlarm.alarmCode,
                                promiseCode = promiseAlarm.promiseCode,
                                state = promiseAlarm.state,
                                startTime = OffsetDateTime.now().plusSeconds(10),
                                endTime = OffsetDateTime.now().plusSeconds(30)
                            )
                        )

                        viewModel.setPromiseAlarm(promiseAlarm)

                        PromiseInfoActivity.startActivity(
                            requireContext(),
                            promiseAlarm.promiseCode
                        )
                        requireActivity().finish()
                    }
                }

                launch {
                    viewModel.errorEvent.collectLatest {
                        showSnackBar(it.message ?: DEFAULT_STRING)
                    }
                }
            }
        }
    }

    private fun setUpListener() {
        binding.btnPromiseLocation.setOnClickListener {
            findNavController().navigate(
                R.id.nav_location_find_frag,
                null,
                animRightToLeftNavOption
            )
        }

        binding.btnPromiseDate.setOnClickListener {
            showPromiseDatePickerDialog()
        }

        binding.btnPromiseTime.setOnClickListener {
            showPromiseTimePickerDialog()
        }

        binding.btnGameTime.setOnClickListener {
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

        binding.btnCreatePromise.btnSubmit.setOnClickListener {
            viewModel.setPromise()
        }
    }

    private fun showPromiseDatePickerDialog() {
        promiseDatePicker.show(parentFragmentManager, DATE_PICKER_TAG)
        promiseDatePicker.addOnPositiveButtonClickListener {
            viewModel.setPromiseDate(it.asOffsetDateTime().toLocalDate())
        }
    }

    private fun showPromiseTimePickerDialog() {
        val localTime = viewModel.promiseTime.value ?: LocalTime.now()

        promiseTimePickerBuilder.setHour(localTime.hour)
        promiseTimePickerBuilder.setMinute(localTime.minute)

        val promiseTimePicker = promiseTimePickerBuilder.build()
        promiseTimePicker.show(parentFragmentManager, TIME_PICKER_TAG)

        promiseTimePicker.addOnPositiveButtonClickListener {
            val hourOfPromise = promiseTimePicker.hour
            val minuteOfPromise = promiseTimePicker.minute
            viewModel.setPromiseTime(LocalTime.of(hourOfPromise, minuteOfPromise))
        }
    }

    private fun showGameTimePickerDialog() {
        gameTimePickerDialog.show()
    }

    private fun showSnackBar(text: String){
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        private const val MIN_SELECT_HOUR = 1
        private const val MAX_SELECT_HOUR = 23

        private const val MIN_SELECT_MINUTE = 0
        private const val MAX_SELECT_MINUTE = 59

        private const val TIME_PICKER_TAG = "Time"
        private const val DATE_PICKER_TAG = "Date"

        private const val DEFAULT_STRING = ""
    }
}