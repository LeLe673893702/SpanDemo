package com.example.spandemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class TestDialogFragment: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        childFragmentManager.isDestroyed
        return inflater.inflate(R.layout.test_dialog_fragment_layout, container, false)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
    }
}