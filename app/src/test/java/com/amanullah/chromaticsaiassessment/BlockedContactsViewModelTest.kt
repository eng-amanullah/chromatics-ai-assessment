package com.amanullah.chromaticsaiassessment

import com.amanullah.chromaticsaiassessment.data.local.entity.BlockedContact
import com.amanullah.chromaticsaiassessment.domain.blockedcontacts.FetchBlockedNumbersUseCase
import com.amanullah.chromaticsaiassessment.domain.blockedcontacts.SearchBlockedContactsUseCase
import com.amanullah.chromaticsaiassessment.domain.blockedcontacts.UnblockContactUseCase
import com.amanullah.chromaticsaiassessment.presentation.blockedcontacts.BlockedContactsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@OptIn(ExperimentalCoroutinesApi::class)
class BlockedContactsViewModelTest {

    private lateinit var viewModel: BlockedContactsViewModel
    private lateinit var fetchBlockedNumbersUseCase: FetchBlockedNumbersUseCase
    private lateinit var searchBlockedContactsUseCase: SearchBlockedContactsUseCase
    private lateinit var unblockContactUseCase: UnblockContactUseCase
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        fetchBlockedNumbersUseCase = mock(FetchBlockedNumbersUseCase::class.java)
        searchBlockedContactsUseCase = mock(SearchBlockedContactsUseCase::class.java)
        unblockContactUseCase = mock(UnblockContactUseCase::class.java)

        viewModel = BlockedContactsViewModel(
            fetchBlockedNumbersUseCase = fetchBlockedNumbersUseCase,
            searchBlockedContactsUseCase = searchBlockedContactsUseCase,
            unblockContactUseCase = unblockContactUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchBlockedNumbers should update blockedContacts`() = runBlocking {
        // Arrange
        val mockBlockedContacts = listOf(
            BlockedContact(name = "John Doe", phoneNumber = "1234567890"),
            BlockedContact(name = "Jane Doe", phoneNumber = "0987654321")
        )
        whenever(fetchBlockedNumbersUseCase()).thenReturn(mockBlockedContacts)

        // Act
        viewModel.fetchBlockedNumbers()

        // Assert
        assertEquals(mockBlockedContacts, viewModel.blockedContacts.value)
    }

    @Test
    fun `updateSearchQuery should call searchBlockedContactsUseCase`() {
        // Arrange
        val query = "John"
        val mockSearchResult = listOf(
            BlockedContact(name = "John Doe", phoneNumber = "1234567890")
        )
        whenever(searchBlockedContactsUseCase(query)).thenReturn(mockSearchResult)

        // Act
        viewModel.updateSearchQuery(query)

        // Assert
        assertEquals(query, viewModel.searchQuery)
        assertEquals(mockSearchResult, viewModel.searchBlockedContacts.value)
    }

    @Test
    fun `unblockContact should call unblockContactUseCase and fetchBlockedNumbers again`() =
        runBlocking {
            // Arrange
            val contact = BlockedContact(name = "John Doe", phoneNumber = "1234567890")
            val mockBlockedContacts = listOf(
                BlockedContact(name = "Jane Doe", phoneNumber = "0987654321")
            )
            whenever(fetchBlockedNumbersUseCase()).thenReturn(mockBlockedContacts)

            // Act
            viewModel.unblockContact(contact)

            // Assert
            verify(unblockContactUseCase).invoke(contact)
            assertEquals(mockBlockedContacts, viewModel.blockedContacts.value)
        }

    @Test
    fun `searchBlockedNumbers should filter contacts based on query`() = runBlocking {
        // Arrange
        val query = "Jane"
        val mockBlockedContacts = listOf(
            BlockedContact(name = "John Doe", phoneNumber = "1234567890"),
            BlockedContact(name = "Jane Doe", phoneNumber = "0987654321")
        )
        whenever(searchBlockedContactsUseCase(query)).thenReturn(
            listOf(BlockedContact(name = "Jane Doe", phoneNumber = "0987654321"))
        )
        viewModel._blockedContacts.value = mockBlockedContacts

        // Act
        viewModel.updateSearchQuery(query)

        // Assert
        assertEquals(1, viewModel.searchBlockedContacts.value.size)
        assertEquals("Jane Doe", viewModel.searchBlockedContacts.value[0].name)
    }

    @Test
    fun `searchBlockedNumbers should return all contacts if query is empty`() = runBlocking {
        // Arrange
        val query = ""
        val mockBlockedContacts = listOf(
            BlockedContact(name = "John Doe", phoneNumber = "1234567890"),
            BlockedContact(name = "Jane Doe", phoneNumber = "0987654321")
        )
        viewModel._blockedContacts.value = mockBlockedContacts

        // Act
        viewModel.updateSearchQuery(query)

        // Assert
        assertEquals(mockBlockedContacts, viewModel.searchBlockedContacts.value)
    }
}