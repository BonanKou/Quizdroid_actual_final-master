package edu.us.ischool.koub2.quizdroid

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            //val intent = Intent(this, question::class.java)
            //intent.putExtra("something", "nononon")

            val listOfQuiz = QuizApp.instance.list

            val adapter = QuizAdapter(listOfQuiz)
            val myRecyclerView = findViewById<View>(R.id.quizCycle) as RecyclerView

                    myRecyclerView.adapter = adapter
            myRecyclerView.setHasFixedSize(true)

            adapter.onQuizClickedListener = { position, name ->
                Log.e("fuck", "02")
                Toast.makeText(this, "$name clicked!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, QuizActivity::class.java)
                intent.putExtra("topic", position)
                startActivity(intent)
            }

            //开始preference
            val preference = getPreferences(Context.MODE_PRIVATE)
            val saveBtn = findViewById<Button>(R.id.save)
        val url = findViewById<EditText>(R.id.url)
        val min = findViewById<EditText>(R.id.min)
        url.hint = preference.getString("url", "Please type your url here.")
        val num = preference.getInt("min", 0)
        if (num==0) {
            min.hint = "Please type a number"
        } else {
            min.hint = num.toString()
        }
            saveBtn.setOnClickListener {
                preference.edit().putString("url", url.text.toString()).apply()
                preference.edit().putInt("min", min.text.toString().toInt()).apply()
            }
            //topic的数目，是int
            Log.e("BonanKou", QuizApp.instance.repo.numberOfTopics().toString())
            //第n个topic的description，是个string
            Log.e("BonanKou", QuizApp.instance.repo.topicDescAt(1))
            //第n个topic有几个question，int
            Log.e("BonanKou", QuizApp.instance.repo.numberOfQuestionAt(1).toString())
            //第n个topic的第m个题目，是个string
            Log.e("BonanKou", QuizApp.instance.repo.questionAt(1,0))
            //这玩意return的是一个list<String>，里面有第n个topic，第m个题目的所有选项
            Log.e("BonanKou", QuizApp.instance.repo.choicesAt(1,0).toString())
            //第n种topic，第m题的正确选项，是个int
            Log.e("BonanKou", QuizApp.instance.repo.answerAt(1,0).toString())
    }
}
