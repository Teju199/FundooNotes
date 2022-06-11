package com.example.fundoonotes.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.fundoonotes.R
import com.example.fundoonotes.model.ReminderWorker
import java.util.*
import java.util.concurrent.TimeUnit

class FragmentRemainderDialog(val contenttv: String, val titletv: String) : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_reminder_dialog, container, false)

        // 1 Create Variables to hold user's selection
        var chosenYear = 0
        var chosenMonth = 0
        var chosenDay = 0
        var chosenHour = 0
        var chosenMin = 0

        // 2 Access View Components using their Id
        val button = view.findViewById<Button>(R.id.setBtn)
        val datePicker = view.findViewById<DatePicker>(R.id.datePicker)
        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
        val today = Calendar.getInstance()


        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            val month = month + 1
            chosenYear = year
            chosenMonth = month
            chosenDay = day
            val msg = "You Selected: $day/$month/$year"
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }



            /*datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, day ->
            chosenYear = year
            chosenMonth = month
            chosenDay = day
        }*/


        timePicker.setOnTimeChangedListener { _, hour, minute ->
            chosenHour = hour
            chosenMin  = minute
        }

        var hour: Int = timePicker.currentHour
        var minute: Int = timePicker.currentMinute

        val startTime: Calendar = Calendar.getInstance()
            startTime.set(Calendar.HOUR_OF_DAY, hour)
            startTime.set(Calendar.MINUTE, minute)
            startTime.set(Calendar.SECOND, 0)

        var alarmStartTime: Long = startTime.timeInMillis


        button.setOnClickListener {


            val userSelectedDateTime =Calendar.getInstance()
            userSelectedDateTime.set(chosenHour , chosenMin)
            var userSelectedDateTime1 = userSelectedDateTime.timeInMillis/100L

            val todayDateTime = Calendar.getInstance()

            val delayInSeconds = ((userSelectedDateTime.timeInMillis)/1000L) - ((todayDateTime.timeInMillis)/1000L)

            createWorkRequest(contenttv, titletv, delayInSeconds, userSelectedDateTime1, alarmStartTime)

            Toast.makeText(requireContext(), "Reminder set", Toast.LENGTH_SHORT).show()

            val intent: android.content.Intent =
                android.content.Intent(getActivity(), ActivityDashboard::class.java)
            startActivity(intent)
        }

        return view
    }

    // Private Function to create the OneTimeWorkRequest
    @SuppressLint("RestrictedApi")
    private fun createWorkRequest(message: String, title: String, timeDelayInSeconds: Long, userSelectedDateTime: Long, alarmStartTime: Long) {
        val myWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(timeDelayInSeconds, TimeUnit.SECONDS)
            .setInputData(workDataOf(
                "title" to title,
                "message" to message,))
            .build()

        WorkManager.getInstance(requireContext()).enqueue(myWorkRequest)
    }

}