package com.example.mindfulinha

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.*

class LockFragment : Fragment() {

    private lateinit var numberPickerHour: NumberPicker
    private lateinit var numberPickerMinute: NumberPicker
    private lateinit var textViewSelectedTime: TextView
    private lateinit var btnSetLockTime: Button
    private lateinit var devicePolicyManager: DevicePolicyManager
    private lateinit var compName: ComponentName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lock, container, false)

        devicePolicyManager = requireActivity().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        compName = ComponentName(requireContext(), DeviceAdminReceiver::class.java)

        numberPickerHour = view.findViewById(R.id.numberPickerHour)
        numberPickerMinute = view.findViewById(R.id.numberPickerMinute)
        textViewSelectedTime = view.findViewById(R.id.textViewSelectedTime)
        btnSetLockTime = view.findViewById(R.id.btnSetLockTime)

        numberPickerHour.minValue = 0
        numberPickerHour.maxValue = 23
        numberPickerHour.value = 0
        numberPickerHour.wrapSelectorWheel = true

        numberPickerMinute.minValue = 0
        numberPickerMinute.maxValue = 59
        numberPickerMinute.value = 30
        numberPickerMinute.wrapSelectorWheel = true

        numberPickerHour.setOnValueChangedListener { _, _, _ ->
            updateSelectedTime()
        }
        numberPickerMinute.setOnValueChangedListener { _, _, _ ->
            updateSelectedTime()
        }

        updateSelectedTime()

        btnSetLockTime.setOnClickListener {
            val selectedHour = numberPickerHour.value
            val selectedMinute = numberPickerMinute.value

            val lockTimeMillis = (selectedHour * 60 * 60 * 1000 + selectedMinute * 60 * 1000).toLong()
            val lockIntent = Intent(requireContext(), LockService::class.java)
            lockIntent.putExtra("LOCK_TIME", lockTimeMillis)
            requireActivity().startService(lockIntent)

            if (!devicePolicyManager.isAdminActive(compName)) {
                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Device admin permissions are required to lock the screen")
                startActivityForResult(intent, 1)
            } else {
                val intent = Intent(requireContext(), LockScreenActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }

        return view
    }

    private fun updateSelectedTime() {
        val selectedHour = numberPickerHour.value
        val selectedMinute = numberPickerMinute.value

        val currentTime = Calendar.getInstance()
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentTime.get(Calendar.MINUTE)

        val lockTimeString = buildLockTimeString(currentHour, currentMinute, selectedHour, selectedMinute)
        textViewSelectedTime.text = lockTimeString
    }

    private fun buildLockTimeString(currentHour: Int, currentMinute: Int, selectedHour: Int, selectedMinute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, currentHour)
        calendar.set(Calendar.MINUTE, currentMinute)

        calendar.add(Calendar.HOUR_OF_DAY, selectedHour)
        calendar.add(Calendar.MINUTE, selectedMinute)

        val lockHour = calendar.get(Calendar.HOUR_OF_DAY)
        val lockMinute = calendar.get(Calendar.MINUTE)

        return "지금부터 ${lockHour}시 ${lockMinute}분 까지 잠금"
    }
}
