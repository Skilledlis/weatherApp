package com.skilled.weatherapp

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.appbar.AppBarLayout
import com.skilled.weatherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        collapseTitleBar()
    }

    private fun collapseTitleBar() {
        var isShow = true
        var scrollRange = -1
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                binding.collapsingToolbar.title = "Rostov-on-Don"
                isShow = true
                binding.collapseToolBarConstLayot.visibility = View.INVISIBLE
            } else if (isShow) {
                binding.collapsingToolbar.title = " "//careful there should a space between double quote otherwise it wont work
                binding.collapseToolBarConstLayot.visibility = View.VISIBLE
                isShow = false
            }
        })
    }
}