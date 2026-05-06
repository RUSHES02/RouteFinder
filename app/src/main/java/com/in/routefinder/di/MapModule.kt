package com.`in`.routefinder.di

import com.`in`.routefinder.BuildConfig
import com.`in`.routefinder.core.networking.HttpClientFactory
import com.`in`.routefinder.data.remote.GoogleMapsDataSource
import com.`in`.routefinder.data.repository.MapsRepositoryImpl
import com.`in`.routefinder.domain.MapsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.LoggingFormat
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMapsRepository(
        api: GoogleMapsDataSource
    ): MapsRepository {
        return MapsRepositoryImpl(api)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(
    ): HttpClient {
        return HttpClientFactory.create(engine = CIO.create(), isDebug = BuildConfig.DEBUG)
    }

    @Provides
    @Singleton
    fun provideGoogleMapsApi(
        client: HttpClient
    ): GoogleMapsDataSource {
        return GoogleMapsDataSource(
            client = client,
            apiKey = BuildConfig.MAPS_API_KEY
        )
    }
}