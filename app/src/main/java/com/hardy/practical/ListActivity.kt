package com.hardy.practical


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hardy.practical.adapter.RecyclerAdapter
import com.hardy.practical.adapter.RecyclerCallback
import com.hardy.practical.databinding.ActivityListBinding
import com.hardy.practical.databinding.ItemDateListingBinding
import com.hardy.practical.dialouges.CustomDialogAddDate
import com.hardy.practical.dialouges.CustomDialogSelectDate
import com.hardy.practical.listeners.onDateSubmitted
import com.hardy.practical.model.DateCustomModel

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ListActivity : AppCompatActivity(), onDateSubmitted {


    internal var dateCustomModelArrayList = ArrayList<DateCustomModel>()
    internal var dateCustomModelArrayListOriginal = ArrayList<DateCustomModel>()
    private var userAdapter: RecyclerAdapter<DateCustomModel, ItemDateListingBinding>? = null

    internal var binding: ActivityListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_list)
        binding?.let {

            it.fabAdd.setOnClickListener {
                val dialogAddDate = CustomDialogAddDate(this@ListActivity, this@ListActivity)
                dialogAddDate.show()
                val window = dialogAddDate.window
                window!!.setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT)
            }


            it.fabFilter.setOnClickListener {
                val dialogAddDate = CustomDialogSelectDate(this@ListActivity, this@ListActivity)
                dialogAddDate.show()
                val window = dialogAddDate.window
                window!!.setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT)
            }

            it.fabClearFilter.setOnClickListener {
                dateCustomModelArrayList.clear()
                dateCustomModelArrayList.addAll(dateCustomModelArrayListOriginal)

                binding?.let {
                    it.rclListDates.adapter!!.notifyDataSetChanged()

                }
            }
            userAdapter = RecyclerAdapter(this, dateCustomModelArrayList, R.layout.item_date_listing,
                RecyclerCallback { binder, model -> binder.model = model })

            it.rclListDates.layoutManager = LinearLayoutManager(this@ListActivity, RecyclerView.VERTICAL, false)
            it.rclListDates.adapter = userAdapter

        }
    }


    override fun dateSelected(model: DateCustomModel) {
        dateCustomModelArrayList.add(model)

        binding?.let { it.rclListDates.adapter!!.notifyDataSetChanged() }

    }

    override fun dateSelectedForFilter(model: DateCustomModel) {

        var dateArrayFiltered = dateCustomModelArrayList.filter {

            it.startDate.equals(model.startDate, ignoreCase = true)


        } as ArrayList<DateCustomModel>

        dateArrayFiltered.sortWith(compareBy({ it.startTime }))


        var filteredArray = ArrayList<DateCustomModel>()
        var i = 0


        var index = i + 1
        for (j in index until dateArrayFiltered.size) {
            var dateObj = Date().stringToDate(dateArrayFiltered.get(i).endTime)
            var dateObj2 = Date().stringToDate(dateArrayFiltered.get(j).endTime)

            val diff = dateObj2.getTime() - dateObj.getTime()
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            if (minutes > 1) {
                dateObj.setMinutes(dateObj.minutes + 1)

                var caledarInstanceStart = Calendar.getInstance()
                caledarInstanceStart.time = dateObj
                caledarInstanceStart.add(Calendar.MINUTE, 1)

                var caledarInstanceEnd = Calendar.getInstance()
                caledarInstanceEnd.time = dateObj2
                caledarInstanceEnd.add(Calendar.MINUTE, -1)

                var model = DateCustomModel(
                    dateArrayFiltered.get(i).startDate,
                    "".toSimpleString(caledarInstanceStart.time),
                    "".toSimpleString(caledarInstanceEnd.time)
                )
                filteredArray.add(model)
            }
            i = j

        }
        dateCustomModelArrayListOriginal.clear()
        dateCustomModelArrayListOriginal.addAll(dateCustomModelArrayList)
        dateCustomModelArrayList.clear()
        dateCustomModelArrayList.addAll(filteredArray)

        binding?.let { it.rclListDates.adapter!!.notifyDataSetChanged() }
        Log.e("FilteredArray ", filteredArray.toString())
    }

    fun Date.stringToDate(stringHour: String): Date {
        val format = SimpleDateFormat("HH:MM")
        return format.parse(stringHour)

    }

    fun String.toSimpleString(date: Date): String {
        val format = SimpleDateFormat("HH:MM")
        return format.format(date)
    }
}
