package com.dapadz.issueprogressview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Outline
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewOutlineProvider
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.abs


class AndroidUtils {
    companion object {

        private const val TAG = "AndroidUtils"
        private var density = 1f

        //region func
        fun checkDisplaySize(context : Context) {
            density = context.resources.displayMetrics.density
        }

        fun showKeyboard(view : View?) : Boolean {
            if (view == null) {
                return false
            }
            try {
                val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                return inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            } catch (e : java.lang.Exception) {
                Log.e(TAG, e.localizedMessage?:e.message?:"error")
            }
            return false
        }

        fun hideKeyboard(view : View?) {
            if (view == null) {
                return
            }
            try {
                val imm : InputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (! imm.isActive) {
                    return
                }
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            } catch (e : Exception) {
                Log.e(TAG, e.localizedMessage?:e.message?:"error")
            }
        }

        fun lerp(a : Int, b : Int, f : Float) : Int {
            return (a + f * (b - a)).toInt()
        }

        fun lerpAngle(a : Float, b : Float, f : Float) : Float {
            val delta = (b - a + 360 + 180) % 360 - 180
            return (a + delta * f + 360) % 360
        }

        fun lerp(a : Float, b : Float, f : Float) : Float {
            return a + f * (b - a)
        }

        fun lerp(a : Double, b : Double, f : Float) : Double {
            return a + f * (b - a)
        }

        fun lerp(ab : FloatArray, f : Float) : Float {
            return lerp(ab[0], ab[1], f)
        }

        fun updateVisibleRows(listView : RecyclerView?) {
            if (listView == null) {
                return
            }
            val adapter = listView.adapter ?: return
            for (i in 0 until listView.childCount) {
                val child : View = listView.getChildAt(i)
                val p : Int = listView.getChildAdapterPosition(child)
                if (p >= 0) {
                    val holder : RecyclerView.ViewHolder = listView.getChildViewHolder(child)
                    adapter.onBindViewHolder(holder, p)
                }
            }
        }
        //endregion

        //region numbers
        fun Float.normalize(
            min : Float,
            max : Float,
            minNormalize : Float,
            maxNormalize : Float,
        ): Float {
            val x = (1 - (this - min) / (max - min) * 100) * -1
            return when {
                x < minNormalize -> minNormalize
                x > maxNormalize -> maxNormalize
                else -> x
            }
        }
        //endregion

        //region dp
        fun Float.dp() : Int = (this * density).toInt()
        fun Float.dpf() : Float = this * density
        fun Int.dp() : Int = (this * density).toInt()
        fun Int.dpf() : Float = this * density
        //endregion

        //region px
        fun Float.px() : Int = (this / density).toInt()
        fun Float.pxf() : Float = this / density
        fun Int.px() : Int = (this / density).toInt()
        fun Int.pxf() : Float = this / density
        //endregion

        //region view
        fun View.setCornerRadiusOfView(radius:Float = 30f) {
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view : View, outline : Outline) {
                    outline.setRoundRect(0, 0, view.width, view.height, radius)
                }
            }
            clipToOutline = true
        }

        fun View.getWidthOfView() : Int {
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            return measuredWidth
        }

        fun View.getHeightOfView() : Int {
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            return measuredHeight
        }

        fun View.updateViewShow(show : Boolean, duration : Long = 340) {
            updateViewShow(show, true, true, duration)
        }

        fun View.updateViewShow(
            show : Boolean,
            scale : Boolean,
            animated : Boolean,
            duration : Long = 340
        ) {
            updateViewShow(show, scale, 0f, animated, null, duration)
        }

        fun View.updateViewShow(
            show : Boolean,
            scale : Boolean,
            animated : Boolean,
            onDone : Runnable?,
            duration : Long = 340
        ) {
            updateViewShow(show, scale, 0f, animated, onDone, duration)
        }

        fun View.updateViewShow(
            show : Boolean,
            scale : Boolean,
            translate : Float,
            animated : Boolean,
            onDone : Runnable?,
            duration : Long = 340
        ) {
            var _animated = animated
            if (parent == null) {
                _animated = false
            }
            animate().setListener(null).cancel()
            if (! _animated) {
                visibility = if (show) View.VISIBLE else View.GONE
                tag = if (show) 1 else null
                alpha = 1f
                scaleX = if (scale && ! show) 0f else 1f
                scaleY = if (scale && ! show) 0f else 1f
                if (translate != 0f) {
                    translationY = if (show) 0f else (- 16f).dp() * translate
                }
                onDone?.run()
            } else if (show) {
                if (visibility != View.VISIBLE) {
                    visibility = View.VISIBLE
                    alpha = 0f
                    scaleX = if (scale) 0f else 1f
                    scaleY = if (scale) 0f else 1f
                    if (translate != 0f) {
                        translationY = (- 16f).dp() * translate
                    }
                }
                var animate = animate()
                animate = animate.alpha(1f).scaleY(1f).scaleX(1f)
                    .setInterpolator(FastOutSlowInInterpolator()).setDuration(duration)
                    .withEndAction(onDone)
                if (translate != 0f) {
                    animate.translationY(0f)
                }
                animate.start()
            } else {
                var animate = animate()
                animate = animate.alpha(0f).scaleY(if (scale) 0f else 1f)
                    .scaleX(if (scale) 0f else 1f).setListener(HideViewAfterAnimation(this))
                    .setInterpolator(FastOutSlowInInterpolator()).setDuration(duration)
                    .withEndAction(onDone)
                if (translate != 0f) {
                    animate.translationY((- 16f).dp() * translate)
                }
                animate.start()
            }
        }

        fun View.updateViewVisibilityAnimated(show : Boolean) {
            updateViewVisibilityAnimated(show, 1f, true)
        }

        fun View.updateViewVisibilityAnimated(
            show : Boolean,
            scaleFactor : Float,
            animated : Boolean,
        ) {
            var _animated = animated
            if (parent == null) {
                _animated = false
            }
            if (! _animated) {
                animate().setListener(null).cancel()
                visibility = if (show) View.VISIBLE else View.GONE
                tag = if (show) 1 else null
                alpha = 1f
                scaleX = 1f
                scaleY = 1f
            } else if (show && tag == null) {
                animate().setListener(null).cancel()
                if (visibility != View.VISIBLE) {
                    visibility = View.VISIBLE
                    alpha = 0f
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                }
                animate().alpha(1f).scaleY(1f).scaleX(1f).setDuration(150).start()
                tag = 1
            } else if (! show && tag != null) {
                animate().setListener(null).cancel()
                animate().alpha(0f).scaleY(scaleFactor).scaleX(scaleFactor).setListener(
                    HideViewAfterAnimation(this)
                ).setDuration(150).start()
                tag = null
            }
        }
        //endregion

        //region context
        fun Context.getTypeColor(colorResId : Int) : Int {
            val typedValue = TypedValue()
            val typedArray = obtainStyledAttributes(typedValue.data, intArrayOf(colorResId))
            val color = typedArray.getColor(0, 0)
            typedArray.recycle()
            return color
        }

        @SuppressLint("InternalInsetResource", "DiscouragedApi")
        fun Context.getStatusBarHeight() : Int {
            val resourceId : Int = resources.getIdentifier("status_bar_height", "dimen", "android")
            return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
        }

        @SuppressLint("InternalInsetResource", "DiscouragedApi")
        fun Context.getNavigationBarHeight() : Int {
            val resourceId : Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
        }

        @ColorInt
        fun Context.getColorFromAttr(
            @AttrRes attrColor : Int,
            typedValue : TypedValue = TypedValue(),
            resolveRefs : Boolean = true,
        ): Int {
            theme.resolveAttribute(attrColor, typedValue, resolveRefs)
            return typedValue.data
        }

        fun Context.copyToClipboard(text: CharSequence) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", text)
            clipboard.setPrimaryClip(clip)
        }

        fun Context.shareLink(link: String) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, link)
            startActivity(Intent.createChooser(shareIntent, ""))
        }

        fun Context.openLink(link: String) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(browserIntent)
        }
        //endregion

        //region resource
        fun Resources.getDeviceWidth(): Int = Resources.getSystem().displayMetrics.widthPixels
        fun Resources.getDeviceHeight(): Int = Resources.getSystem().displayMetrics.heightPixels
        //endregion

        //region ImageView
        fun ImageView.setTint(colors: ColorStateList) {
            ImageViewCompat.setImageTintList(this, colors)
        }

        fun ImageView.updateImageViewImageAnimated(@DrawableRes newIcon : Int) {
            val drawable = ContextCompat.getDrawable(context, newIcon) ?: return
            updateImageViewImageAnimated(drawable)
        }

        fun ImageView.updateImageViewImageAnimated(newIcon: Drawable) {
            if (drawable == newIcon) {
                return
            }
            ValueAnimator.ofFloat(0f, 1f).apply {
                duration = 150
                val changed = AtomicBoolean()
                addUpdateListener {
                    val animValue = it.animatedValue as Float
                    val scale = 0.5f + abs(animValue - 0.5f)
                    scaleX = scale
                    scaleY = scale
                    if (animValue >= 0.5f && ! changed.get()) {
                        changed.set(true)
                        setImageDrawable(newIcon)
                    }
                }
            }.start()
        }
        //endregion
    }


    class HideViewAfterAnimation(private val view : View) : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation : Animator) {
            super.onAnimationEnd(animation)
            view.visibility = View.GONE
        }
    }
}