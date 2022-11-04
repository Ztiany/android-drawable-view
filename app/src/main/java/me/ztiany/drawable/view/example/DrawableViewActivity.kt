package me.ztiany.drawable.view.example

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.peter.viewgrouptutorial.drawable.*

class DrawableViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawable_view)

        findViewById<View>(R.id.drawable_view01).background = CodeGradientDrawable.Builder(this@DrawableViewActivity).apply {
            solidColor(CodeColorStateList.valueOf(Color.parseColor("#FF0000")))
            shape(GradientDrawable.OVAL)
        }.build()

        findViewById<View>(R.id.drawable_view02).background = CodeGradientDrawable.Builder(this@DrawableViewActivity).apply {
            val gradient = Gradient.Builder(this@DrawableViewActivity).apply {
                val colors = IntArray(2).apply {
                    this[0] = Color.parseColor("#ff0000")
                    this[1] = Color.parseColor("#00ff00")
                }
                this.gradientColors(colors)
            }

            gradient(gradient)
        }.build()


        findViewById<View>(R.id.drawable_view03).background = CodeGradientDrawable.Builder(this@DrawableViewActivity).apply {
            val gradient = Gradient.Builder(this@DrawableViewActivity).apply {
                val colors = IntArray(2).apply {
                    this[0] = Color.parseColor("#ff0000")
                    this[1] = Color.parseColor("#00ff00")
                }
                this.gradientColors(colors)
            }
            val corner = Corner.Builder(this@DrawableViewActivity).apply {
                this.radius(20F)
            }
            gradient(gradient)
            corner(corner)
        }.build()


        findViewById<View>(R.id.drawable_view04).background = CodeGradientDrawable.Builder(this@DrawableViewActivity).apply {
            val gradient = Gradient.Builder(this@DrawableViewActivity).apply {
                val colors = IntArray(3).apply {
                    this[0] = Color.parseColor("#ff0000")
                    this[1] = Color.parseColor("#0000ff")
                    this[2] = Color.parseColor("#00ff00")
                }
                this.gradientColors(colors)
            }
            val corner = Corner.Builder(this@DrawableViewActivity).apply {
                this.radius(20F)
            }
            gradient(gradient)
            corner(corner)
        }.build()

        val codeColor1 = CodeColorStateList.Builder().apply {
            this.addSelectorColorItem(SelectorColorItem.Builder().apply {
                this.color(Color.parseColor("#ff0000"))
                this.addState(StatePressed)
                this.minusState(StateChecked)
            })
            this.addSelectorColorItem(SelectorColorItem.Builder().apply {
                this.color(Color.parseColor("#00ff00"))
                this.minusState(StatePressed)
                this.addState(StateChecked)
            })
            this.addSelectorColorItem(SelectorColorItem.Builder().apply {
                this.color(Color.parseColor("#0000ff"))
            })
        }.build()

        val codeColor2 = CodeColorStateList.Builder().apply {
            this.addSelectorColorItem(SelectorColorItem.Builder().apply {
                this.color(Color.parseColor("#ff0000"))
                this.minusState(StateChecked)
                this.addState(StatePressed)
            })
            this.addSelectorColorItem(SelectorColorItem.Builder().apply {
                this.color(Color.parseColor("#00ff00"))
                this.minusState(StatePressed)
                this.addState(StateChecked)
            })
            this.addSelectorColorItem(SelectorColorItem.Builder().apply {
                this.color(Color.parseColor("#0000ff"))
            })
        }.build()

        println("codeColor1 equals codeColor2: ${codeColor1 == codeColor2}")
        findViewById<TextView>(R.id.drawable_tv01).setTextColor(codeColor1)

        findViewById<View>(R.id.drawable_view05).background = CodeStateListDrawable.Builder().apply {
            this.addSelectorDrawableItem(SelectorDrawableItem.Builder().apply {
                this.drawable(AppCompatResources.getDrawable(this@DrawableViewActivity, R.drawable.img_scenery_a)!!)
                this.minusState(StatePressed)
            })
            this.addSelectorDrawableItem(SelectorDrawableItem.Builder().apply {
                this.drawable(AppCompatResources.getDrawable(this@DrawableViewActivity, R.drawable.img_scenery_b)!!)
                this.addState(StatePressed)
            })
        }.build()
    }

}