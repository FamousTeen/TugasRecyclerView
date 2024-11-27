package praktikum.c14220059.tugasrecyclerview

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.content.res.TypedArray
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private var _title: MutableList<String> = emptyList<String>().toMutableList()
    private var _date: MutableList<String> = emptyList<String>().toMutableList()
    private var _description: MutableList<String> = emptyList<String>().toMutableList()
    private lateinit var _image: MutableList<Any>
    private lateinit var _like: MutableList<Any>
    private var _status: MutableList<String> = emptyList<String>().toMutableList()
    lateinit var sp : SharedPreferences

    private var arTask = arrayListOf<task>()
    private lateinit var _rvTask : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var _addButton = findViewById<ImageButton>(R.id.addButton)

        _addButton.setOnClickListener() {
            val intent = Intent(this@MainActivity, TaskFormActivity::class.java)
            startActivity(intent)
        }


        _rvTask = findViewById(R.id.rvTask)
        sp = getSharedPreferences("datSP", MODE_PRIVATE)
        val gsoon = Gson()
        val isiSP = sp.getString("spTask", null)
        val type = object : TypeToken<ArrayList<task>>() {}.type
        if (isiSP != null) {
            arTask = gsoon.fromJson(isiSP, type)
        }
        if (arTask.size == 0) {
            PrepareData()
        } else {
            _image = arrayListOf()
            _like = arrayListOf()
            // Temporary lists to hold resource IDs for each task
            val imageResourceIds = mutableListOf<Int>()
            val likeResourceIds = mutableListOf<Int>()

            arTask.forEach {
                // Collect image and like resource IDs
                imageResourceIds.add(it.image) // Assuming it.image is a resource ID
                likeResourceIds.add(it.like)   // Assuming it.like is a resource ID
                // Process other details (not related to _image and _like)
                _title.add(it.title)
                _description.add(it.description)
                _date.add(it.date)
                _status.add(it.status)
            }

            // Dynamically create TypedArrays using the collected resource IDs
            val likeTypedArray = likeResourceIds.toTypedArray()

            // Add these TypedArrays to the _image and _like lists
            _image.add(imageResourceIds)
            _like.add(likeResourceIds)

            // Clear arTask after processing
            arTask.clear()
        }

        val dataIntent = intent.getStringArrayListExtra("editData")
        val pos = intent.getIntExtra("position", 0)

        if(dataIntent != null) {
            EditData(dataIntent, pos)
        }

//        Buat add data
        val dataIntent2 = intent.getStringArrayListExtra("addData")

        if(dataIntent2 != null) {
            _title.add(dataIntent2[0])
            _date.add(dataIntent2[1])
            _description.add(dataIntent2[2])
            _status.add("Waiting for response")
            _image.add(resources.obtainTypedArray(R.array.task_images))
        }
        AddData()
        ShowData()
    }

    fun PrepareData() {
        _title = resources.getStringArray(R.array.taskTitle).toMutableList()
        _description = resources.getStringArray(R.array.taskDescription).toMutableList()
        _status = resources.getStringArray(R.array.task_status).toMutableList()
        _date = resources.getStringArray(R.array.taskDate).toMutableList()

        _image = mutableListOf()
        _image.add(resources.obtainTypedArray(R.array.task_images))
        _like = mutableListOf()
        _like.add(resources.obtainTypedArray(R.array.task_likes))
    }

    fun AddData() {
        arTask.clear()
        for (position in _title.indices) {
            val imageResourceId = when (val imageData = _image[0]) {
                is TypedArray -> imageData.getResourceId(position % imageData.length(), 0) // Predefined
                is List<*> -> (imageData as List<Int>)[position % imageData.size] // Dynamic
                else -> 0 // Fallback
            }

            val likeResourceId = when (val likeData = _like[0]) {
                is TypedArray -> likeData.getResourceId(position % likeData.length(), 0) // Predefined
                is List<*> -> (likeData as List<Int>)[position % likeData.size] // Dynamic
                else -> 0 // Fallback
            }
            val data = task(
                imageResourceId,
                likeResourceId,
                _date[position],
                _title[position],
                _description[position],
                _status[position]
            )
            arTask.add(data)
        }
    }

    fun ShowData() {
        _rvTask.layoutManager = LinearLayoutManager(this)
        val adapterTask = adapterRecView(arTask)
        _rvTask.adapter = adapterTask

        adapterTask.setOnItemClickCallback(object : adapterRecView.OnItemClickCallback {
            override fun onItemClicked(data: task, pos: Int) {
//                Toast.makeText(this@MainActivity, data.nama, Toast.LENGTH_LONG).show()
                val intent = Intent(this@MainActivity, TaskFormActivity::class.java)
                intent.putExtra("kirimData", data)
                intent.putExtra("position", pos)
                startActivity(intent)
            }

            override fun delData(pos: Int) {
                AlertDialog.Builder(this@MainActivity).setTitle("HAPUS DATA").setMessage("Are you sure want to delete "+_title[pos]+" task?")
                    .setPositiveButton("DELETE", DialogInterface.OnClickListener { dialog, which ->
                        _title.removeAt(pos)
                        _description.removeAt(pos)
                        _status.removeAt(pos)
                        AddData()
                        ShowData()
                    })
                    .setNegativeButton("CANCEL", DialogInterface.OnClickListener { dialog, which ->
                        Toast.makeText(this@MainActivity, "Task Deletion Cancelled", Toast.LENGTH_LONG).show()
                    }).show()
            }

            override fun likeTask(pos: Int) {
                Toast.makeText(this@MainActivity, _title[pos], Toast.LENGTH_LONG).show()
//              // buat gambar heart post click
                val imageResourceId = when (val imageData = _image[0]) {
                    is TypedArray -> imageData.getResourceId(1, 0) // Predefined
                    is List<*> -> (imageData as List<Int>)[pos % imageData.size] // Dynamic
                    else -> 0 // Fallback
                }

                val likeResourceId = when (val likeData = _like[0]) {
                    is TypedArray -> likeData.getResourceId(1, 0) // Predefined
                    is List<*> -> R.drawable.heart2 // Dynamic
                    else -> 0 // Fallback
                }
                arTask[pos].image = imageResourceId
                arTask[pos].like = likeResourceId
                Log.d("Like Resource ID", likeResourceId.toString())

                val gson = Gson()
                val editor = sp.edit()
                val json = gson.toJson(arTask) // Serialize the updated list
                editor.putString("spTask", json) // Replace the existing `spTask` key with the new data
                editor.apply()
            }
        })
    }

    fun EditData(data: ArrayList<String>, pos: Int) {
        _title[pos] = data[0]
        _date[pos] = data[1]
        _description[pos] = data[2]
        AddData()
        ShowData()
    }
}