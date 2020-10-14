package com.live.dummy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.active_mentees_layout.*
import org.systers.mentorship.R

class ActiveMenteeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.active_mentees_layout)


        rvMentees.adapter = ActiveMenteeAdapter(this)
        rv_task.adapter = TaskAdapter(this)
    }
}