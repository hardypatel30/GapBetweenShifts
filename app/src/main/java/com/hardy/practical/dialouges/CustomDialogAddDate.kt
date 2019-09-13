package com.hardy.practical.dialouges

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.hardy.practical.R
import com.hardy.practical.databinding.CustomDialogAddDateBinding
import com.hardy.practical.listeners.onDateSubmitted
import com.hardy.practical.model.DateCustomModel
import java.text.SimpleDateFormat
import java.util.*


class CustomDialogAddDate(context: Context, val listener: onDateSubmitted) : Dialog(context) {


    lateinit var model: DateCustomModel
    var startDate = ""

    var startTIme = ""
    var endDTIme = ""
    var sTimeObj: Date? = null
    var eTimeObj: Date? = null

    var binding: CustomDialogAddDateBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(context),
            R.layout.custom_dialog_add_date,
            null,
            false
        ) as CustomDialogAddDateBinding?


        binding?.let {
            setContentView(it.getRoot())
            it.txtStartDate.setOnClickListener {
                datePicker(object : onDateFoundListener {
                    override fun dateFound(dateSelected: String) {
                        startDate = dateSelected
                        binding?.let {
                            it.txtStartDate.setText(startDate)
                        }
                    }

                    override fun timeFound(dateSelected: String) {

                    }
                })

            }

            it.txtStartTime.setOnClickListener {

                tiemPicker(object : onDateFoundListener {
                    override fun dateFound(dateSelected: String) {

                    }

                    override fun timeFound(dateSelected: String) {

                        startTIme = dateSelected
                        var dateObj = Date()

                        sTimeObj = dateObj.toSimpleString(startTIme)

                        binding?.let {
                            it.txtStartTime.setText(startTIme)
                        }
                    }
                })


            }

            it.txtEndTime.setOnClickListener {

                tiemPicker(object : onDateFoundListener {
                    override fun dateFound(dateSelected: String) {

                    }

                    override fun timeFound(dateSelected: String) {
                        var dateObj = Date()


                        endDTIme = dateSelected
                        eTimeObj = dateObj.toSimpleString(endDTIme)
                        if (eTimeObj!!.before(sTimeObj)) {
                            Toast.makeText(context, "End Time must not be less than Start Time!!", Toast.LENGTH_SHORT)
                                .show()
                            endDTIme = ""
                        } else {
                            binding?.let {
                                it.txtEndTime.setText(endDTIme)
                            }
                        }
                    }
                })

            }
            it.btnAdd.setOnClickListener {

                if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(startTIme) || TextUtils.isEmpty(endDTIme)
                    || TextUtils.isEmpty(endDTIme)
                ) {
                    Toast.makeText(context, "Please Fill All Data !!", Toast.LENGTH_SHORT).show()
                } else {
                    model = DateCustomModel(startDate, startTIme, endDTIme)
                    listener.dateSelected(model)
                    dismiss()
                }
            }
        }
    }

    private fun datePicker(listener: onDateFoundListener) {

        // Get Current Date
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                //*************Call Time Picker Here ********************

                listener.dateFound(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)

            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    private fun tiemPicker(listener: onDateFoundListener) {
        // Get Current Time
        val c = Calendar.getInstance()
        val mHour = c.get(Calendar.HOUR_OF_DAY)
        val mMinute = c.get(Calendar.MINUTE)

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

                listener.timeFound(hourOfDay.toString() + ":" + minute)


            }, mHour, mMinute, false
        )
        timePickerDialog.show()
    }

    interface onDateFoundListener {
        fun dateFound(dateSelected: String)
        fun timeFound(dateSelected: String)
    }

    fun Date.toSimpleString(stringHour: String): Date {


        val format = SimpleDateFormat("HH:MM")
        return format.parse(stringHour)

    }

}

