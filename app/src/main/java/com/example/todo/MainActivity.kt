package com.example.todo

import java.io.IOException
import java.io.File
import org.apache.commons.io.FileUtils
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    val listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {

            override fun onItemLongClicked(position: Int) {
                // 1. Remove the item from the list
                listOfTasks.removeAt(position)
                // 2. Notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        loadItems()

        // Lookup the recyclerview in activity layout
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)

        // Attach the adapter to the recyclerview to populate items
        recyclerview.adapter = adapter
        // Set layout manager to position the items
        recyclerview.layoutManager = LinearLayoutManager(this)

        // set up the button and input field, so that data can be added to the list
        val inputTextField = findViewById<EditText>(R.id.editTextText)

        // get a reference to the button
        // and then set an onclicklistener
        findViewById<Button>(R.id.button).setOnClickListener {
            // grab the text the user has inputted into @id/editTextText
            val userInputtedTask = inputTextField.text.toString()

            // add the string to our list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            // notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    // Save the data that the user has inputted
    // save data by writing and reading from a file


    // create a method to get the file we need
    fun getDataFile() : File {

        // every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }

    // load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks.addAll(FileUtils.readLines(getDataFile(), Charset.defaultCharset()))
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // save items by writing them into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

}