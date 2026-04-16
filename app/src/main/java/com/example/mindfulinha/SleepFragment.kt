package com.example.mindfulinha

import android.app.Dialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class SleepFragment : Fragment() {

    private var mediaPlayer: MediaPlayer? = null
    private var timer: CountDownTimer? = null
    private var alarmPlayer: MediaPlayer? = null
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var dimOverlay: View
    private lateinit var soundButton: Button

    private var selectedSoundResId: Int = R.raw.white_noise

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sleep, container, false)

        startButton = view.findViewById(R.id.startButton)
        stopButton = view.findViewById(R.id.stopButton)
        timerTextView = view.findViewById(R.id.timerTextView)
        dimOverlay = view.findViewById(R.id.dimOverlay)
        soundButton = view.findViewById(R.id.soundButton)

        stopButton.isEnabled = false

        startButton.setOnClickListener {
            showTimePickerDialog()
        }

        stopButton.setOnClickListener {
            stopSleepTimer()
        }

        soundButton.setOnClickListener {
            showSoundPickerDialog()
        }

        return view
    }

    private fun showTimePickerDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_time_picker)

        val hourPicker: NumberPicker = dialog.findViewById(R.id.hourPicker)
        val minutePicker: NumberPicker = dialog.findViewById(R.id.minutePicker)
        val setButton: Button = dialog.findViewById(R.id.setButton)

        hourPicker.maxValue = 23
        hourPicker.minValue = 0

        minutePicker.maxValue = 59
        minutePicker.minValue = 0

        setButton.setOnClickListener {
            val hours = hourPicker.value
            val minutes = minutePicker.value
            val totalMillis = (hours * 60 + minutes) * 60 * 1000
            startSleepTimer(totalMillis.toLong())
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showSoundPickerDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_sound_picker)

        val soundRadioGroup: RadioGroup = dialog.findViewById(R.id.soundRadioGroup)
        val setSoundButton: Button = dialog.findViewById(R.id.setSoundButton)

        setSoundButton.setOnClickListener {
            val selectedRadioButtonId = soundRadioGroup.checkedRadioButtonId
            selectedSoundResId = when (selectedRadioButtonId) {
                R.id.radioWhiteNoise -> R.raw.white_noise
                R.id.radioRainSound -> R.raw.rain_sound
                R.id.radioWindSound -> R.raw.wind_sound
                else -> R.raw.white_noise
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun startSleepTimer(millis: Long) {
        timer = object : CountDownTimer(millis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                stopSleepTimer()
                playAlarmSound()
            }
        }.start()

        mediaPlayer = MediaPlayer.create(context, selectedSoundResId)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()

        dimOverlay.visibility = View.VISIBLE

        startButton.isEnabled = false
        stopButton.isEnabled = true
    }

    private fun stopSleepTimer() {
        timer?.cancel()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        dimOverlay.visibility = View.GONE

        startButton.isEnabled = true
        stopButton.isEnabled = false
        timerTextView.text = "00:00"
    }

    private fun playAlarmSound() {
        alarmPlayer = MediaPlayer.create(context, R.raw.alram_sound)
        alarmPlayer?.start()
        alarmPlayer?.setOnCompletionListener {
            alarmPlayer?.release()
            alarmPlayer = null
        }
    }
}
