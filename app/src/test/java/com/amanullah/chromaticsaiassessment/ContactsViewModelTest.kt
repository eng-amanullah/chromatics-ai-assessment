package com.amanullah.chromaticsaiassessment

import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.paging.PagingData
import com.amanullah.chromaticsaiassessment.data.local.entity.Contact
import com.amanullah.chromaticsaiassessment.domain.contacts.BlockOrUnblockContactUseCase
import com.amanullah.chromaticsaiassessment.domain.contacts.CacheContactsUseCase
import com.amanullah.chromaticsaiassessment.domain.contacts.FetchContactsUseCase
import com.amanullah.chromaticsaiassessment.domain.contacts.GetContactsPagedUseCase
import com.amanullah.chromaticsaiassessment.domain.contacts.SearchContactsUseCase
import com.amanullah.chromaticsaiassessment.presentation.contacts.ContactsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@OptIn(ExperimentalCoroutinesApi::class)
class ContactsViewModelTest {

    private lateinit var viewModel: ContactsViewModel
    private lateinit var getContactsPagedUseCase: GetContactsPagedUseCase
    private lateinit var fetchContactsUseCase: FetchContactsUseCase
    private lateinit var cacheContactsUseCase: CacheContactsUseCase
    private lateinit var searchContactsUseCase: SearchContactsUseCase
    private lateinit var blockOrUnblockContactUseCase: BlockOrUnblockContactUseCase
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getContactsPagedUseCase = mock(GetContactsPagedUseCase::class.java)
        fetchContactsUseCase = mock(FetchContactsUseCase::class.java)
        cacheContactsUseCase = mock(CacheContactsUseCase::class.java)
        searchContactsUseCase = mock(SearchContactsUseCase::class.java)
        blockOrUnblockContactUseCase = mock(BlockOrUnblockContactUseCase::class.java)

        viewModel = ContactsViewModel(
            application = mock(Application::class.java),
            getContactsPagedUseCase = getContactsPagedUseCase,
            fetchContactsUseCase = fetchContactsUseCase,
            cacheContactsUseCase = cacheContactsUseCase,
            searchContactsUseCase = searchContactsUseCase,
            blockOrUnblockContactUseCase = blockOrUnblockContactUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchContacts should update filteredContacts and permission state when permission is granted`() =
        runBlocking {
            // Arrange
            val mockContacts = listOf(Contact("John Doe", "1234567890"))
            whenever(fetchContactsUseCase()).thenReturn(mockContacts)

            val isPermissionGranted = true
            val context = mock(Application::class.java)
            whenever(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS))
                .thenReturn(PackageManager.PERMISSION_GRANTED)

            // Act
            viewModel.fetchContacts()

            // Assert
            assertEquals(mockContacts, viewModel._filteredContacts.value)
            assertFalse(viewModel.isPermissionDenied)
            assertFalse(viewModel.isPermissionPermanentlyDenied)
        }

    @Test
    fun `fetchContacts should set permissionDenied state when permission is denied`() {
        // Arrange
        val context = mock(Application::class.java)
        whenever(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS))
            .thenReturn(PackageManager.PERMISSION_DENIED)

        // Act
        viewModel.fetchContacts()

        // Assert
        assertTrue(viewModel.isPermissionDenied)
        assertFalse(viewModel.isPermissionPermanentlyDenied)
    }

    @Test
    fun `updateSearchQuery should call searchContactsUseCase`() {
        // Arrange
        val query = "John"
        val mockPagedFlow: Flow<PagingData<Contact>> = flowOf(PagingData.empty())
        whenever(searchContactsUseCase(query)).thenReturn(mockPagedFlow)

        // Act
        viewModel.updateSearchQuery(query)

        // Assert
        assertEquals(query, viewModel.searchQuery)
        assertEquals(mockPagedFlow, viewModel.searchPagingFlow.value)
    }

    @Test
    fun `blockOrUnblockNumber should call blockOrUnblockContactUseCase`(): Unit = runBlocking {
        // Arrange
        val contact = Contact("John Doe", "1234567890")

        // Act
        viewModel.blockOrUnblockNumber(contact)

        // Assert
        verify(blockOrUnblockContactUseCase).invoke(contact)
    }

    @Test
    fun `onPermissionDenied should update permissionDenied state`() {
        // Act
        viewModel.onPermissionDenied()

        // Assert
        assertTrue(viewModel.isPermissionDenied)
        assertFalse(viewModel.isPermissionPermanentlyDenied)
    }

    @Test
    fun `onPermissionPermanentlyDenied should update permission state`() {
        // Act
        viewModel.onPermissionPermanentlyDenied()

        // Assert
        assertTrue(viewModel.isPermissionDenied)
        assertTrue(viewModel.isPermissionPermanentlyDenied)
    }
}