package ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.window.core.layout.WindowSizeClass
import coil3.compose.AsyncImage
import data.ui.model.Photo
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.appDark
import ui.theme.appWhite
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.ic_image_search
import unsplashkmp.composeapp.generated.resources.searched_term_not_found

private const val MINIMUM_SCROLL = -2_000f

@ExperimentalFoundationApi
@Composable
internal fun UnsplashImageList(
    modifier: Modifier,
    imageList: Flow<PagingData<Photo>>,
    lazyGridState: LazyStaggeredGridState,
    nestedScrollConnection: () -> NestedScrollConnection,
    onItemClicked: (Photo?) -> Unit,
    onItemLongClicked: (Photo?) -> Unit = {},
    onScrollToTop: () -> Unit = {},
    fixedGridCell: Boolean = true,
) {
    val list = imageList.collectAsLazyPagingItems()
    val isListEmpty by derivedStateOf { list.itemCount <= 0 }
    if (list.loadState.refresh is LoadState.Loading) {
        LoadingView(modifier = modifier)
    } else {
        Crossfade(targetState = isListEmpty, label = "") {
            if (it) {
                EmptyListStateComponent(modifier = modifier)
            } else {
                PhotosList(
                    imageList = list,
                    lazyGridState = lazyGridState,
                    nestedScrollConnection = nestedScrollConnection,
                    onItemClicked = onItemClicked,
                    onItemLongClicked = onItemLongClicked,
                    onScrollToTop = onScrollToTop,
                    fixedGridCell = fixedGridCell,
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun PhotosList(
    imageList: LazyPagingItems<Photo>,
    lazyGridState: LazyStaggeredGridState,
    nestedScrollConnection: () -> NestedScrollConnection,
    onItemClicked: (Photo?) -> Unit,
    onItemLongClicked: (Photo?) -> Unit,
    onScrollToTop: () -> Unit = {},
    fixedGridCell: Boolean,
) {

    var isScrollToTopVisible by rememberSaveable { mutableStateOf(false) }

    var totalScrolled by rememberSaveable { mutableStateOf(0f) }

    val scrollButtonConnection = retain {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                totalScrolled += consumed.y
                isScrollToTopVisible = totalScrolled < MINIMUM_SCROLL

                return super.onPostScroll(consumed, available, source)
            }
        }
    }

    val wind = currentWindowAdaptiveInfo(supportLargeAndXLargeWidth = true)
    val gridSize =
        if (fixedGridCell) {
            2
        } else {
            when {
                wind.windowSizeClass.isWidthAtLeastBreakpoint(
                    WindowSizeClass.WIDTH_DP_LARGE_LOWER_BOUND
                ) -> 5
                wind.windowSizeClass.isWidthAtLeastBreakpoint(
                    WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
                ) -> 3
                wind.windowSizeClass.isWidthAtLeastBreakpoint(
                    WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
                ) -> 2
                else -> 2
            }
        }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalStaggeredGrid(
            state = lazyGridState,
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 30.dp),
            modifier =
                Modifier.padding(top = 15.dp)
                    .nestedScroll(nestedScrollConnection())
                    .nestedScroll(scrollButtonConnection),
            columns = StaggeredGridCells.Fixed(gridSize),
        ) {
            lazyItems(imageList) { photo ->
                key(photo?.id) {
                    UnsplashImageStaggered(
                        modifier = Modifier,
                        data = photo,
                        onImageClicked = onItemClicked,
                        onImageLongClicked = onItemLongClicked,
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = isScrollToTopVisible,
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it },
            modifier = Modifier.padding(vertical = 30.dp).align(Alignment.BottomCenter),
        ) {
            FloatingActionButton(
                onClick = {
                    totalScrolled = 0f
                    isScrollToTopVisible = false
                    onScrollToTop()
                },
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    Icons.Rounded.KeyboardArrowUp,
                    tint = appWhite,
                    contentDescription = "Scroll to top",
                )
            }
        }
    }
}

@Preview
@Composable
fun EmptyListStateComponent(
    modifier: Modifier = Modifier,
    term: String = stringResource(Res.string.searched_term_not_found),
) {

    Column(
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(100.dp).background(color = appDark, shape = CircleShape),
            painter = painterResource(Res.drawable.ic_image_search),
            contentScale = ContentScale.Inside,
            contentDescription = null,
        )

        Text(
            text = term,
            color = appWhite,
            fontSize = 13.sp,
            fontStyle = FontStyle.Normal,
            modifier = Modifier.padding(top = 10.dp),
        )
    }
}

@ExperimentalFoundationApi
@Composable
private fun UnsplashImageStaggered(
    modifier: Modifier,
    data: Photo?,
    onImageClicked: (Photo?) -> Unit,
    onImageLongClicked: (Photo?) -> Unit,
) {

    val aspectRatio: Float by remember {
        derivedStateOf { (data?.width?.toFloat() ?: 1.0F) / (data?.height?.toFloat() ?: 1.0F) }
    }

    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(10.dp),
        modifier =
            Modifier.fillMaxWidth()
                .combinedClickable(
                    onClick = { onImageClicked(data) },
                    onLongClick = { onImageLongClicked(data) },
                )
                .then(modifier),
    ) {
        AsyncImage(
            model = data?.urls?.small,
            contentDescription = data?.description,
            contentScale = ContentScale.FillBounds,
            modifier =
                Modifier.aspectRatio(aspectRatio).fillMaxWidth().defaultMinSize(minHeight = 200.dp),
        )
    }
}

@ExperimentalFoundationApi
private fun <T : Any> LazyStaggeredGridScope.lazyItems(
    lazyPagingItems: LazyPagingItems<T>,
    itemContent: @Composable LazyStaggeredGridItemScope.(value: T?) -> Unit,
) {
    items(lazyPagingItems.itemCount) { index -> itemContent(lazyPagingItems[index]) }
}

@ExperimentalFoundationApi
private fun <T : Any> LazyStaggeredGridScope.lazyItemsIndexed(
    lazyPagingItems: LazyPagingItems<T>,
    key: ((item: T?) -> Any)? = null,
    itemContent: @Composable LazyStaggeredGridItemScope.(value: T?) -> Unit,
) {
    items(lazyPagingItems.itemCount, key?.let { { index -> key(lazyPagingItems[index]) } }) { index
        ->
        itemContent(lazyPagingItems[index])
    }
}
