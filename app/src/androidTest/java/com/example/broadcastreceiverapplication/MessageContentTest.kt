package com.example.broadcastreceiverapplication


import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.broadcastreceiverapplication.data.SmsData
import com.example.broadcastreceiverapplication.ui.sms.MessagesContent
import com.example.broadcastreceiverapplication.ui.sms.SmsItem
import org.junit.Rule
import org.junit.Test


class MessageContentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun messagesContent_emptyList_showsNoMessagesPlaceholder() {
        composeTestRule.setContent {
            MessagesContent(sender = "+37112345678", messages = emptyList())
        }

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.no_messages_received_yet))
            .assertExists()
    }

    @Test
    fun messagesContent_nonEmptyList_showsSenderHeader() {
        val sender = "+37112345678"
        val messages = listOf(SmsData(sender, "Hi there", 1000L))

        composeTestRule.setContent {
            MessagesContent(sender = sender, messages = messages)
        }

        composeTestRule.onNodeWithText(sender).assertExists()
    }

    @Test
    fun messagesContent_nonEmptyList_rendersEachMessageBody() {
        val sender = "+37112345678"
        val messages = listOf(
            SmsData(sender, "First message", 1000L),
            SmsData(sender, "Second message", 2000L)
        )

        composeTestRule.setContent {
            MessagesContent(sender = sender, messages = messages)
        }

        composeTestRule.onNodeWithText("First message").assertExists()
        composeTestRule.onNodeWithText("Second message").assertExists()
    }

    @Test
    fun smsItem_singleMessage_displaysMessageBodyText() {
        val message = SmsData("+37112345678", "Body", 1234L)

        composeTestRule.setContent {
            SmsItem(message = message)
        }

        composeTestRule.onNodeWithText("Body").assertExists()
    }
}

