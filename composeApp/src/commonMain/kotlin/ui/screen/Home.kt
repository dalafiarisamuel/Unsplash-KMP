package ui.screen


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import data.model.ui.Photo
import data.model.ui.dummyPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.component.ChipComponent
import ui.component.CollapsibleSearchBar
import ui.component.HomeScreenTitleAlignment
import ui.component.UnsplashImageList
import ui.component.getEdgeToEdgeTopPadding
import ui.dialog.ImagePreviewDialog
import ui.event.HomeScreenEvent
import ui.state.HomeScreenState
import ui.theme.UnsplashKMPTheme
import ui.theme.appWhite
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.unsplash_images

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
internal fun HomeScreen(
    state: HomeScreenState = HomeScreenState(),
    imageList: Flow<PagingData<Photo>> = flowOf(PagingData.empty()),
    onLongClicked: (Photo) -> Unit = {},
    dispatch: (HomeScreenEvent) -> Unit = {},
) {

    val lazyGridState = rememberLazyStaggeredGridState()
    val coroutine = rememberCoroutineScope()
    val toolbarHeight = 105.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    var toolbarOffsetHeightPx by remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx + delta
                toolbarOffsetHeightPx = newOffset.coerceIn(-toolbarHeightPx, 0f)
                // Returning Zero so we just observe the scroll but don't execute it
                return Offset.Zero
            }
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ) {

        Spacer(modifier = Modifier.padding(top = 20.dp + getEdgeToEdgeTopPadding()))

        Text(
            modifier = Modifier.align(HomeScreenTitleAlignment()),
            text = stringResource(Res.string.unsplash_images),
            color = appWhite,
            fontSize = 22.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold
        )

        CollapsibleSearchBar(
            toolbarOffset = toolbarOffsetHeightPx,
            toolbarHeight = toolbarHeight,
            keyboardAction = {
                coroutine.launch(Dispatchers.Main) {
                    lazyGridState.scrollToItem(0)
                }
                dispatch(HomeScreenEvent.Search)
            },
            textValue = state.searchFieldValue,
            textValueChange = {
                dispatch(HomeScreenEvent.UpdateSearchField(it))
            },
        )

        ChipComponent(
            modifier = Modifier
                .testTag("chip_group")
                .padding(top = 20.dp),
            selectedText = state.searchFieldValue,
        ) {
            coroutine.launch(Dispatchers.Main) {
                lazyGridState.scrollToItem(0)
            }
            dispatch(HomeScreenEvent.SelectChip(it))
        }

        UnsplashImageList(
            modifier = Modifier.fillMaxSize(),
            imageList = imageList,
            lazyGridState = lazyGridState,
            nestedScrollConnection = nestedScrollConnection,
            onItemClicked = {
                dispatch(HomeScreenEvent.OnImageClicked(it))
            },
            onItemLongClicked = {
                it?.let { onLongClicked(it) }
            }
        )

        if (state.isImagePreviewDialogVisible) {
            ImagePreviewDialog(
                photo = state.selectedImage
            ) {
                dispatch(HomeScreenEvent.ImagePreviewDialog.Dismiss)
            }
        }
    }

}

@ExperimentalComposeUiApi
@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun PreviewHomeScreen() {

    UnsplashKMPTheme {
        HomeScreen(
            state = HomeScreenState(
                selectedImage = dummyPhoto,
                isImagePreviewDialogVisible = true,
                isDownloadImageDialogVisible = false,
                searchFieldValue = "Comet"
            ),
            imageList = flowOf(PagingData.from(listOf(dummyPhoto)))
        )
    }
}
