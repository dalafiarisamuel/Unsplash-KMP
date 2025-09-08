@file:OptIn(ExperimentalSharedTransitionApi::class)

package ui.component


import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import data.model.ui.Photo
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.appDark
import ui.theme.appWhite
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.ic_image_search
import unsplashkmp.composeapp.generated.resources.searched_term_not_found


@ExperimentalFoundationApi
@Composable
internal fun SharedTransitionScope.UnsplashImageList(
    modifier: Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    imageList: Flow<PagingData<Photo>>,
    lazyGridState: LazyStaggeredGridState,
    nestedScrollConnection: NestedScrollConnection,
    onItemClicked: (Photo?) -> Unit,
    onItemLongClicked: (Photo?) -> Unit,
) {
    val list = imageList.collectAsLazyPagingItems()
    val isListEmpty by remember { derivedStateOf { list.itemCount <= 0 } }
    if (list.loadState.refresh is LoadState.Loading) {
        LoadingView(modifier = Modifier.fillMaxSize())
    } else {
        Crossfade(targetState = isListEmpty, label = "") {
            if (it) {
                EmptyListStateComponent(modifier = Modifier.fillMaxSize())
            } else {
                PhotosList(
                    modifier = modifier,
                    animatedVisibilityScope = animatedVisibilityScope,
                    imageList = list,
                    lazyGridState = lazyGridState,
                    nestedScrollConnection = nestedScrollConnection,
                    onItemClicked = onItemClicked,
                    onItemLongClicked = onItemLongClicked,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@ExperimentalFoundationApi
@Composable
private fun SharedTransitionScope.PhotosList(
    modifier: Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    imageList: LazyPagingItems<Photo>,
    lazyGridState: LazyStaggeredGridState,
    nestedScrollConnection: NestedScrollConnection,
    onItemClicked: (Photo?) -> Unit,
    onItemLongClicked: (Photo?) -> Unit,
) {

    val windowSizeClass = calculateWindowSizeClass()

    val gridSize = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 2
        WindowWidthSizeClass.Medium -> 3
        else -> 4
    }

    LazyVerticalStaggeredGrid(
        state = lazyGridState,
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(top = 15.dp)
            .nestedScroll(nestedScrollConnection)
            .then(
                modifier.sharedBounds(
                    sharedContentState = rememberSharedContentState("photo_image"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
            ),
        columns = StaggeredGridCells.Fixed(gridSize)
    ) {
        lazyItems(imageList) { photo ->
            key(photo?.id) {
                UnsplashImageStaggered(
                    modifier = Modifier.animateItem(),
                    data = photo,
                    onImageClicked = onItemClicked,
                    onImageLongClicked = onItemLongClicked
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier
                .size(100.dp)
                .background(color = appDark, shape = CircleShape),
            painter = painterResource(Res.drawable.ic_image_search),
            contentScale = ContentScale.Inside,
            contentDescription = null
        )

        Text(
            text = term,
            color = appWhite,
            fontSize = 13.sp,
            fontStyle = FontStyle.Normal,
            modifier = Modifier.padding(top = 10.dp)
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
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onImageClicked(data) },
                onLongClick = { onImageLongClicked(data) },
            )
            .then(modifier)

    ) {
        AsyncImage(
            model = data?.urls?.small,
            contentDescription = data?.description,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .aspectRatio(aspectRatio)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 200.dp)
        )
    }

}

@ExperimentalFoundationApi
private fun <T : Any> LazyStaggeredGridScope.lazyItems(
    lazyPagingItems: LazyPagingItems<T>,
    itemContent: @Composable LazyStaggeredGridItemScope.(value: T?) -> Unit,
) {
    items(lazyPagingItems.itemCount) { index ->
        itemContent(lazyPagingItems[index])
    }
}

@ExperimentalFoundationApi
private fun <T : Any> LazyStaggeredGridScope.lazyItemsIndexed(
    lazyPagingItems: LazyPagingItems<T>,
    key: ((item: T?) -> Any)? = null,
    itemContent: @Composable LazyStaggeredGridItemScope.(value: T?) -> Unit,
) {
    items(
        lazyPagingItems.itemCount,
        key?.let { { index -> key(lazyPagingItems[index]) } }
    ) { index ->
        itemContent(lazyPagingItems[index])
    }
}

