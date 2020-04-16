package jp.techacademy.jouchan.wan.taskapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import io.realm.Realm
import kotlinx.android.synthetic.main.categoryinput_input.*

class CategoryInputActivity : AppCompatActivity() {
    private var mCategory: Category? = null

    private val mOnDoneClickListener = View.OnClickListener {
        addCategory()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_input)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        done_button.setOnClickListener(mOnDoneClickListener)

        val intent = intent
        val categoryId = intent.getIntExtra(EXTRA_CATEGORY, -1)
        val realm = Realm.getDefaultInstance()
        mCategory = realm.where(Category::class.java).equalTo("categoryid", categoryId).findFirst()
        realm.close()
    }

    private fun addCategory() {
        val realm = Realm.getDefaultInstance()

        realm.beginTransaction()

        if (mCategory == null) {
            mCategory = Category()

            val categoryRealmResults = realm.where(Category::class.java).findAll()

            val identifier: Int =
                if (categoryRealmResults.max("categoryid") != null) {
                    categoryRealmResults.max("categoryid")!!.toInt() + 1
                } else {
                    0
                }
            mCategory!!.categoryid = identifier
        }

        val category = category_edit_text.text.toString()

        mCategory!!.category = category

        realm.copyToRealmOrUpdate(mCategory!!)
        realm.commitTransaction()

        realm.close()
    }
}
