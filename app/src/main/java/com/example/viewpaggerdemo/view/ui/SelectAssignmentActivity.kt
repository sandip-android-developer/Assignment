package com.example.viewpaggerdemo.view.ui

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.viewpaggerdemo.R
import com.example.viewpaggerdemo.databinding.ActivitySelectAssignmentBinding
import com.example.viewpaggerdemo.other.AppConstant
import com.example.viewpaggerdemo.view.base.BaseActivity
import com.example.viewpaggerdemo.view.ui.assigment1.Assignment1Activity
import com.example.viewpaggerdemo.view.ui.assigment2.Assignment2Activity
import dagger.hilt.android.AndroidEntryPoint
import com.example.viewpaggerdemo.other.ChangeBaseUrlInterceptor

import javax.inject.Inject




@AndroidEntryPoint
class SelectAssignmentActivity : BaseActivity() {
    @Inject
    lateinit var mInterceptor: ChangeBaseUrlInterceptor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        selectAssignmentBinding.clickListener = ClickHandlerSelectActivity()
    }

    private val selectAssignmentBinding by lazy {
        DataBindingUtil.setContentView<ActivitySelectAssignmentBinding>(
            this,
            R.layout.activity_select_assignment
        )
    }

    inner class ClickHandlerSelectActivity {
        private var mLastClickAssignment1: Long = 0L
        private var mLastClickAssignment2: Long = 0L
        fun onClickAssignment1(view: View) {
            if (SystemClock.elapsedRealtime() - mLastClickAssignment1 < 1000) return
            mLastClickAssignment1 = SystemClock.elapsedRealtime()
            mInterceptor.setInterceptor(AppConstant.BASE_URL_BOOK)
            val intent = Intent(this@SelectAssignmentActivity, Assignment1Activity::class.java)
            startActivity(intent)

        }

        fun onClickAssignment2(view: View) {
            if (SystemClock.elapsedRealtime() - mLastClickAssignment2 < 1000) return
            mLastClickAssignment2 = SystemClock.elapsedRealtime()
            mInterceptor.setInterceptor(AppConstant.BASE_URL_CAT)
            val intent = Intent(this@SelectAssignmentActivity, Assignment2Activity::class.java)
            startActivity(intent)
        }
    }
}