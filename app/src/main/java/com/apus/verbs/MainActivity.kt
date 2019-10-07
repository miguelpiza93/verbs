package com.apus.verbs

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.app.SearchManager
import android.content.Context
import android.view.Menu
import android.widget.SearchView


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val data = readJson()
        initReclycler(data)

    }

    private fun initReclycler(data: ArrayList<Verb>) {
        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(data)

        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager
            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    private fun readJson(): ArrayList<Verb> {
        val am = getAssets()
        val inputStream = am.open("dictionary.json")
        val string = convertStreamToString(inputStream)
        val json = JSONObject(string)
        val verbsJson = json.getJSONArray("verbs")

        val verbs = ArrayList<Verb>()

        (0 until verbsJson.length()).forEach { index ->
            val actual = verbsJson.getJSONObject(index)
            val verb = actual.getString("verb")
            val simple_past = actual.getString("simple_past")
            val past_participle = actual.getString("past_participle")
            val spanish = actual.getString("spanish")
            verbs.add(Verb(verb, simple_past, past_participle, spanish))
        }
        return verbs
    }

    private fun convertStreamToString(input: InputStream): String {
        val reader = BufferedReader(InputStreamReader(input))
        val sb = StringBuilder()

        var line: String?
        try {
            line = reader.readLine()
            while ((line) != null) {
                sb.append(line).append('\n')
                line = reader.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                input.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return sb.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchMenuItem = menu.findItem(R.id.search)
        val searchView = searchMenuItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)

        return true
    }

    override fun onQueryTextSubmit(newText: String?): Boolean {
        (viewAdapter as MyAdapter).filter?.filter(newText)
        return true;
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        (viewAdapter as MyAdapter).filter?.filter(newText)
        return true;
    }
}
