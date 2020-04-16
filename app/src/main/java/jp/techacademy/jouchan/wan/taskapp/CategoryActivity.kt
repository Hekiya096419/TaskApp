package jp.techacademy.jouchan.wan.taskapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.content.Intent
import android.view.View
import kotlinx.android.synthetic.main.category_input.*
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.Sort

const val EXTRA_CATEGORY ="jp.techacademy.jouchan.wan.taskapp.CATEGORY"

class CategoryActivity : AppCompatActivity() {
    private lateinit var mRealm: Realm
    private val mRealmListener = object : RealmChangeListener<Realm>{
        override fun onChange(element: Realm) {
            reloadListView()
        }
    }
    private lateinit var mCategoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        fab2.setOnClickListener{
            val intent = Intent(this@CategoryActivity, CategoryInputActivity::class.java)
            startActivity(intent)
        }

        mRealm = Realm.getDefaultInstance()
        mRealm.addChangeListener(mRealmListener)

        mCategoryAdapter = CategoryAdapter(this@CategoryActivity)

        listview2.setOnItemClickListener { parent, _, position, _ ->
            val category = parent.adapter.getItem(position) as Category
            val intent = Intent(this@CategoryActivity, InputActivity::class.java)
            intent.putExtra(EXTRA_CATEGORY, category.categoryid)
            startActivity(intent)
            finish()
        }

        listview2.setOnItemLongClickListener { parent, _, position, _ ->
            val category = parent.adapter.getItem(position) as Category
            val intent = Intent(this@CategoryActivity, CategoryInputActivity::class.java)
            intent.putExtra(EXTRA_CATEGORY, category.categoryid)
            startActivity(intent)
            true
        }

        reloadListView()
    }

    private fun reloadListView(){
        val categoryRealmResults = mRealm.where(Category::class.java).findAll().sort("category", Sort.DESCENDING)
        mCategoryAdapter.categorylist = mRealm.copyFromRealm(categoryRealmResults)
        listview2.adapter = mCategoryAdapter
        mCategoryAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()

        mRealm.close()
    }
}