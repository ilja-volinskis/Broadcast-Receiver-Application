package com.example.broadcastreceiverapplication


import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.broadcastreceiverapplication.ui.permission.PermissionScreen
import org.junit.Rule
import org.junit.Test


class PermissionScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun permissionScreen_onDisplay_showsExplanationAndButtonText() {
        composeTestRule.setContent {
            PermissionScreen(onRequestPermission = {})
        }

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.please_grant_the_sms_permission))
            .assertExists()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.grant_permission))
            .assertExists()
    }

    @Test
    fun permissionScreen_buttonClicked_invokesOnRequestPermissionOnce() {
        var clickCount = 0

        composeTestRule.setContent {
            PermissionScreen(onRequestPermission = { clickCount++ })
        }

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.grant_permission))
            .performClick()

        assert(clickCount == 1)
    }
}
