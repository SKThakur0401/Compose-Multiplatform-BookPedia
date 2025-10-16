package com.plcoding.bookpedia.book.presentation.book_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import com.plcoding.bookpedia.core.presentation.components.BookPediaIcons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.favorites
import cmp_bookpedia.composeapp.generated.resources.search_results
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.presentation.components.*
import com.plcoding.bookpedia.core.presentation.theme.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PremiumBookListScreen(
    state: BookListState,
    onAction: (BookListAction) -> Unit,
    onCartClick: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var viewMode by remember { mutableStateOf(ViewMode.Grid) }
    var showSearch by remember { mutableStateOf(false) }
    
    val pagerState = rememberPagerState { 2 }
    val searchResultsListState = rememberLazyListState()
    val favoriteBooksListState = rememberLazyListState()

    LaunchedEffect(state.searchResults) {
        searchResultsListState.animateScrollToItem(0)
    }

    LaunchedEffect(state.selectedTabIndex) {
        pagerState.animateScrollToPage(state.selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage) {
        onAction(BookListAction.OnTabSelected(pagerState.currentPage))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background with premium gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            BookPediaColors.GradientStart,
                            BookPediaColors.GradientMiddle,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Hero Section with App Branding
            HeroSection(
                onSearchClick = { showSearch = !showSearch },
                showSearch = showSearch,
                searchQuery = state.searchQuery,
                onSearchQueryChange = { onAction(BookListAction.OnSearchQueryChange(it)) },
                onSearchSubmit = { keyboardController?.hide() }
            )
            
            // Main Content Area
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.background,
                shape = BookPediaCustomShapes.TopContainer
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Tab Row with enhanced styling
                    EnhancedTabRow(
                        selectedTabIndex = state.selectedTabIndex,
                        onTabClick = { onAction(BookListAction.OnTabSelected(it)) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                    
                    // View Mode Toggle
                    ViewModeToggle(
                        viewMode = viewMode,
                        onViewModeChange = { viewMode = it },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    
                    // Content Pager
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f)
                    ) { page ->
                        when (page) {
                            0 -> SearchResultsContent(
                                state = state,
                                viewMode = viewMode,
                                onBookClick = { onAction(BookListAction.OnBookClick(it)) }
                            )
                            1 -> FavoriteBooksContent(
                                state = state,
                                viewMode = viewMode,
                                onBookClick = { onAction(BookListAction.OnBookClick(it)) }
                            )
                        }
                    }
                }
            }
        }
        
        // Floating Cart Button with enhanced styling
        BadgedFloatingActionButton(
            onClick = onCartClick,
            icon = Icons.Default.ShoppingCart,
            badgeCount = 0, // TODO: Add cart count from state
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun HeroSection(
    onSearchClick: () -> Unit,
    showSearch: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Title and Greeting
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.animateContentSize()
        ) {
            Text(
                text = "BookPedia",
                style = BookPediaCustomTypography.SplashTitle.copy(fontSize = 32.sp),
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(4.dp))
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Enhanced Search Bar
        AnimatedContent(
            targetState = showSearch,
            label = "search_animation"
        ) { isExpanded ->
            if (isExpanded) {
                CompactSearchBar(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    onSearch = { onSearchSubmit() },
                    placeholder = "Find your book...",
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                PremiumButton(
                    text = "Search Books",
                    onClick = onSearchClick,
                    style = ButtonStyle.Outlined,
                    size = ButtonSize.Medium,
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
            }
        }
    }
}

@Composable
private fun EnhancedTabRow(
    selectedTabIndex: Int,
    onTabClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        stringResource(Res.string.search_results),
        stringResource(Res.string.favorites)
    )
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = selectedTabIndex == index
            
            PremiumButton(
                text = title,
                onClick = { onTabClick(index) },
                style = if (isSelected) ButtonStyle.Primary else ButtonStyle.Outlined,
                size = ButtonSize.Medium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ViewModeToggle(
    viewMode: ViewMode,
    onViewModeChange: (ViewMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            ),
            shape = BookPediaCustomShapes.CategoryChip
        ) {
            Row {
                IconButton(
                    onClick = { onViewModeChange(ViewMode.Grid) }
                ) {
                    Icon(
                        imageVector = /*BookPediaIcons.Grid*/ Icons.Default.Star,
                        contentDescription = "Grid View",
                        tint = if (viewMode == ViewMode.Grid)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                IconButton(
                    onClick = { onViewModeChange(ViewMode.List) }
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "List View",
                        tint = if (viewMode == ViewMode.List)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResultsContent(
    state: BookListState,
    viewMode: ViewMode,
    onBookClick: (Book) -> Unit
) {
    when {
        state.isLoading -> {
            LoadingContent()
        }
        state.searchResults.isEmpty() && state.searchQuery.isBlank() -> {
            EmptyState(
                type = EmptyStateType.NoBooks,
                onActionClick = null
            )
        }
        state.searchResults.isEmpty() && state.searchQuery.isNotBlank() -> {
            EmptyState(
                type = EmptyStateType.NoSearchResults,
                customTitle = "No results for \"${state.searchQuery}\"",
                onActionClick = null
            )
        }
        else -> {
            BookGrid(
                books = state.searchResults,
                viewMode = viewMode,
                onBookClick = onBookClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun FavoriteBooksContent(
    state: BookListState,
    viewMode: ViewMode,
    onBookClick: (Book) -> Unit
) {
    if (state.favoriteBooks.isEmpty()) {
        EmptyState(
            type = EmptyStateType.NoFavorites,
            onActionClick = null
        )
    } else {
        BookGrid(
            books = state.favoriteBooks,
            viewMode = viewMode,
            onBookClick = onBookClick,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BookGrid(
    books: List<Book>,
    viewMode: ViewMode,
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    when (viewMode) {
        ViewMode.Grid -> {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                modifier = modifier,
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(books, key = { it.id }) { book ->
                    BookCard(
                        book = book,
                        onBookClick = onBookClick,
                        cardStyle = BookCardStyle.Standard,
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            }
        }
        ViewMode.List -> {
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(books, key = { it.id }) { book ->
                    BookCard(
                        book = book,
                        onBookClick = onBookClick,
                        cardStyle = BookCardStyle.Featured,
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp,
                modifier = Modifier.size(48.dp)
            )
            
            Text(
                text = "Loading amazing books...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

enum class ViewMode {
    Grid, List
}
