package com.example.demuz

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CompletedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CompletedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var questionAdapter: QuestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_uncompleted, container, false)
        val questionView = rootView.findViewById<RecyclerView>(R.id.questionList)
        questionView.setHasFixedSize(true)

        val questionList = getListOfNames(context)
        questionAdapter = QuestionAdapter(questionList)
        questionView.adapter = questionAdapter

        questionView.layoutManager = LinearLayoutManager(context)

        return rootView
    }

    private fun getListOfNames(context: Context?): List<Question> {
        val questionDao = QuestionDataBase.getDatabase(context!!)!!.questionDao()
        val questions = QuestionRepository(questionDao).allQuestions

        return questions +
                listOf(Question(id = 1, title = "Two sums"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 2, title = "Make new"),
                    Question(id = 3, title = "wreafsgdfgdgd"),
                    Question(id = 4, title = "adrgrdgdfg"),
                    Question(id = 5, title = "waht now"))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CompletedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CompletedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}