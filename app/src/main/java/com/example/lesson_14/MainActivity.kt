package com.example.lesson_14

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson_14.adapters.ElementAdapter
import com.example.lesson_14.databinding.ActivityMainBinding
import com.example.lesson_14.fragments.CustomDialogFragment
import com.example.lesson_14.listeners.DeleteElementListener
import com.example.lesson_14.listeners.FinishAppListener
import com.example.lesson_14.listeners.SaveElementsListener
import com.example.lesson_14.models.Element
import java.io.*
import java.util.*

class MainActivity : AppCompatActivity(),
    FinishAppListener,
    SaveElementsListener,
    DeleteElementListener {
    companion object {
        const val KEY_FOLDER_NAME = "/listElementsFolder"
        const val KEY_FILE_NAME = "/elements.txt"
        const val KEY_SAVE_INSTANCE = "SAVE_INSTANCE"
    }

    private var listElement = ArrayList<Element>()

    private var adapter: ElementAdapter? = null

    private var bindingMain: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain?.root)

        setSupportActionBar(bindingMain?.myToolBar)

        checkPresenceFileElements()

        if (savedInstanceState != null) {
            listElement =
                savedInstanceState.getParcelableArrayList<Element>(KEY_SAVE_INSTANCE) as ArrayList<Element>
        } else {
            readElementsFile()
        }

        setUpAdapter()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(KEY_SAVE_INSTANCE, listElement)
    }

    override fun onDestroy() {
        super.onDestroy()
        bindingMain = null
        adapter = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_save -> {
                writeElementsFile()

                Toast.makeText(
                    applicationContext,
                    resources.getString(R.string.txt_toast_save),
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.btn_plus -> {
                listElement.add(Element("", 0))
                adapter?.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        CustomDialogFragment().apply {
            isCancelable = false
            show(supportFragmentManager, null)
        }
    }

    override fun finishApp() {
        finish()
    }

    override fun deleteElement(elementItem: Element) {
        listElement.remove(elementItem)
        adapter?.notifyDataSetChanged()
    }

    override fun saveAllElements() {
        writeElementsFile()
    }

    private fun checkPresenceFileElements() {
        val dir = File("$filesDir$KEY_FOLDER_NAME")
        val file = File("$dir$KEY_FILE_NAME")

        if (!dir.exists()) {
            dir.mkdir()
        }

        if (!file.exists()) {
            file.createNewFile()
        }
    }

    private fun writeElementsFile() {
        try {
            ObjectOutputStream(FileOutputStream(File("$filesDir$KEY_FOLDER_NAME$KEY_FILE_NAME"))).use { output ->
                output.writeObject(listElement)
            }
        } catch (ex: Exception) {
            println(ex.message)
        }
    }

    private fun readElementsFile() {
        try {
            ObjectInputStream(FileInputStream(File("$filesDir$KEY_FOLDER_NAME$KEY_FILE_NAME"))).use { input ->
                listElement = input.readObject() as ArrayList<Element>
            }
        } catch (ex: Exception) {
            println(ex.message)
        }
    }

    private fun setUpAdapter() {
        adapter = ElementAdapter(this, listElement, this)

        bindingMain?.elementList?.adapter = adapter
        bindingMain?.elementList?.layoutManager = LinearLayoutManager(this)
    }
}