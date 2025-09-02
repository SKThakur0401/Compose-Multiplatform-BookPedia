package com.plcoding.bookpedia.core.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Custom icons for BookPedia app using available Material Icons and custom paths
 */
object BookPediaIcons {
    
    // Use available Material Icons
    val ArrowBack = Icons.Default.ArrowBack
    val Star = Icons.Default.Star
    val Favorite = Icons.Default.Favorite
    val FavoriteBorder = Icons.Default.FavoriteBorder
    val Add = Icons.Default.Add
    val Remove = Icons.Default.Star
    val Search = Icons.Default.Search
    val Close = Icons.Default.Close
    
    // Custom book icon using simple path
    val Book: ImageVector
        get() {
            if (_book != null) {
                return _book!!
            }
            _book = materialIcon(name = "Filled.Book") {
                materialPath {
                    moveTo(18.0f, 2.0f)
                    horizontalLineTo(6.0f)
                    curveTo(4.9f, 2.0f, 4.0f, 2.9f, 4.0f, 4.0f)
                    verticalLineTo(20.0f)
                    curveTo(4.0f, 21.1f, 4.9f, 22.0f, 6.0f, 22.0f)
                    horizontalLineTo(18.0f)
                    curveTo(19.1f, 22.0f, 20.0f, 21.1f, 20.0f, 20.0f)
                    verticalLineTo(4.0f)
                    curveTo(20.0f, 2.9f, 19.1f, 2.0f, 18.0f, 2.0f)
                    close()
                    moveTo(18.0f, 20.0f)
                    horizontalLineTo(6.0f)
                    verticalLineTo(4.0f)
                    horizontalLineTo(18.0f)
                    verticalLineTo(20.0f)
                    close()
                }
            }
            return _book!!
        }
    
    private var _book: ImageVector? = null
    
    // Custom library/books icon
    val LibraryBooks: ImageVector
        get() {
            if (_libraryBooks != null) {
                return _libraryBooks!!
            }
            _libraryBooks = materialIcon(name = "Filled.LibraryBooks") {
                materialPath {
                    moveTo(4.0f, 6.0f)
                    horizontalLineTo(2.0f)
                    verticalLineTo(20.0f)
                    curveTo(2.0f, 21.1f, 2.9f, 22.0f, 4.0f, 22.0f)
                    horizontalLineTo(18.0f)
                    verticalLineTo(20.0f)
                    horizontalLineTo(4.0f)
                    verticalLineTo(6.0f)
                    close()
                    moveTo(20.0f, 2.0f)
                    horizontalLineTo(8.0f)
                    curveTo(6.9f, 2.0f, 6.0f, 2.9f, 6.0f, 4.0f)
                    verticalLineTo(16.0f)
                    curveTo(6.0f, 17.1f, 6.9f, 18.0f, 8.0f, 18.0f)
                    horizontalLineTo(20.0f)
                    curveTo(21.1f, 18.0f, 22.0f, 17.1f, 22.0f, 16.0f)
                    verticalLineTo(4.0f)
                    curveTo(22.0f, 2.9f, 21.1f, 2.0f, 20.0f, 2.0f)
                    close()
                    moveTo(20.0f, 16.0f)
                    horizontalLineTo(8.0f)
                    verticalLineTo(4.0f)
                    horizontalLineTo(20.0f)
                    verticalLineTo(16.0f)
                    close()
                }
            }
            return _libraryBooks!!
        }
    
    private var _libraryBooks: ImageVector? = null
    
    // Custom shopping cart icon
    val ShoppingCart: ImageVector
        get() {
            if (_shoppingCart != null) {
                return _shoppingCart!!
            }
            _shoppingCart = materialIcon(name = "Filled.ShoppingCart") {
                materialPath {
                    moveTo(7.0f, 18.0f)
                    curveTo(5.9f, 18.0f, 5.01f, 18.9f, 5.01f, 20.0f)
                    reflectiveCurveTo(5.9f, 22.0f, 7.0f, 22.0f)
                    reflectiveCurveTo(9.0f, 21.1f, 9.0f, 20.0f)
                    reflectiveCurveTo(8.1f, 18.0f, 7.0f, 18.0f)
                    close()
                    moveTo(1.0f, 2.0f)
                    verticalLineTo(4.0f)
                    horizontalLineTo(3.0f)
                    lineToRelative(3.6f, 7.59f)
                    lineToRelative(-1.35f, 2.45f)
                    curveTo(5.04f, 14.29f, 5.0f, 14.63f, 5.0f, 15.0f)
                    curveTo(5.0f, 16.1f, 5.9f, 17.0f, 7.0f, 17.0f)
                    horizontalLineTo(19.0f)
                    verticalLineTo(15.0f)
                    horizontalLineTo(7.42f)
                    curveTo(7.28f, 15.0f, 7.17f, 14.89f, 7.17f, 14.75f)
                    curveTo(7.17f, 14.7f, 7.18f, 14.66f, 7.2f, 14.63f)
                    lineTo(8.1f, 13.0f)
                    horizontalLineTo(15.55f)
                    curveTo(16.3f, 13.0f, 16.96f, 12.59f, 17.3f, 11.97f)
                    lineTo(21.88f, 4.0f)
                    horizontalLineTo(5.21f)
                    lineToRelative(-0.94f, -2.0f)
                    horizontalLineTo(1.0f)
                    close()
                    moveTo(17.0f, 18.0f)
                    curveTo(15.9f, 18.0f, 15.01f, 18.9f, 15.01f, 20.0f)
                    reflectiveCurveTo(15.9f, 22.0f, 17.0f, 22.0f)
                    reflectiveCurveTo(19.0f, 21.1f, 19.0f, 20.0f)
                    reflectiveCurveTo(18.1f, 18.0f, 17.0f, 18.0f)
                    close()
                }
            }
            return _shoppingCart!!
        }
    
