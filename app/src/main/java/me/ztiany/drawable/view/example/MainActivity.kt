package me.ztiany.drawable.view.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseUtils.init(this.application)
        Timber.plant(Timber.DebugTree())
        setContentView(R.layout.activity_main)
    }

    fun shapeView(view: View) {
        startActivity(Intent(this, ShapeViewActivity::class.java))
    }

    fun drawableView(view: View) {
        startActivity(Intent(this, DrawableViewActivity::class.java))
    }

    fun shapeViewDialog(view: View) {
        AppCompatDialog(this, R.style.AppTheme_Dialog).apply {
            setContentView(R.layout.activity_shape_view)
        }.show()
    }

    fun drawableViewDialog(view: View) {
        AppCompatDialog(this, R.style.AppTheme_Dialog).apply {
            setContentView(R.layout.activity_drawable_view)
        }.show()
    }

}