package jp.techacademy.jouchan.wan.taskapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import io.realm.Realm
import kotlinx.android.synthetic.main.category_input.*
import kotlinx.android.synthetic.main.category_input.done_button

class CategoryActivity: AppCompatActivity() {
    private var mCategory: Category? = null

    private val mOnDoneClickListener = View.OnClickListener {
        addCategory()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        done_button.setOnClickListener(mOnDoneClickListener)
    }

    private fun addCategory() {
        val realm = Realm.getDefaultInstance()

        realm.beginTransaction()

        if (mCategory == null) {
            mCategory = Category()

            val categoryRealmResults = realm.where(Category::class.java).findAll()

            val identifier: Int =
                if (categoryRealmResults.max("id") != null) {
                    categoryRealmResults.max("id")!!.toInt() + 1
                } else {
                    0
                }
            mCategory!!.id = identifier
        }

        val category = category_edit_text.text.toString()

        mCategory!!.category = category

        realm.copyToRealmOrUpdate(mCategory!!)
        realm.commitTransaction()

        realm.close()
    }
}