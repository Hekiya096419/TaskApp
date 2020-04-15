package jp.techacademy.jouchan.wan.taskapp

import java.io.Serializable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Category : RealmObject(), Serializable {
    var category: String = ""

    @PrimaryKey
    var categoryid: Int = 0
}