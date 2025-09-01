package com.plcoding.bookpedia.app

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentation.SelectedBookViewModel
import com.plcoding.bookpedia.book.presentation.add_to_cart.AddToCartScreen
import com.plcoding.bookpedia.book.presentation.add_to_cart.AddToCartViewModel
import com.plcoding.bookpedia.book.presentation.add_to_cart.components.CheckoutScreen
import com.plcoding.bookpedia.book.presentation.book_detail.BookDetailAction
import com.plcoding.bookpedia.book.presentation.book_detail.BookDetailScreenRoot
import com.plcoding.bookpedia.book.presentation.book_detail.BookDetailViewModel
import com.plcoding.bookpedia.book.presentation.book_list.BookListScreenRoot
import com.plcoding.bookpedia.book.presentation.book_list.BookListViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

// App is the main entry point for the application
// It is the top-level composable that contains the NavHost
// The NavHost is responsible for managing the navigation between different screens
// The NavHost is composed of multiple NavGraphs
// Each NavGraph is responsible for managing the navigation between different screens within that graph
// The NavGraphs are composed of multiple composables
// Each composable is responsible for displaying a different screen
@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.BookGraph
        ) {
            navigation<Route.BookGraph>(
                startDestination = Route.BookList
            ) {
                composable<Route.BookList>(
                    exitTransition = { slideOutHorizontally() },
                    popEnterTransition = { slideInHorizontally() }
                ) {
                    val viewModel = koinViewModel<BookListViewModel>()
                    val selectedBookViewModel =
                        it.sharedKoinViewModel<SelectedBookViewModel>(navController)

                    LaunchedEffect(true) {
                        selectedBookViewModel.onSelectBook(null)
                    }

//                    Log.d("skt_0401", "nav to book-List")

                    BookListScreenRoot(
                        viewModel = viewModel,
                        onBookClick = { book ->
                            selectedBookViewModel.onSelectBook(book)
                            navController.navigate(
                                Route.BookDetail(book.id)
                            )
                        },
                        onCartClick = {
                            navController.navigate(Route.AddToCart)
                        }
                    )
                }
                composable<Route.BookDetail>(
                    enterTransition = { slideInHorizontally { initialOffset ->
                        initialOffset
                    } },
                    exitTransition = { slideOutHorizontally { initialOffset ->
                        initialOffset
                    } }
                ) {
                    val selectedBookViewModel =
                        it.sharedKoinViewModel<SelectedBookViewModel>(navController)    // This is
                                    // the Koin way of doing what Hilt does with navGraphViewModels()
                                    // this is shared only within this "navGraph" --->> (BookGraph)

                    val viewModel = koinViewModel<BookDetailViewModel>()
                    val selectedBook by selectedBookViewModel.selectedBook.collectAsStateWithLifecycle()

                    LaunchedEffect(selectedBook) {
                        selectedBook?.let {
                            viewModel.onAction(BookDetailAction.OnSelectedBookChange(it))
                        }
                    }

                    BookDetailScreenRoot(
                        viewModel = viewModel,
                        onBackClick = {
                            navController.navigateUp()
                        },
                        selectedBookViewModel = selectedBookViewModel
                    )
                }

                composable<Route.AddToCart>(
                    enterTransition = { slideInHorizontally { initialOffset ->
                        initialOffset
                    } },
                    exitTransition = { slideOutHorizontally { initialOffset ->
                        initialOffset
                    } }
                ) {
                    val viewModel = koinViewModel<AddToCartViewModel>()
                    val selectedBookViewModel =
                        it.sharedKoinViewModel<SelectedBookViewModel>(navController)

                    AddToCartScreen(
                        viewModel = viewModel,
                        selectedBookViewModel = selectedBookViewModel,
                        onBackClick = {
                            navController.navigateUp()
                        },
                        onCheckoutClicked = {
                            navController.navigate(Route.CheckoutScreen)
                        }
                    )
                }

                composable<Route.CheckoutScreen>(
                    enterTransition = { slideInHorizontally { initialOffset ->
                        initialOffset
                    } },
                    exitTransition = { slideOutHorizontally { initialOffset ->
                        initialOffset
                    } }
                ) {
                    CheckoutScreen(
                        selectedBookViewModel = it.sharedKoinViewModel<SelectedBookViewModel>(navController)
                    )
                }
            }
        }

    }
}

@Composable
private inline fun <reified T: ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    // destination.parent ---> Implies the NavGraph

    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(                       // We're basically creating a koinViewModel only
        viewModelStoreOwner = parentEntry       // just changing the "viewModelStoreOwner" to "parentEntry"
    )                               // & to obtain "parentEntry" all this code was required
}                       // All this "NavBackStackEntry" & "navController" is just required to generate
                        // this "parentEntry" object :)