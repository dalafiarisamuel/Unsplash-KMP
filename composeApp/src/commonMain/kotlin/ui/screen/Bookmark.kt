package ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import data.ui.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import ui.component.NavBar
import ui.component.UnsplashImageList
import ui.component.getEdgeToEdgeTopPadding
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.bookmarks

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
internal fun Bookmark(
    modifier: Modifier = Modifier,
    showNavigationBackIcon: Boolean = true,
    imageList: Flow<PagingData<Photo>> = flowOf(PagingData.empty()),
    onBackPressed: () -> Unit = {},
    onItemClicked: (Photo) -> Unit,
) {

    Column(
        modifier =
            Modifier.background(MaterialTheme.colors.background)
                .fillMaxSize()
                .padding(top = 10.dp + getEdgeToEdgeTopPadding(), start = 12.dp, end = 12.dp)
                .then(modifier)
    ) {
        NavBar(
            modifier = Modifier.defaultMinSize(minHeight = 30.dp).fillMaxWidth(),
            navigationBarTitle = stringResource(Res.string.bookmarks),
            showNavigationBackIcon = showNavigationBackIcon,
            onBackPressed = onBackPressed,
        )

        val lazyGridState = rememberLazyStaggeredGridState()
        val coroutine = rememberCoroutineScope()

        UnsplashImageList(
            modifier = Modifier.fillMaxSize(),
            imageList = imageList,
            lazyGridState = lazyGridState,
            nestedScrollConnection = { object : NestedScrollConnection {} },
            onItemClicked = { it?.let { onItemClicked(it) } },
            onScrollToTop = {
                coroutine.launch(Dispatchers.Main) { lazyGridState.animateScrollToItem(0) }
            },
            fixedGridCell = false,
        )
        Spacer(modifier = Modifier.padding(top = 30.dp))
    }
}
