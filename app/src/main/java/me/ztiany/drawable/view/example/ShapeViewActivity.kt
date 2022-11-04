package me.ztiany.drawable.view.example

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.shape.*
import timber.log.Timber

/** 参考：https://blog.csdn.net/yechaoa/article/details/117339632?spm=1001.2014.3001.5501 */
class ShapeViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shape_view)
        byCode()
        researchHowItWork()
    }

    private fun byCode() {
        findViewById<TextView>(R.id.material_tv_01).apply {
            val appearanceModel = ShapeAppearanceModel.builder().apply {
                setAllCorners(RoundedCornerTreatment())
                setAllCornerSizes(50f)
                setAllEdges(TriangleEdgeTreatment(50f, false))
            }.build()

            val drawable = MaterialShapeDrawable(appearanceModel).apply {
                setTint(ContextCompat.getColor(this@ShapeViewActivity, R.color.colorPrimary))
                paintStyle = Paint.Style.FILL_AND_STROKE
                strokeWidth = 50f
                strokeColor = ContextCompat.getColorStateList(this@ShapeViewActivity, R.color.red)
            }

            background = drawable
        }

        //需要 parent.clipChildren = false
        findViewById<TextView>(R.id.material_tv_02).apply {
            val appearanceModel = ShapeAppearanceModel.builder().apply {
                setAllCorners(RoundedCornerTreatment())
                setAllCornerSizes(20f)
                setRightEdge(object : TriangleEdgeTreatment(20F, false) {
                    // center 位置 ， interpolation 角的大小
                    override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
                        super.getEdgePath(length, 55f, interpolation, shapePath)
                    }
                })
            }.build()

            val drawable = MaterialShapeDrawable(appearanceModel).apply {
                setTint(ContextCompat.getColor(this@ShapeViewActivity, R.color.colorPrimary))
                paintStyle = Paint.Style.FILL
            }
            background = drawable
        }
    }

    private fun researchHowItWork() {
        findViewById<View>(R.id.material_view_test1).apply {
            val appearanceModel = ShapeAppearanceModel.builder().apply {
                setAllCornerSizes(UnitConverter.dpToPx(15F))
                    .setTopLeftCorner(object : CornerTreatment() {
                        override fun getCornerPath(shapePath: ShapePath, angle: Float, interpolation: Float, radius: Float) {
                            Timber.d("getCornerPath() top-left  called with: shapePath = $shapePath, angle = $angle, interpolation = $interpolation, radius = $radius")
                            shapePath.reset(0f, radius * interpolation, 180F, 180 - angle)
                            shapePath.addArc(0f, 0f, 2 * radius * interpolation, 2 * radius * interpolation, 180f, angle)
                        }
                    })
                    .setTopRightCorner(object : CornerTreatment() {
                        override fun getCornerPath(shapePath: ShapePath, angle: Float, interpolation: Float, radius: Float) {
                            Timber.d("getCornerPath() top-right  called with: shapePath = $shapePath, angle = $angle, interpolation = $interpolation, radius = $radius")
                            super.getCornerPath(shapePath, angle, interpolation, radius)
                        }
                    })
                    .setBottomLeftCorner(object : CornerTreatment() {
                        override fun getCornerPath(shapePath: ShapePath, angle: Float, interpolation: Float, radius: Float) {
                            Timber.d("getCornerPath() bottom-left  called with: shapePath = $shapePath, angle = $angle, interpolation = $interpolation, radius = $radius")
                            super.getCornerPath(shapePath, angle, interpolation, radius)
                        }
                    })
                    .setBottomRightCorner(object : CornerTreatment() {
                        override fun getCornerPath(shapePath: ShapePath, angle: Float, interpolation: Float, radius: Float) {
                            Timber.d("getCornerPath() bottom-right  called with: shapePath = $shapePath, angle = $angle, interpolation = $interpolation, radius = $radius")
                            shapePath.reset(0f, radius * interpolation, 180F, 180 - angle)
                            shapePath.addArc(0f, 0f, 2 * radius * interpolation, 2 * radius * interpolation, 180f, angle)
                        }
                    })
                    .setLeftEdge(object : EdgeTreatment() {
                        override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
                            Timber.d("getEdgePath() left called with: length = $length, center = $center, interpolation = $interpolation, shapePath = $shapePath")
                            super.getEdgePath(length, center, interpolation, shapePath)
                        }
                    })
                    .setRightEdge(object : EdgeTreatment() {
                        override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
                            Timber.d("getEdgePath() right called with: length = $length, center = $center, interpolation = $interpolation, shapePath = $shapePath")
                            super.getEdgePath(length, center, interpolation, shapePath)
                        }
                    })
                    .setTopEdge(object : EdgeTreatment() {
                        override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
                            Timber.d("getEdgePath() top called with: length = $length, center = $center, interpolation = $interpolation, shapePath = $shapePath")
                            super.getEdgePath(length, center, interpolation, shapePath)
                        }
                    })
                    .setBottomEdge(object : EdgeTreatment() {
                        override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
                            Timber.d("getEdgePath() bottom called with: length = $length, center = $center, interpolation = $interpolation, shapePath = $shapePath")
                            super.getEdgePath(length, center, interpolation, shapePath)
                        }
                    })
            }.build()

            val drawable = MaterialShapeDrawable(appearanceModel).apply {
                setTint(ContextCompat.getColor(this@ShapeViewActivity, R.color.colorPrimary))
                paintStyle = Paint.Style.FILL
            }
            background = drawable
        }
    }

}