    private var _shoppingCart: ImageVector? = null
    
    // Custom bookmark icon
    val Bookmark: ImageVector
        get() {
            if (_bookmark != null) {
                return _bookmark!!
            }
            _bookmark = materialIcon(name = "Filled.Bookmark") {
                materialPath {
                    moveTo(17.0f, 3.0f)
                    horizontalLineTo(7.0f)
                    curveTo(6.0f, 3.0f, 5.0f, 4.0f, 5.0f, 5.0f)
                    verticalLineTo(21.0f)
                    lineToRelative(7.0f, -3.0f)
                    lineToRelative(7.0f, 3.0f)
                    verticalLineTo(5.0f)
                    curveTo(19.0f, 4.0f, 18.0f, 3.0f, 17.0f, 3.0f)
                    close()
                }
            }
            return _bookmark!!
        }
    
    private var _bookmark: ImageVector? = null
    
    // Custom bookmark border icon
    val BookmarkBorder: ImageVector
        get() {
            if (_bookmarkBorder != null) {
                return _bookmarkBorder!!
            }
            _bookmarkBorder = materialIcon(name = "Filled.BookmarkBorder") {
                materialPath {
                    moveTo(17.0f, 3.0f)
                    horizontalLineTo(7.0f)
                    curveTo(6.0f, 3.0f, 5.0f, 4.0f, 5.0f, 5.0f)
                    verticalLineTo(21.0f)
                    lineToRelative(7.0f, -3.0f)
                    lineToRelative(7.0f, 3.0f)
                    verticalLineTo(5.0f)
                    curveTo(19.0f, 4.0f, 18.0f, 3.0f, 17.0f, 3.0f)
                    close()
                    moveTo(17.0f, 18.0f)
                    lineToRelative(-5.0f, -2.18f)
                    lineTo(7.0f, 18.0f)
                    verticalLineTo(5.0f)
                    horizontalLineTo(17.0f)
                    verticalLineTo(18.0f)
                    close()
                }
            }
            return _bookmarkBorder!!
        }
    
    private var _bookmarkBorder: ImageVector? = null

    // Custom grid icon for view toggle
/*    val Grid: ImageVector
        get() {
            if (_grid != null) {
                return _grid!!
            }
            _grid = materialIcon(name = "Filled.Grid") {
                materialPath {
                    moveTo(4.0f, 6.0f)
                    horizontalLineTo(8.0f)
                    verticalLineTo(10.0f)
                    horizontalLineTo(4.0f)
                    verticalLineTo(6.0f)
                    close()
                    moveTo(4.0f, 11.0f)
                    horizontalLineTo(8.0f)
                    verticalLineTo(15.0f)
                    horizontalLineToH(4.0f)
                    verticalLineTo(11.0f)
                    close()
                    moveTo(4.0f, 16.0f)
                    horizontalLineTo(8.0f)
                    verticalLineTo(20.0f)
                    horizontalLineToH(4.0f)
                    verticalLineTo(16.0f)
                    close()
                    moveTo(9.0f, 6.0f)
                    horizontalLineTo(13.0f)
                    verticalLineTo(10.0f)
                    horizontalLineToH(9.0f)
                    verticalLineTo(6.0f)
                    close()
                    moveTo(9.0f, 11.0f)
                    horizontalLineToH(13.0f)
                    verticalLineTo(15.0f)
                    horizontalLineToH(9.0f)
                    verticalLineToV(11.0f)
                    close()
                    moveTo(9.0f, 16.0f)
                    horizontalLineToH(13.0f)
                    verticalLineTo(20.0f)
                    horizontalLineToH(9.0f)
                    verticalLineToV(16.0f)
                    close()
                    moveTo(14.0f, 6.0f)
                    horizontalLineToH(18.0f)
                    verticalLineTo(10.0f)
                    horizontalLineToH(14.0f)
                    verticalLineToV(6.0f)
                    close()
                    moveTo(14.0f, 11.0f)
                    horizontalLineToH(18.0f)
                    verticalLineTo(15.0f)
                    horizontalLineToH(14.0f)
                    verticalLineToV(11.0f)
                    close()
                    moveTo(14.0f, 16.0f)
                    horizontalLineToH(18.0f)
                    verticalLineTo(20.0f)
                    horizontalLineToH(14.0f)
                    verticalLineToV(16.0f)
                    close()
                }
            }
            return _grid!!
        }*/

    private var _grid: ImageVector? = null
}
