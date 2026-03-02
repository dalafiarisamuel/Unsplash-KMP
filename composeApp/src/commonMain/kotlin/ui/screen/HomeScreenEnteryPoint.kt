package ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FabPosition
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import ui.component.BookmarkFab
import ui.component.SaveNewChip
import ui.event.HomeScreenEvent
import ui.theme.appDark
import ui.viewmodel.HomeScreenViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
internal fun HomeScreenEntryPoint(
    navigateToDetailScreen: (imageId: String) -> Unit = {},
    navigateToBookmarks: () -> Unit = {},
    resetSearchInput: () -> Unit = {},
    flipTheme: () -> Unit = {},
    isDarkTheme: Boolean = false,
    viewModel: HomeScreenViewModel = koinViewModel<HomeScreenViewModel>(),
) {

    val coroutine = rememberCoroutineScope()
    val modalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
        )

    LaunchedEffect(viewModel.state.isSaveQueryDialogVisible) {
        if (viewModel.state.isSaveQueryDialogVisible) {
            modalBottomSheetState.show()
        } else {
            modalBottomSheetState.hide()
        }
    }

    LaunchedEffect(modalBottomSheetState.isVisible) {
        if (!modalBottomSheetState.isVisible && viewModel.state.isSaveQueryDialogVisible) {
            viewModel.dispatch(HomeScreenEvent.SearchQueryChip.DismissSaveQueryDialog)
        }
    }

    val bookmarkPhotoCount by viewModel.bookmarkPhotoCount.collectAsStateWithLifecycle()

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetBackgroundColor = appDark,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContent = {
            SaveNewChip(
                isDialogVisible = viewModel.state.isSaveQueryDialogVisible,
                onSave = { query ->
                    viewModel.dispatch(HomeScreenEvent.SearchQueryChip.Save(query))
                    coroutine.launch { modalBottomSheetState.hide() }
                },
            )
        },
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                if (viewModel.state.isSaveQueryDialogVisible.not()) {
                    BookmarkFab(bookmarkPhotoCount, navigateToBookmarks)
                }
            },
            floatingActionButtonPosition = FabPosition.End,
        ) {
            HomeScreen(
                state = viewModel.state,
                imageList = viewModel.photos,
                dispatch = viewModel::dispatch,
                resetSearchInput = resetSearchInput,
                isDarkTheme = isDarkTheme,
                flipTheme = flipTheme,
                savedSearchQuery = viewModel.savedSearchQuery.collectAsStateWithLifecycle().value,
            ) { image ->
                navigateToDetailScreen(image.id)
            }
        }
    }
}
