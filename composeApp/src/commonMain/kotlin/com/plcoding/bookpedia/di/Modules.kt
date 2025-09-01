package com.plcoding.bookpedia.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.plcoding.bookpedia.book.data.database.DatabaseFactory
import com.plcoding.bookpedia.book.data.database.FavoriteBookDatabase
import com.plcoding.bookpedia.book.data.network.KtorRemoteBookDataSource
import com.plcoding.bookpedia.book.data.network.RemoteBookDataSource
import com.plcoding.bookpedia.book.data.repository.DefaultBookRepository
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.book.presentation.SelectedBookViewModel
import com.plcoding.bookpedia.book.presentation.book_detail.BookDetailViewModel
import com.plcoding.bookpedia.book.presentation.book_list.BookListViewModel
import com.plcoding.bookpedia.book.presentation.add_to_cart.AddToCartViewModel
import com.plcoding.bookpedia.core.data.HttpClientFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {

    single { HttpClientFactory.create(get()) }
    // In above code "get()" is a koin function which gets the dependency which'll be required
    // Here the dependency required is basically "engine", "engine" will be different for all
    // platforms, and it is written in platformModule of all the device types... (So koin knows
    // how to provide engine depending on platform...

    // Now for any object creation using koin we can inject dependencies using "get()" if there are
    // 2 dependencies we can simply do "    single { myCoolClass(get(), get()) } to get both
    // but there's a better way... instead of doing get() for every new dependency we can do is
    // singleOf(::myCoolClass)      // This would automatically try to create instance of this
    // class with all required dependencies

    singleOf(::KtorRemoteBookDataSource).bind<RemoteBookDataSource>()
    singleOf(::DefaultBookRepository).bind<BookRepository>()

    // .bind<XYZ> allows us to bind interface to implementation, whenever u ask koin to generate
    // object of BookRepository ... it will return DefaultBookRepository instance :)

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<FavoriteBookDatabase>().favoriteBookDao }

    viewModelOf(::BookListViewModel)
    viewModelOf(::BookDetailViewModel)
    viewModelOf(::SelectedBookViewModel)
    viewModelOf(::AddToCartViewModel)
}