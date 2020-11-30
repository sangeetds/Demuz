package com.example.demuz

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.demuz.Filters.*
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    lateinit var questionAdapter: QuestionAdapter
    private var searchView: SearchView? = null
    private lateinit var questionDao: QuestionDao

    init {
        instance = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.demuz.R.layout.activity_main)
        questionDao = QuestionDataBase.getDatabase(this)!!.questionDao()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Title"

        toolbar = findViewById(com.example.demuz.R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val filterButton = findViewById<Button>(com.example.demuz.R.id.filterButton)
        filterButton.setOnClickListener {
            showBottomSheetFilterFragment()
        }

        val questionView = findViewById<RecyclerView>(R.id.questionList)
        questionView.setHasFixedSize(true)

        val questionList = getListOfNames()
        questionAdapter = QuestionAdapter(this, questionList.toMutableList())
        questionView.adapter = questionAdapter
        questionView.layoutManager = LinearLayoutManager(this)
    }

    private fun getListOfNames() = QuestionRepository(questionDao).allQuestions

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search)
            .actionView as SearchView
        searchView.setSearchableInfo(
            searchManager
                .getSearchableInfo(componentName)
        )
        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                questionAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                questionAdapter.filter.filter(query)
                return false
            }
        })

        return true
    }

    private fun showBottomSheetFilterFragment() {
        val bottomSheetFragment = FilterFragment()
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)

        bottomSheetFragment.onSubmit = { list, name ->
            println("$list $name")
            val newQuestionList: List<Question> = when (name) {
                COLLEGE -> list.fold(mutableSetOf<Question>()) { acc, item -> acc.addAll(questionDao.filterCollege(item)); acc }.toMutableList()
                COMPANY -> list.fold(mutableSetOf<Question>()) { acc, item -> acc.addAll(questionDao.filterCompanies(item)); acc }.toMutableList()
                TOPICS -> list.fold(mutableSetOf<Question>()) { acc, item -> acc.addAll(questionDao.filterTopics(item)); acc }.toMutableList()
                ROLE -> list.fold(mutableSetOf<Question>()) { acc, item -> acc.addAll(questionDao.filterRole(item)); acc }.toMutableList()
                DIFFICULTY -> list.fold(mutableSetOf<Question>()) { acc, item -> acc.addAll(questionDao.filterDifficulty(item)); acc }.toMutableList()
                FAVORITE -> questionDao.getFavoriteQuestions(true).toMutableList()
                COMPLETED -> if (list.size == 1 && list.first() == "Completed") questionDao.getCompletedQuestions(true)
                        else if (list.size == 1 && list.first() == "Not Started") questionDao.getCompletedQuestions(false)
                        else questionDao.getCompletedQuestions(true) + questionDao.getCompletedQuestions(false)
                else -> questionDao.getAllQuestions()
            }

            println(newQuestionList)

            questionAdapter.filteredQuestions = newQuestionList.toMutableList()
            questionAdapter.notifyDataSetChanged()
        }
    }

    companion object {

        private var instance: MainActivity? = null

        private fun getQuestions() : List<Question> =
            QuestionRepository(
                QuestionDataBase.getDatabase(instance!!.applicationContext!!)!!.questionDao()
            ).allQuestions


        fun getAllCompanies(): List<String> = getQuestions().fold(mutableSetOf<String>()) { acc, question ->
            acc.addAll(question.companies.trim().split(","))
            acc
        }.toList()

        fun getAllRole(): List<String> = getQuestions().fold(mutableSetOf<String>()) { acc, question ->
            acc.addAll(question.role.trim().split(","))
            acc
        }.toList()

        fun getAllTopics(): List<String> = getQuestions().fold(mutableSetOf<String>()) { acc, question ->
            acc.addAll(question.topics.trim().split(","))
            acc
        }.toList()

        fun getAllColleges(): List<String> = getQuestions().fold(mutableSetOf<String>()) { acc, question ->
            acc.addAll(question.college.trim().split(","))
            acc
        }.toList()

        fun getAllDifficulty(): List<String> = getQuestions().fold(mutableSetOf<String>()) { acc, question ->
            acc.addAll(question.difficulty.trim().split(","))
            acc
        }.toList()

        fun getFavorites(): List<String> = listOf("Favorite")

        fun getCompleted(): List<String> = listOf("Completed", "Not Started")
    }
}