package praktikum.c14220059.tugasrecyclerview

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var _formTitle = findViewById<TextView>(R.id.formtitle)
        var _taskNameField = findViewById<TextInputLayout>(R.id.taskNameField)
        var _taskDateField = findViewById<TextInputLayout>(R.id.taskDateField)
        var _titleTextMultiLine = findViewById<EditText>(R.id.editTextTextMultiLine)
        var _buttonSave = findViewById<Button>(R.id.saveBtn)

        val dataIntent = intent.getParcelableExtra<task>("kirimData", task::class.java)
        val dataPos = intent.getIntExtra("position", 0)
        if (dataIntent != null) {
            _formTitle.text = "Edit Selected Task"
            _taskNameField.editText?.setText(dataIntent.title)
            _taskDateField.editText?.setText(dataIntent.date)
            _titleTextMultiLine.setText(dataIntent.description)
            _buttonSave.setOnClickListener {
                var arrData = arrayListOf<String>()
                arrData.add(_taskNameField.editText?.text.toString())
                arrData.add(_taskDateField.editText?.text.toString())
                arrData.add(_titleTextMultiLine.text.toString())
                val intent = Intent(this@TaskFormActivity, MainActivity::class.java)
                intent.putExtra("editData", arrData)
                intent.putExtra("position", dataPos)
                startActivity(intent)
            }
        } else {
            _formTitle.text = "Add New Task"
            _buttonSave.setOnClickListener {
                var arrData = arrayListOf<String>()
                arrData.add(_taskNameField.editText?.text.toString())
                arrData.add(_taskDateField.editText?.text.toString())
                arrData.add(_titleTextMultiLine.text.toString())
                val intent = Intent(this@TaskFormActivity, MainActivity::class.java)
                intent.putExtra("addData", arrData)
                startActivity(intent)
            }
        }

        _taskDateField.editText?.setOnClickListener {
            val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTitleText("Select date")
                .build()

            datePicker.addOnPositiveButtonClickListener {
                selection ->
                val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                val dateString = simpleDateFormat.format(Date(selection))
                _taskDateField.editText?.setText(dateString)
            }

            datePicker.show(supportFragmentManager, "tag");
        }

    }
}