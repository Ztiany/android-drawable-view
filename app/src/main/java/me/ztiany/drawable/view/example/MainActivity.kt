package me.ztiany.drawable.view.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseUtils.init(this.application)
        setContentView(R.layout.activity_main)
    }

    fun shapeView(view: View) {
        startActivity(Intent(this, ShapeViewActivity::class.java))
    }

    fun drawableView(view: View) {
        startActivity(Intent(this, DrawableViewActivity::class.java))
    }

}