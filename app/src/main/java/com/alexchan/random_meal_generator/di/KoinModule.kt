package com.alexchan.random_meal_generator.di

import com.alexchan.random_meal_generator.BuildConfig
import com.alexchan.random_meal_generator.BuildConfig.DEBUG
import com.alexchan.random_meal_generator.api.CocktailApi
import com.alexchan.random_meal_generator.api.MealApi
import com.alexchan.random_meal_generator.repository.CocktailRepository
import com.alexchan.random_meal_generator.repository.MealRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val COCKTAIL_API = "COCKTAIL_API"
private const val MEAL_API = "MEAL_API"

val viewModelModule = module {
}

val repositoryModule = module {
    single { CocktailRepository(get()) }
    single { MealRepository(get()) }
}

val apiModule = module {
    fun provideCocktailApi(retrofit: Retrofit): CocktailApi {
        return retrofit.create(CocktailApi::class.java)
    }

    fun provideMealApi(retrofit: Retrofit): MealApi {
        return retrofit.create(MealApi::class.java)
    }

    single { provideCocktailApi(get(named(COCKTAIL_API))) }
    single { provideMealApi(get(named(MEAL_API))) }
}

val networkModule = module {
    fun provideHttpClient(): OkHttpClient {
        val connectTimeout: Long = 60
        val readTimeout: Long = 60

        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)

        if (DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }

        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(client: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    single(named(COCKTAIL_API)) {
        val baseUrl = BuildConfig.COCKTAIL_BASE_URL
        provideRetrofit(provideHttpClient(), baseUrl)
    }

    single(named(MEAL_API)) {
        val baseUrl = BuildConfig.MEAL_BASE_URL
        provideRetrofit(provideHttpClient(), baseUrl)
    }
}
