package com.setruth.jetpackcomposemviframework.di

import android.app.Application
import android.content.Context
import com.setruth.jetpackcomposemviframework.network.RequestBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RequestModule {
    @Provides
    fun providesRequestModule(@ApplicationContext context: Context):RequestBuilder{
        return RequestBuilder(context)
    }
}