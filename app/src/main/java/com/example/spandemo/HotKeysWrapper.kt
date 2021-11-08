package com.example.spandemo


data class HotKeysWrapper(

    var hotKeys: List<HotKey>? = null
)  {


    data class HotKey(

        var id: Long = 0,

        var searchKey: String = "",

        var displayText: String = "",

    )
}