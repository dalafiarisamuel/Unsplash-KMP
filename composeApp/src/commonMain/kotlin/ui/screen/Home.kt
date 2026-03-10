package ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrightnessAuto
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import data.ui.model.ChipData
import data.ui.model.Photo
import data.ui.model.Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import ui.component.ChipComponent
import ui.component.CollapsibleSearchBar
import ui.component.UnsplashImageList
import ui.component.getEdgeToEdgeTopPadding
import ui.dialog.ImagePreviewDialog
import ui.event.HomeScreenEvent
import ui.state.HomeScreenState
import ui.theme.appWhite
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.unsplash_images

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
internal fun HomeScreen(
    state: HomeScreenState = HomeScreenState(),
    imageList: Flow<PagingData<Photo>> = flowOf(PagingData.empty()),
    dispatch: (HomeScreenEvent) -> Unit = {},
    theme: Theme = Theme.SYSTEM,
    savedSearchQuery: List<ChipData> = emptyList(),
    resetSearchInput: () -> Unit = {},
    onImageClicked: (Photo) -> Unit = {},
) {

    val coroutineScope = rememberCoroutineScope()
    val lazyGridState = rememberLazyStaggeredGridState()
    val toolbarState = rememberToolbarState(105.dp)

    Column(
        modifier =
            Modifier.background(MaterialTheme.colors.background)
                .fillMaxSize()
                .padding(horizontal = 10.dp)
    ) {
        Spacer(modifier = Modifier.padding(top = 10.dp + getEdgeToEdgeTopPadding()))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(Res.string.unsplash_images),
                color = appWhite,
                fontSize = 22.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
            )

            IconButton(
                onClick = { dispatch(HomeScreenEvent.ThemeSelectionDialog.Open) },
                modifier = Modifier.align(Alignment.CenterVertically),
            ) {
                val icon =
                    when (theme) {
                        Theme.SYSTEM -> Icons.Rounded.BrightnessAuto
                        Theme.LIGHT -> Icons.Rounded.LightMode
                        Theme.DARK -> Icons.Rounded.DarkMode
                    }
                Icon(imageVector = icon, tint = appWhite, contentDescription = null)
            }
        }

        CollapsibleSearchBar(
            toolbarOffset = toolbarState.offset,
            toolbarHeight = toolbarState.height,
            keyboardAction = {
                coroutineScope.launch(Dispatchers.Main) {
                    toolbarState.reset()
                    lazyGridState.animateScrollToItem(0)
                    dispatch(HomeScreenEvent.Search)
                    resetSearchInput()
                }
            },
            textValue = state.searchFieldValue,
            textValueChange = {
                dispatch(HomeScreenEvent.UpdateSearchField(it))
                resetSearchInput()
            },
        )

        ChipComponent(
            modifier = Modifier.testTag("chip_group").padding(top = 20.dp),
            selectedText = state.searchFieldValue,
            savedSearchQuery = savedSearchQuery,
            onAddMoreChipClicked = { dispatch(HomeScreenEvent.SearchQueryChip.OpenSaveQueryDialog) },
        ) {
            coroutineScope.launch(Dispatchers.Main) {
                toolbarState.reset()
                lazyGridState.animateScrollToItem(0)
                dispatch(HomeScreenEvent.SelectChip(it))
                resetSearchInput()
            }
        }

        UnsplashImageList(
            modifier = Modifier.fillMaxSize(),
            imageList = imageList,
            lazyGridState = lazyGridState,
            nestedScrollConnection = { toolbarState.nestedScrollConnection },
            onItemClicked = { it?.let { onImageClicked(it) } },
            onItemLongClicked = { dispatch(HomeScreenEvent.OnImageLongClicked(it)) },
            onScrollToTop = {
                coroutineScope.launch(Dispatchers.Main) {
                    toolbarState.reset()
                    lazyGridState.animateScrollToItem(0)
                }
            },
        )

        if (state.isImagePreviewDialogVisible) {
            ImagePreviewDialog(photo = state.selectedImage) {
                dispatch(HomeScreenEvent.ImagePreviewDialog.Dismiss)
            }
        }
    }
}
