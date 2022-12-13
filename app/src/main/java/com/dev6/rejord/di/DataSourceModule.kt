package com.dev6.rejord.di
import com.dev6.remote.JoinRemoteDataSource
import com.dev6.remote.JoinRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Singleton
    @Binds
    abstract fun bindsJoinRemoteSource(source: JoinRemoteDataSourceImpl): JoinRemoteDataSource


}