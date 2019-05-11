package edu.us.ischool.koub2.quizdroid

import android.app.Application
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

/**
 * there can only be once instance of your application running. Thus we made a variable called instance that has a references
 * to the application and any class can get a reference of that activity by just calling SingletonApplication.instance
 *
 * This allows us to have a shared repository class called DataManager that can exist across activities, fragments, etc.
 *
 * When extending Applicaiton:
 *      Do not forget to add this class in the Manifest in the <application name=[INSERT HERE] /> Otherwise it will not work
 *
 */

interface TopicRepository {
    //return the number of topics
    fun numberOfTopics():Int

    //return the nth topic's description
    fun topicDescAt(position:Int):String

    //return the number of question in the nth topic
    fun numberOfQuestionAt(position: Int):Int

    // return the nth question of the mth topic
    fun questionAt(topicNum: Int, questionNum: Int): String

    // return all the answers to the nth question of the mth topic
    fun choicesAt(topicNum: Int, questionNum: Int): List<String>

    // return the correct answer (as an int) of the nth question of the mth topic
    fun answerAt(topicNum: Int, questionNum: Int): Int
}

class QuizRepository(val topics: ArrayList<Topic>):TopicRepository {
    fun getTopicAt(topic:Int):String{
        return topics.get(topic).name
    }
    override fun numberOfQuestionAt(position: Int): Int {
        return topics[position].quizNum
    }
    override fun numberOfTopics(): Int{
        return topics.size
    }
    override fun questionAt(topicNum: Int, questionNum: Int): String {
        return topics[topicNum].get(questionNum).question
    }

    override fun choicesAt(topicNum: Int, questionNum: Int): List<String> {
        return topics[topicNum].get(questionNum).choices
    }

    override fun answerAt(topicNum: Int, questionNum: Int): Int {
        return topics[topicNum].get(questionNum).answer
    }

    override fun topicDescAt(position: Int): String {
        return topics[position].desc
    }
}

class QuizApp: Application() {

    lateinit var repo: QuizRepository
        private set
    val list = listOf("Science!", "Marvel Super Heroes", "Mathematics")

    companion object {
        lateinit var instance: QuizApp
            private set
    }

    override fun onCreate() {
        Log.e("BonanKou", "Success")
        super.onCreate()

        //读Json
        val jsonString: String? = try {
            // grab file from assets folder & read it to a String
            val inputStream = assets.open("quiz.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            null
        }
        Log.e("BonanKou", jsonString)

        //构造Topic Repository
        jsonString.let {

            Log.e("haha", jsonString)

            // Create json from string
            //val jsonObject = JSONObject(jsonString)
            //val testarray = JSONArray(jsonObject)
            val testarray = JSONArray(jsonString)
            val output = ArrayList<Topic>()
            for (i in 0 until testarray.length()) {
                var outputQuiz = ArrayList<Quiz>()
                val topicJSON = testarray.get(i) as JSONObject
                val quizJson = topicJSON.get("questions") as JSONArray
                Log.e("BonanKou", "fuck")
                for (j in 0 until quizJson.length()) {
                    val quizJSON = quizJson.get(j) as JSONObject
                    var arrayChoices = ArrayList<String>()
                    val arrayQuestion = quizJSON.get("answers") as JSONArray
                    for (k in 0 until arrayQuestion.length()) {
                        arrayChoices.add(arrayQuestion.get(k) as String)
                    }

                    outputQuiz.add(Quiz((quizJSON.get("answer")as String).toInt(), arrayChoices, quizJSON.get("text") as String))
                }
                val oneTopic = Topic(outputQuiz, topicJSON.get("desc") as String, topicJSON.get("title") as String)
                output.add(oneTopic)
                //val questions: List<Quiz>, val desc:String, val name:String
            }
            repo = QuizRepository(output)
        }

        instance = this
    }
}


class Quiz(val answer: Int, val choices: ArrayList<String>, val question: String) {}

class Topic(val questions: ArrayList<Quiz>, val desc:String, val name:String){
    val quizNum = questions.size
    fun get(position: Int): Quiz {
        return questions[position]
    }
}