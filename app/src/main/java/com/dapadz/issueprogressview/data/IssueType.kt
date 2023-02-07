package com.dapadz.issueprogressview.data
import androidx.annotation.ColorRes
import com.dapadz.issueprogressview.R

enum class IssueType(@ColorRes val color:Int) {
    ANDROID(R.color.tag_yellow),
    IOS(R.color.tag_blue),
    WEB(R.color.tag_green),
    DESIGN(R.color.tag_purple),
    DESKTOP(R.color.tag_gray),
    BUG_FIX(R.color.tag_pink)
}