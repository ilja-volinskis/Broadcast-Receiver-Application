package com.example.broadcastreceiverapplication


import com.example.broadcastreceiverapplication.data.SmsData
import com.example.broadcastreceiverapplication.data.SmsRepositoryImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class SmsRepositoryImplTest {

    private lateinit var repository: SmsRepositoryImpl

    @Before
    fun setUp() {
        repository = SmsRepositoryImpl()
    }

    @Test
    fun addMessage_singleMessage_appendsToMessagesList() = runTest {
        val before = repository.messages.value.size
        val newSms = SmsData("+37100000000", "Hello world", 999999L)

        repository.addMessage(newSms)

        val after = repository.messages.value
        assertEquals(before + 1, after.size)
        assertEquals(newSms, after.last())
    }

    @Test
    fun addMessage_multipleMessages_preservesInsertionOrder() = runTest {
        val first = SmsData("+37100000001", "First", 1L)
        val second = SmsData("+37100000001", "Second", 2L)

        repository.addMessage(first)
        repository.addMessage(second)

        val tail = repository.messages.value.takeLast(2)
        assertEquals(listOf(first, second), tail)
    }
}
