package praktikum.c14220059.tugasrecyclerview

import android.content.DialogInterface
import android.content.Intent
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

class MainActivity : AppCompatActivity() {
    private lateinit var _title: MutableList<String>
    private lateinit var _date: MutableList<String>
    private lateinit var _description: MutableList<String>
    private lateinit var _image: MutableList<TypedArray>
    private lateinit var _status: MutableList<String>

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
        PrepareData()
//Buat edit data
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

        _image = arrayListOf()
        _image.add(resources.obtainTypedArray(R.array.task_images))
    }

    fun AddData() {
        arTask.clear()
        for (position in _title.indices) {
            val resourceId = _image[0].getResourceId(0, 0)
            val data = task(
                resourceId,
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