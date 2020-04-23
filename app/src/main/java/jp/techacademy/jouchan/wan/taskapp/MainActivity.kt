package jp.techacademy.jouchan.wan.taskapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AlertDialog
import android.content.Intent
import android.app.AlarmManager
import android.app.PendingIntent
import io.realm.*
import kotlinx.android.synthetic.main.activity_main.*

const val EXTRA_TASK ="jp.techacademy.jouchan.wan.taskapp.TASK"

class MainActivity : AppCompatActivity() {
    private lateinit var mRealm: Realm
    private val mRealmListener = object : RealmChangeListener<Realm>{
        override fun onChange(element: Realm) {
            reloadListView()
        }
    }

    private lateinit var mTaskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, InputActivity::class.java)
            startActivity(intent)
        }

        mRealm = Realm.getDefaultInstance()
        mRealm.addChangeListener(mRealmListener)

        Realm.init(this)
        val cofing = RealmConfiguration.Builder().name("category.realm").build()
        Realm.setDefaultConfiguration(cofing)

        search_button.setOnClickListener {
            val taskRealmResults =
                mRealm.where(Task::class.java).equalTo("category", search_edit_text.text.toString()).findAll()
            mTaskAdapter.tasklist = mRealm.copyFromRealm(taskRealmResults)
            listview.adapter = mTaskAdapter
            mTaskAdapter.notifyDataSetChanged()
        }

        mTaskAdapter = TaskAdapter(this@MainActivity)

        listview.setOnItemClickListener { parent, _, position, _ ->
            val task = parent.adapter.getItem(position) as Task
            val intent = Intent(this@MainActivity, InputActivity::class.java)
            intent.putExtra(EXTRA_TASK, task.id)
            startActivity(intent)
        }

        listview.setOnItemLongClickListener { parent, _, position, _ ->
            val task = parent.adapter.getItem(position) as Task

            val builder = AlertDialog.Builder(this@MainActivity)

            builder.setTitle("削除")
            builder.setMessage(task.title + "を削除しますか")

            builder.setPositiveButton("OK") { _, _ ->
                val results = mRealm.where(Task::class.java).equalTo("id", task.id).findAll()

                mRealm.beginTransaction()
                results.deleteAllFromRealm()
                mRealm.commitTransaction()

                val resultIntent = Intent(applicationContext, TaskAlarmReceiver::class.java)
                val resultPendingIntent = PendingIntent.getBroadcast(
                    this@MainActivity,
                    task.id,
                    resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(resultPendingIntent)

                reloadListView()
            }

            builder.setNegativeButton("CANCEL", null)

            val dialog = builder.create()
            dialog.show()

            true
        }

        reloadListView()
    }

    private fun reloadListView() {
        val taskRealmResults = mRealm.where(Task::class.java).findAll().sort("date", Sort.DESCENDING)
        mTaskAdapter.tasklist = mRealm.copyFromRealm(taskRealmResults)
        listview.adapter = mTaskAdapter
        mTaskAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()

        mRealm.close()
    }
}
