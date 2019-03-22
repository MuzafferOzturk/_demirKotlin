package com.rookiesoft.demir

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class hakkimizdaFragment :android.support.v4.app.Fragment{
    constructor() : super()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_hakkimizda,null)
    }
}