package com.hardy.practical.dialouges

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.hardy.practical.R
import com.hardy.practical.databinding.CustomDialogSelectDateBinding
import com.hardy.practical.listeners.onDateSubmitted
import com.hardy.practical.model.DateCustomModel
import java.util.*


class CustomDialogSelectDate(context: Context, val listener: onDateSubmitted) : Dialog(context) {


    lateinit var model: DateCustomModel
    var startDate = ""


    var binding: CustomDialogSelectDateBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(context),
            R.layout.custom_dialog_select_date,
            null,
            false
        ) as CustomDialogSelectDateBinding?


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

                })

            }

            it.btnAdd.setOnClickListener {

                if (TextUtils.isEmpty(startDate)
                ) {
                    Toast.makeText(context, "Please Select Date !!", Toast.LENGTH_SHORT).show()
                } else {
                    model = DateCustomModel(startDate, "", "")
                    listener.dateSelectedForFilter(model)
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


                listener.dateFound(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)

            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }


    interface onDateFoundListener {
        fun dateFound(dateSelected: String)

    }
}

