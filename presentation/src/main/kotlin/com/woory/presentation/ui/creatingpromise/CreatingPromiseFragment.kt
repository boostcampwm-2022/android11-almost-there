package com.woory.presentation.ui.creatingpromise

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
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
import com.woory.presentation.model.PromiseData
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.ui.promiseinfo.PromiseInfoActivity
import com.woory.presentation.util.TAG
import com.woory.presentation.util.TimeConverter.asOffsetDateTime
import com.woory.presentation.util.animRightToLeftNavOption
import com.woory.presentation.util.getExceptionMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.threeten.bp.Duration
import org.threeten.bp.LocalTime

@AndroidEntryPoint
class CreatingPromiseFragment :
    BaseFragment<FragmentCreatingPromiseBinding>(R.layout.fragment_creating_promise) {

    private val viewModel: CreatingPromiseViewModel by activityViewModels()

    private val alarmFunctions by lazy {
        AlarmFunctions(requireContext())
    }

    private val promiseDatePicker by lazy {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(System.currentTimeMillis())
                .setValidator(DateValidatorPointForward.now())
        val materialDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.hint_select_promise_date))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        materialDatePicker.addOnPositiveButtonClickListener {
            viewModel.setPromiseDate(it.asOffsetDateTime().toLocalDate())
        }

        materialDatePicker
    }

    private val promiseTimePicker by lazy {
        val nowLocalTime = LocalTime.now()
        val materialTimePicker = MaterialTimePicker.Builder()
            .setInputMode(INPUT_MODE_KEYBOARD)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(nowLocalTime.hour)
            .setMinute(nowLocalTime.minute)
            .setTitleText(getString(R.string.hint_select_promise_time)).build()

        materialTimePicker.addOnPositiveButtonClickListener {
            val hourOfPromise = materialTimePicker.hour
            val minuteOfPromise = materialTimePicker.minute
            viewModel.setPromiseTime(LocalTime.of(hourOfPromise, minuteOfPromise))
        }

        materialTimePicker
    }

    private val checkPromiseDataDialog by lazy {
        CheckPromiseDataDialog().apply {
            setButtonClickListener(object : CheckPromiseDataDialog.ButtonClickListener {
                override fun onSubmit() {
                    viewModel.setPromise()
                }

                override fun onCancel() {}
            })
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
                    viewModel.isEnabled.collect {
                        binding.btnCreatePromise.btnSubmit.isEnabled = it
                    }
                }

                launch {
                    viewModel.requestSetPromiseEvent.collectLatest {
                        showCheckPromiseDataDialog(it)
                    }
                }

                launch {
                    viewModel.setPromiseSuccessEvent.collectLatest { promiseAlarm ->
                        alarmFunctions.registerAlarm(promiseAlarm)
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
                        val message = getExceptionMessage(requireContext(), it)
                        showSnackBar(message)
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

        binding.spinnerGameTime.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    (parent?.getChildAt(position) as? TextView)?.setTextColor(
                        requireActivity().getColor(
                            R.color.hint_color
                        )
                    )
                }
                viewModel.setReadyDuration(GameTime.values()[position].duration)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.btnCreatePromise.btnSubmit.setOnClickListener {
            viewModel.setRequestSetPromiseEvent()
        }
    }

    private fun showPromiseDatePickerDialog() {
        promiseDatePicker.show(parentFragmentManager, DATE_PICKER_TAG)
    }

    private fun showPromiseTimePickerDialog() {
        promiseTimePicker.show(parentFragmentManager, TIME_PICKER_TAG)
    }

    private fun showCheckPromiseDataDialog(promiseData: PromiseData) {
        try {
            checkPromiseDataDialog.setPromiseData(promiseData)
            checkPromiseDataDialog.show(
                parentFragmentManager.beginTransaction().remove(checkPromiseDataDialog),
                CheckPromiseDataDialog.TAG
            )
        } catch (e: Exception) {
            val message = getExceptionMessage(requireContext(), e)
            showSnackBar(message)
        }
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    enum class GameTime(val duration: Duration?) {
        DEFAULT(null),
        ONE_TEN_MINUTES(Duration.ofMinutes(10)),
        THREE_TEN_MINUTES(Duration.ofMinutes(30)),
        ONE_HOURS(Duration.ofHours(1)),
        TWO_HOURS(Duration.ofHours(2)),
        THREE_HOURS(Duration.ofHours(3)),
        FOUR_HOURS(Duration.ofHours(4)),
        FIVE_HOURS(Duration.ofHours(5)),
        SIX_HOURS(Duration.ofHours(6)),
    }

    companion object {
        private const val TIME_PICKER_TAG = "Time"
        private const val DATE_PICKER_TAG = "Date"

        private const val DEFAULT_STRING = ""
    }
}