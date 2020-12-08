package com.a.papermaxx.hiltModules

import com.a.domainmodule.inputValidation.ChatIdDecider
import com.a.domainmodule.inputValidation.GetCurrentTime
import com.a.domainmodule.inputValidation.ValidateInput
import com.a.papermaxx.general.FileExtension
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object MyModule {

    @Singleton
    @Provides
    fun provideCurrentTime(): GetCurrentTime {
        return GetCurrentTime()
    }

    @Singleton
    @Provides
    fun provideFileExtention(): FileExtension {
        return FileExtension()
    }

    @Singleton
    @Provides
    fun provideValidation(): ValidateInput {
        return ValidateInput()
    }

    @Singleton
    @Provides
    fun provideChatIdDecider(): ChatIdDecider {
        return ChatIdDecider()
    }

    @Singleton
    @Provides
    fun provideAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Singleton
    @Provides
    fun provideDatabaseReference(): DatabaseReference {
        return provideDatabase().reference
    }

    @Singleton
    @Provides
    fun provideStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Singleton
    @Provides
    fun provideStorageReference(): StorageReference {
        return provideStorage().reference
    }
}