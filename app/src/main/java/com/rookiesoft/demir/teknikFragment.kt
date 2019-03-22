package com.rookiesoft.demir

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rookiesoft.demir.R

class teknikFragment :android.support.v4.app.Fragment{
    constructor() : super()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView : View = inflater!!.inflate(R.layout.fragment_teknik,null)
        return rootView
    }
}