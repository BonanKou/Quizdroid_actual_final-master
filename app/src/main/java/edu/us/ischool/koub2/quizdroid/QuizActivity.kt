package edu.us.ischool.koub2.quizdroid

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView

class QuizActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val topic = intent.extras.getInt("topic")
        setContentView(R.layout.fragment_summary)
        val summary = findViewById<TextView>(R.id.summary)
        val number = findViewById<TextView>(R.id.number)
        val begin = findViewById<Button>(R.id.begin)
        number.text = "There are " + QuizApp.instance.repo.numberOfQuestionAt(topic).toString() + " questions for " + QuizApp.instance.repo.getTopicAt(topic)
        summary.text = QuizApp.instance.repo.topicDescAt(topic)
        begin.text= "Begin"
        begin.setOnClickListener{
            setContentView(R.layout.blank)

            val comeQuiz = QuizFragment.newInstance(topic, 0, 0)
            val ft:FragmentTransaction=supportFragmentManager.beginTransaction()
            ft.add(R.id.father, comeQuiz, "QuizBegin")
            ft.commit()
            Log.e("BonanKou", "wonder")
        }
    }

}


class QuizFragment: Fragment() {
    companion object {
        fun newInstance(topic:Int, total:Int, correct:Int):QuizFragment {
            val args = Bundle()
            args.apply{
                putInt("correct", correct)
                putInt("total", total)
                putInt("topic", topic)
            }
            val result = QuizFragment()
            result.arguments = args
            return result
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val topic = arguments!!.getInt("topic")
        val total = arguments!!.getInt("total")
        val correct = arguments!!.getInt("correct")
        val rootview = inflater.inflate(R.layout.quizview, container, false)
        val choose = rootview.findViewById<RadioGroup>(R.id.choose)
        val option1 = rootview.findViewById<RadioButton>(R.id.option1)
        val option2 = rootview.findViewById<RadioButton>(R.id.option2)
        val option3 = rootview.findViewById<RadioButton>(R.id.option3)
        val option4 = rootview.findViewById<RadioButton>(R.id.option4)
        option1.text = QuizApp.instance.repo.choicesAt(topic, total)[0]
        option2.text = QuizApp.instance.repo.choicesAt(topic, total)[1]
        option3.text = QuizApp.instance.repo.choicesAt(topic, total)[2]
        option4.text = QuizApp.instance.repo.choicesAt(topic, total)[3]
        val wenti = rootview.findViewById<TextView>(R.id.wenti)
        wenti.text = QuizApp.instance.repo.questionAt(topic, total)
        val submit = rootview.findViewById<Button>(R.id.submit)
        submit.text = "Submit"
        submit.setOnClickListener{
            if (choose.checkedRadioButtonId != -1) {
                val correctAnswer = QuizApp.instance.repo.choicesAt(topic, total)[QuizApp.instance.repo.answerAt(topic, total)-1]
                val ft = activity!!.supportFragmentManager.beginTransaction()
                val answer = rootview.findViewById<RadioButton>(choose.checkedRadioButtonId).text.toString()
                if (answer.equals(correctAnswer)) {
                    val goAnswer = AnswerFragment.newInstance(topic, total+1, correct+1, answer, correctAnswer)
                    ft.replace(R.id.father, goAnswer, "goAnswer")
                    ft.commit()
                } else {
                    val goAnswer = AnswerFragment.newInstance(topic, total+1, correct, answer, correctAnswer)
                    ft.replace(R.id.father, goAnswer, "goAnswer")
                    ft.commit()
                }
            }

        }
        return rootview
    }

}

class AnswerFragment:Fragment() {
    companion object {
        fun newInstance(topic:Int, total:Int, correct:Int, answer:String, correctAnswer:String):AnswerFragment {
            val args = Bundle()
            args.apply{
                putString("answer",answer)
                putInt("correct", correct)
                putInt("total", total)
                putInt("topic", topic)
                putString("correctString", correctAnswer)
            }
            val result = AnswerFragment()
            result.arguments = args
            return result
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val topic = arguments!!.getInt("topic")
        val total = arguments!!.getInt("total")
        val correct = arguments!!.getInt("correct")
        val answer = arguments!!.getString("answer")
        val correctAnswer = arguments!!.getString("correctString")
        val rootview = inflater.inflate(R.layout.answerview, container, false)
        val showAnswer = rootview.findViewById<TextView>(R.id.answer)
        val showcorrectAnswer = rootview.findViewById<TextView>(R.id.correct)
        val stat = rootview.findViewById<TextView>(R.id.stat)
        showAnswer.text = "Your answer is: " + answer
        showcorrectAnswer.text= "Correct answer is: " + correctAnswer
        stat.text = "You got $correct out of $total right!"

        val next = rootview.findViewById<Button>(R.id.next)
        if (total == QuizApp.instance.repo.numberOfQuestionAt(topic)) {
            next.text = "Finish"
            next.setOnClickListener{
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
        } else {
            next.text = "Next"
            next.setOnClickListener {
                val ft = activity!!.supportFragmentManager.beginTransaction()
                val nextQuiz = QuizFragment.newInstance(topic, total, correct)
                ft.replace(R.id.father, nextQuiz, "Quizback")
                ft.commit()
            }
        }
        return rootview
    }


}