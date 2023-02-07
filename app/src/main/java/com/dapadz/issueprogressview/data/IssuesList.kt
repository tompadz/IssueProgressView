package com.dapadz.issueprogressview.data

import kotlin.random.Random

fun getRnd():Long {
    return Random.nextLong(25, 400)
}

val issuesList = listOf(
    Issue(
        id = 1,
        label = "Rewrite Authorization",
        workTime = getRnd(),
        type = IssueType.ANDROID
    ),
    Issue(
        id = 2,
        label = "Fix bugs in feed",
        workTime =  getRnd(),
        type = IssueType.ANDROID
    ),
    Issue(
        id = 3,
        label = "Update animation",
        workTime =  getRnd(),
        type = IssueType.IOS
    ),
    Issue(
        id = 4,
        label = "Refactor code",
        workTime =  getRnd(),
        type = IssueType.IOS
    ),
    Issue(
        id = 5,
        label = "Fix inputs bugs",
        workTime =  getRnd(),
        type = IssueType.WEB
    ),
    Issue(
        id = 6,
        label = "Update page design",
        workTime =  getRnd(),
        type = IssueType.WEB
    ),
    Issue(
        id = 7,
        label = "Create new style",
        workTime =  getRnd(),
        type = IssueType.DESIGN
    ),
    Issue(
        id = 8,
        label = "Manage folders",
        workTime =  getRnd(),
        type = IssueType.DESIGN
    ),
    Issue(
        id = 9,
        label = "Just work, ok?",
        workTime =  getRnd(),
        type = IssueType.DESKTOP
    ),
    Issue(
        id = 10,
        label = "Fix crash in backstack",
        workTime =  getRnd(),
        type = IssueType.DESKTOP
    ),
    Issue(
        id = 11,
        label = "Optimize view",
        workTime =  getRnd(),
        type = IssueType.BUG_FIX
    ),
)