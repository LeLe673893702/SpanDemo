package com.example.spandemo

import org.junit.Test

import org.junit.Assert.*
import java.util.ArrayList

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val datas = ArrayList<String>(200)
        repeat(200) {
            datas.add("2131")
        }
        val removeList = datas.subList(10, 12)
        val size = removeList.size
        datas.removeAll(removeList)
        print("size--- ${datas.size}")
    }
}