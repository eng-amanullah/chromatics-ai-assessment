package com.amanullah.chromaticsaiassessment.base.di

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import com.amanullah.chromaticsaiassessment.data.local.dao.BlockedContactsDAO
import com.amanullah.chromaticsaiassessment.data.local.dao.ContactDAO
import com.amanullah.chromaticsaiassessment.data.local.dao.IncomingCallDAO
import com.amanullah.chromaticsaiassessment.data.local.db.AppDatabase
import com.amanullah.chromaticsaiassessment.data.repository.BlockedContactsRepository
import com.amanullah.chromaticsaiassessment.data.repository.ContactsRepository
import com.amanullah.chromaticsaiassessment.data.repository.IncomingCallRepository
import com.amanullah.chromaticsaiassessment.data.repositoryimpl.BlockedContactsRepositoryImpl
import com.amanullah.chromaticsaiassessment.data.repositoryimpl.ContactRepositoryImpl
import com.amanullah.chromaticsaiassessment.data.repositoryimpl.IncomingCallRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver =
        context.contentResolver

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase = Room.databaseBuilder(
        context = context.applicationContext,
        klass = AppDatabase::class.java,
        name = "contacts_database"
    ).build()

    @Provides
    fun provideContactDao(database: AppDatabase): ContactDAO = database.contactDao()

    @Provides
    fun provideIdentifiedCallerDAO(database: AppDatabase): IncomingCallDAO =
        database.identifiedCallerDAO()

    @Provides
    fun provideBlockedNumberDAO(database: AppDatabase): BlockedContactsDAO =
        database.blockedContactsDAO()

    @Provides
    fun provideContactsRepository(
        contactDAO: ContactDAO,
        blockedContactsDAO: BlockedContactsDAO
    ): ContactsRepository =
        ContactRepositoryImpl(contactDao = contactDAO, blockedContactsDAO = blockedContactsDAO)

    @Provides
    fun provideBlockedContactsRepository(
        contactDAO: ContactDAO,
        blockedContactsDAO: BlockedContactsDAO
    ): BlockedContactsRepository =
        BlockedContactsRepositoryImpl(blockedContactsDAO = blockedContactsDAO)

    @Provides
    fun provideIncomingCallerRepository(incomingCallDAO: IncomingCallDAO): IncomingCallRepository =
        IncomingCallRepositoryImpl(incomingCallDAO = incomingCallDAO)
}