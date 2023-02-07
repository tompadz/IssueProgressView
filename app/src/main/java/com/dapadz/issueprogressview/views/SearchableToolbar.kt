package com.dapadz.issueprogressview.views

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.view.marginLeft
import androidx.core.view.setPadding
import com.dapadz.issueprogressview.AndroidUtils
import com.dapadz.issueprogressview.AndroidUtils.Companion.dp
import com.dapadz.issueprogressview.AndroidUtils.Companion.getDeviceWidth
import com.dapadz.issueprogressview.AndroidUtils.Companion.updateImageViewImageAnimated
import com.dapadz.issueprogressview.AndroidUtils.Companion.updateViewShow
import com.dapadz.issueprogressview.AndroidUtils.Companion.updateViewVisibilityAnimated
import com.dapadz.issueprogressview.R

class SearchableToolbar(
    context : Context,
    attributeSet : AttributeSet? = null,
) : FrameLayout(context, attributeSet) {

    private val TAG = "SearchableToolbar"
    private val SEARCH_ICON = R.drawable.ic_search
    private val CLOSE_ICON = R.drawable.ic_close
    private val ROOT_MARGIN = 16f.dp()

    private lateinit var searchButton : ImageView
    private lateinit var titleText : TextView
    private lateinit var searchField : EditText

    private var isSearchActive = false
    private var listener : SearchableToolbarListener? = null

    init {
        initRootView()
        createSearchButton()
        createTitleText()
        createSearchField()
    }

    private fun initRootView() {
        setPadding(ROOT_MARGIN)
    }

    private fun createSearchButton() {
        searchButton = ImageView(context).apply {
            layoutParams = LayoutParams(
                24f.dp(),
                24f.dp(),
                Gravity.END or Gravity.CENTER_VERTICAL
            )
            setImageResource(SEARCH_ICON)
            setOnClickListener {
                onSearchClick()
            }
        }
        addView(searchButton)
    }

    private fun createTitleText() {
        titleText = TextView(context).apply {
            layoutParams = LayoutParams(
                MATCH_PARENT,
                WRAP_CONTENT,
                Gravity.END or Gravity.CENTER_VERTICAL
            )
            text = context.getString(R.string.title_issues)
            textSize = 21f
            maxLines = 1
            setTextColor(Color.WHITE)
        }
        addView(titleText)
    }

    private fun createSearchField() {
        searchField = EditText(context).apply {
            layoutParams = LayoutParams(
                MATCH_PARENT,
                WRAP_CONTENT,
                Gravity.END or Gravity.CENTER_VERTICAL
            )
            hint = context.getString(R.string.hint_search)
            textSize = 21f
            isVisible = false
            background = null
            maxLines = 1
            isFocusable = true
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
            setPadding(0)
            addTextChangedListener(object : SearchTextWrapper {
                override fun onTextChanged(query : CharSequence?, p1 : Int, p2 : Int, p3 : Int) {
                    listener?.onQueryChange(query?:"")
                }
            })
        }
        addView(searchField)
    }

    fun addSearchableToolbarListener(listener : SearchableToolbarListener) {
        this.listener = listener
    }

    private fun onSearchClick() {
        isSearchActive = !isSearchActive
        animateIcon()
        animateViews()
        updateKeyboardState()
        listener?.onSearchEnableChange(isSearchActive)
    }

    private fun animateIcon() {
        val icon = if (isSearchActive) CLOSE_ICON else SEARCH_ICON
        searchButton.updateImageViewImageAnimated(newIcon = icon)
    }

    private fun animateViews() {
        titleText.updateViewShow(
            show = !isSearchActive,
            scale = true,
            translate = -90f,
            animated = true,
            duration = 300,
            onDone = {
                //empty
            }
        )
        searchField.updateViewShow(
            show = isSearchActive,
            scale = false,
            translate = 0f,
            animated = true,
            duration = 250,
            onDone = {
                if (!isSearchActive) {
                    searchField.text.clear()
                }
            }
        )
    }

    private fun updateKeyboardState() {
        if (isSearchActive) {
            AndroidUtils.showKeyboard(searchField)
        }else {
            AndroidUtils.hideKeyboard(searchField)
        }
    }

    override fun onLayout(changed : Boolean, left : Int, top : Int, right : Int, bottom : Int) {
        super.onLayout(changed, left, top, right, bottom)
        searchField.layout(
            searchField.left,
            searchField.top,
            searchField.right - searchButton.measuredWidth,
            searchField.bottom
        )
    }
}

interface SearchableToolbarListener {
    fun onQueryChange(query:CharSequence)
    fun onSearchEnableChange(isEnable:Boolean)
}

interface SearchTextWrapper : TextWatcher {
    override fun afterTextChanged(p0 : Editable?) {}
    override fun beforeTextChanged(p0 : CharSequence?, p1 : Int, p2 : Int, p3 : Int) {}
}