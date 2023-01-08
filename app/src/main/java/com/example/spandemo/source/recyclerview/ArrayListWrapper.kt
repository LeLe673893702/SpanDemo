package com.example.spandemo.source.recyclerview


class ArrayListWrapper<T>: ArrayList<T>() {
    var maxSize = 0
    var canReset = true
    private val lastSize = 0

    override fun remove(element: T): Boolean {
        if (size > maxSize) {
            maxSize = size
            canReset = false
        }
        if (size == 0) {
            canReset = true
        }
        return super.remove(element)
    }

    override fun add(t: T): Boolean {
        if (canReset) {
            if (size + 1 > lastSize) {
                maxSize = size + 1
            }
        }
        return super.add(t)
    }
}