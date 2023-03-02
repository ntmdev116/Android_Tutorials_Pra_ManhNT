package com.sun.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sun.android.adapters.ActivityListAdapter
import com.sun.android.databinding.ActivityNavigationBinding
import com.sun.android.ex10.TabExperimentActivity
import com.sun.android.ex12.SimpleAsyncTaskActivity
import com.sun.android.ex13.SimpleApiUsingActivity
import com.sun.android.ex3.Ex3MainActivity
import com.sun.android.ex5.Ex5MainActivity
import com.sun.android.ex6.FragmentExample1Activity
import com.sun.android.ex6.FragmentExample2Activity
import com.sun.android.ex8.ScoreKeeperActivity
import com.sun.android.ex9.MenuExampleActivity
import com.sun.android.models.NavigationActivityData

class NavigationActivity : AppCompatActivity() {
    private val binding by lazy { ActivityNavigationBinding.inflate(layoutInflater) }

    private val activityList = mutableListOf(
        NavigationActivityData("Ex3", Ex3MainActivity::class.java),
        NavigationActivityData("Ex5", Ex5MainActivity::class.java),
        NavigationActivityData("Ex6-1", FragmentExample1Activity::class.java),
        NavigationActivityData("Ex6-2", FragmentExample2Activity::class.java),
        NavigationActivityData("Ex8", ScoreKeeperActivity::class.java),
        NavigationActivityData("Ex9", MenuExampleActivity::class.java),
        NavigationActivityData("Ex10", TabExperimentActivity::class.java),
        NavigationActivityData("Ex11", SimpleAsyncTaskActivity::class.java),
        NavigationActivityData("Ex13", SimpleApiUsingActivity::class.java),
    )

    private val adapter = ActivityListAdapter().also {
        it.updateActivityList(activityList)
        it.onItemClickListener = {  data ->
            val intent = Intent(this, data.activityClass)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rvNavigationActivities.adapter = adapter
    }
}
