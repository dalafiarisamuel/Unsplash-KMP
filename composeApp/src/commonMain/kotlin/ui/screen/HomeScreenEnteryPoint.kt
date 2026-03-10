package ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
import data.ui.model.Theme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import ui.component.BookmarkFab
import ui.component.SaveNewChip
import ui.dialog.ThemeSelectionSheet
import ui.event.HomeScreenEvent
import ui.state.HomeScreenState
import ui.theme.appDark
import ui.viewmodel.HomeScreenViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
internal fun HomeScreenEntryPoint(
    navigateToDetailScreen: (imageId: String) -> Unit = {},
    navigateToBookmarks: () -> Unit = {},
    resetSearchInput: () -> Unit = {},
    theme: Theme = Theme.SYSTEM,
    setTheme: (Theme) -> Unit = {},
    viewModel: HomeScreenViewModel = koinViewModel<HomeScreenViewModel>(),
) {

    val coroutine = rememberCoroutineScope()
    val modalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
        )

    LaunchedEffect(
        viewModel.state.isSaveQueryDialogVisible,
        viewModel.state.isThemeSelectionDialogVisible,
    ) {
        if (
            viewModel.state.isSaveQueryDialogVisible ||
                viewModel.state.isThemeSelectionDialogVisible
        ) {
            modalBottomSheetState.show()
        } else {
            modalBottomSheetState.hide()
        }
    }

    LaunchedEffect(modalBottomSheetState.isVisible) {
        if (!modalBottomSheetState.isVisible) {
            if (viewModel.state.isSaveQueryDialogVisible) {
                viewModel.dispatch(HomeScreenEvent.SearchQueryChip.DismissSaveQueryDialog)
            }
            if (viewModel.state.isThemeSelectionDialogVisible) {
                viewModel.dispatch(HomeScreenEvent.ThemeSelectionDialog.Dismiss)
            }
        }
    }

    val bookmarkPhotoCount by viewModel.bookmarkPhotoCount.collectAsStateWithLifecycle()

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetBackgroundColor = appDark,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContent = {
            HomeSheetContent(
                state = viewModel.state,
                theme = theme,
                onSaveQuery = { query ->
                    viewModel.dispatch(HomeScreenEvent.SearchQueryChip.Save(query))
                    coroutine.launch { modalBottomSheetState.hide() }
                },
                onThemeSelected = { selectedTheme ->
                    setTheme(selectedTheme)
                    coroutine.launch { modalBottomSheetState.hide() }
                },
            )
        },
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                if (
                    viewModel.state.isSaveQueryDialogVisible.not() &&
                        viewModel.state.isThemeSelectionDialogVisible.not()
                ) {
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
                theme = theme,
                savedSearchQuery = viewModel.savedSearchQuery.collectAsStateWithLifecycle().value,
            ) { image ->
                navigateToDetailScreen(image.id)
            }
        }
    }
}

@Composable
private fun HomeSheetContent(
    state: HomeScreenState,
    theme: Theme,
    onSaveQuery: (String) -> Unit,
    onThemeSelected: (Theme) -> Unit,
) {
    if (state.isSaveQueryDialogVisible) {
        SaveNewChip(isDialogVisible = state.isSaveQueryDialogVisible, onSave = onSaveQuery)
    } else if (state.isThemeSelectionDialogVisible) {
        ThemeSelectionSheet(currentTheme = theme, onThemeSelected = onThemeSelected)
    } else {
        Spacer(modifier = Modifier.size(1.dp))
    }
}
