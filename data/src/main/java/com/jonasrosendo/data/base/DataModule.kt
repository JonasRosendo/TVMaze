package com.jonasrosendo.data.base

import android.content.Context
import com.jonasrosendo.data.datasources.HomeDataSource
import com.jonasrosendo.data.repositories.ShowRepository
import com.jonasrosendo.data.services.TVMazeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(NetworkConnectionInterceptor(context))
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.tvmaze.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideShowService(retrofit: Retrofit): TVMazeService =
        retrofit.create(TVMazeService::class.java)

    @Provides
    @Singleton
    fun bindHomeDataSource(tvMazeService: TVMazeService): HomeDataSource =
        HomeDataSource(tvMazeService)


    @Provides
    @Singleton
    fun bindHomeRepository(dataSource: HomeDataSource): ShowRepository =
        ShowRepository(dataSource)
}