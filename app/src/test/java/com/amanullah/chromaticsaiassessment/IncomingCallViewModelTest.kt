package com.amanullah.chromaticsaiassessment

import com.amanullah.chromaticsaiassessment.data.local.entity.Contact
import com.amanullah.chromaticsaiassessment.domain.incomingcall.InsertCallerUseCase
import com.amanullah.chromaticsaiassessment.domain.incomingcall.SimulateIncomingCallUseCase
import com.amanullah.chromaticsaiassessment.presentation.incomingcall.IncomingCallViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IncomingCallViewModelTest {

    private lateinit var viewModel: IncomingCallViewModel
    private lateinit var simulateIncomingCallUseCase: FakeSimulateIncomingCallUseCase
    private lateinit var insertCallerUseCase: FakeInsertCallerUseCase
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        simulateIncomingCallUseCase = FakeSimulateIncomingCallUseCase()
        insertCallerUseCase = FakeInsertCallerUseCase()
        viewModel = IncomingCallViewModel(simulateIncomingCallUseCase, insertCallerUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `simulateIncomingCall should update matchedCaller`() = runBlocking {
        // Act
        val phoneNumber = "1234567890"
        viewModel.simulateIncomingCall(phoneNumber)

        // Assert
        assertEquals("John Doe", viewModel.matchedCaller?.name)
        assertEquals(phoneNumber, viewModel.matchedCaller?.phoneNumber)
    }

    @Test
    fun `insertCaller should add a new caller`() = runBlocking {
        // Act
        val name = "Jane Doe"
        val phoneNumber = "0987654321"
        viewModel.insertCaller(name, phoneNumber)

        // Assert
        val caller = insertCallerUseCase.callers.first { it.phoneNumber == phoneNumber }
        assertEquals(name, caller.name)
        assertEquals(phoneNumber, caller.phoneNumber)
    }

    @Test
    fun `clearCallerInfo should reset matchedCaller to null`() {
        // Arrange
        viewModel.matchedCaller = Contact(name = "John Doe", phoneNumber = "1234567890")

        // Act
        viewModel.clearCallerInfo()

        // Assert
        assertNull(viewModel.matchedCaller)
    }
}

class FakeSimulateIncomingCallUseCase : SimulateIncomingCallUseCase {
    private val dummyContacts = listOf(
        Contact(name = "John Doe", phoneNumber = "1234567890"),
        Contact(name = "Jane Doe", phoneNumber = "0987654321")
    )

    override suspend fun invoke(phoneNumber: String): Contact? {
        return dummyContacts.find { it.phoneNumber == phoneNumber }
    }
}

class FakeInsertCallerUseCase : InsertCallerUseCase {
    val callers = mutableListOf<Contact>()

    override suspend fun invoke(name: String, phoneNumber: String) {
        callers.add(Contact(name = name, phoneNumber = phoneNumber))
    }
}