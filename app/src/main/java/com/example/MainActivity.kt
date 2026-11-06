package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.LauncherViewModel
import com.example.ui.components.AboutDialog
import com.example.ui.components.AppDetailDialog
import com.example.ui.components.AppGridPage
import com.example.ui.components.AppSearchSheet
import com.example.ui.components.BibleCoverScreen
import com.example.ui.components.BiblePageTurnLayout
import com.example.ui.components.CreateFolderDialog
import com.example.ui.components.FolderContentsDialog
import com.example.ui.components.MoveToPageDialog
import com.example.ui.components.OnboardingDialog
import com.example.ui.components.SettingsScreen
import com.example.ui.theme.BibleTheme
import com.example.ui.theme.HolyHomeTheme

class MainActivity : ComponentActivity() {

    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            val bibleTheme = when (uiState.preferences.themeModeName) {
                "PARCHMENT_IVORY" -> BibleTheme.PARCHMENT_IVORY
                "ROYAL_BURGUNDY" -> BibleTheme.ROYAL_BURGUNDY
                else -> BibleTheme.MIDNIGHT_BIBLE
            }

            HolyHomeTheme(themeMode = bibleTheme) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Back navigation handling for launcher
                    BackHandler(enabled = true) {
                        when {
                            uiState.showAbout -> viewModel.showAbout(false)
                            uiState.showSettings -> viewModel.showSettings(false)
                            uiState.isSearchActive -> viewModel.setSearchActive(false)
                            uiState.selectedAppForDetail != null -> viewModel.selectAppForDetail(null)
                            uiState.selectedFolder != null -> viewModel.selectFolder(null)
                            uiState.showCreateFolderDialog -> viewModel.closeCreateFolderDialog()
                            uiState.showMoveToPageDialog != null -> viewModel.showMoveToPageDialog(null)
                            uiState.isCoverOpen && uiState.currentPageIndex > 0 -> viewModel.previousPage()
                            uiState.isCoverOpen && uiState.currentPageIndex == 0 -> viewModel.closeBibleCover()
                            // If cover is closed on home screen, do nothing (keep home screen)
                        }
                    }

                    // Main Launcher Screens
                    if (!uiState.isCoverOpen) {
                        // Bible Cover Screen
                        BibleCoverScreen(
                            onOpenBible = { viewModel.openBibleCover() },
                            onOpenSearch = { viewModel.setSearchActive(true) },
                            onOpenSettings = { viewModel.showSettings(true) },
                            onOpenAbout = { viewModel.showAbout(true) }
                        )
                    } else {
                        // Bible Pages with realistic page turn animation
                        BiblePageTurnLayout(
                            currentPage = uiState.currentPageIndex,
                            totalPages = uiState.totalPages,
                            showVerseHeader = uiState.preferences.showVerseHeader,
                            showPageNumbers = uiState.preferences.showPageNumbers,
                            pageTransitionStyle = uiState.preferences.pageTransitionStyle,
                            animationSpeed = uiState.preferences.animationSpeed,
                            onNextPage = { viewModel.nextPage() },
                            onPreviousPage = { viewModel.previousPage() },
                            onCloseToCover = { viewModel.closeBibleCover() },
                            onOpenSearch = { viewModel.setSearchActive(true) },
                            onOpenSettings = { viewModel.showSettings(true) }
                        ) { page ->
                            AppGridPage(
                                pageIndex = page,
                                allApps = uiState.allApps,
                                displayApps = uiState.displayApps,
                                folders = uiState.folders,
                                preferences = uiState.preferences,
                                isLoading = uiState.isLoading,
                                onAppClick = { app -> viewModel.launchApp(app) },
                                onAppLongClick = { app -> viewModel.selectAppForDetail(app) },
                                onFolderClick = { folder -> viewModel.selectFolder(folder) }
                            )
                        }
                    }

                    // Full-screen Search Overlay
                    if (uiState.isSearchActive) {
                        AppSearchSheet(
                            query = uiState.searchQuery,
                            searchResults = uiState.searchResults,
                            onQueryChange = { viewModel.setSearchQuery(it) },
                            onAppClick = { app ->
                                viewModel.launchApp(app)
                                viewModel.setSearchActive(false)
                            },
                            onClose = { viewModel.setSearchActive(false) }
                        )
                    }

                    // Settings Screen
                    if (uiState.showSettings) {
                        SettingsScreen(
                            preferences = uiState.preferences,
                            onUpdateIconSize = { viewModel.updateIconSize(it) },
                            onUpdateColumns = { viewModel.updateColumns(it) },
                            onUpdateRows = { viewModel.updateRows(it) },
                            onUpdateShowLabels = { viewModel.updateShowAppLabels(it) },
                            onUpdateLabelSize = { viewModel.updateLabelTextSize(it) },
                            onUpdateShowPageNumbers = { viewModel.updateShowPageNumbers(it) },
                            onUpdateShowVerseHeader = { viewModel.updateShowVerseHeader(it) },
                            onUpdateTheme = { viewModel.updateThemeMode(it) },
                            onUpdateAnimationSpeed = { viewModel.updateAnimationSpeed(it) },
                            onUpdatePageTransition = { viewModel.updatePageTransition(it) },
                            onUpdateIconShape = { viewModel.updateIconShape(it) },
                            onUpdateSortOrder = { viewModel.updateSortOrder(it) },
                            onOpenDefaultLauncherSettings = { viewModel.openDefaultLauncherSettings() },
                            onCreateFolderClick = { viewModel.openCreateFolderDialog() },
                            onOpenAbout = { viewModel.showAbout(true) },
                            onClose = { viewModel.showSettings(false) }
                        )
                    }

                    // App Detail / Action Dialog
                    uiState.selectedAppForDetail?.let { selectedApp ->
                        AppDetailDialog(
                            app = selectedApp,
                            formattedSize = viewModel.appRepository.formatFileSize(selectedApp.appSizeBytes),
                            onLaunch = {
                                viewModel.launchApp(selectedApp)
                                viewModel.selectAppForDetail(null)
                            },
                            onMoveToPage = {
                                viewModel.showMoveToPageDialog(selectedApp)
                                viewModel.selectAppForDetail(null)
                            },
                            onAddToFolder = {
                                viewModel.openCreateFolderDialog(selectedApp)
                                viewModel.selectAppForDetail(null)
                            },
                            onOpenSystemInfo = {
                                viewModel.openAppInfo(selectedApp.packageName)
                                viewModel.selectAppForDetail(null)
                            },
                            onDismiss = { viewModel.selectAppForDetail(null) }
                        )
                    }

                    // Folder Contents Dialog
                    uiState.selectedFolder?.let { selectedFolder ->
                        FolderContentsDialog(
                            folder = selectedFolder,
                            allApps = uiState.allApps,
                            onLaunchApp = { app ->
                                viewModel.launchApp(app)
                                viewModel.selectFolder(null)
                            },
                            onRemoveFromFolder = { pkg ->
                                viewModel.removeAppFromFolder(selectedFolder.id, pkg)
                            },
                            onRenameFolder = { newName ->
                                viewModel.renameFolder(selectedFolder.id, newName)
                            },
                            onDeleteFolder = {
                                viewModel.deleteFolder(selectedFolder.id)
                            },
                            onDismiss = { viewModel.selectFolder(null) }
                        )
                    }

                    // Move App to Page Dialog
                    uiState.showMoveToPageDialog?.let { targetApp ->
                        MoveToPageDialog(
                            app = targetApp,
                            totalPages = uiState.totalPages,
                            currentPage = uiState.currentPageIndex,
                            folders = uiState.folders,
                            onSelectPage = { pageIdx ->
                                viewModel.moveAppToPage(targetApp, pageIdx)
                            },
                            onSelectFolder = { folderId ->
                                viewModel.addAppToFolder(folderId, targetApp.packageName)
                                viewModel.showMoveToPageDialog(null)
                            },
                            onDismiss = { viewModel.showMoveToPageDialog(null) }
                        )
                    }

                    // Create Folder Dialog
                    if (uiState.showCreateFolderDialog) {
                        CreateFolderDialog(
                            initialTargetApp = uiState.targetAppForFolder,
                            onCreate = { name -> viewModel.createFolder(name) },
                            onDismiss = { viewModel.closeCreateFolderDialog() }
                        )
                    }

                    // About Dialog
                    if (uiState.showAbout) {
                        AboutDialog(
                            onDismiss = { viewModel.showAbout(false) }
                        )
                    }

                    // First Launch Onboarding Dialog
                    if (uiState.showOnboarding) {
                        OnboardingDialog(
                            onSetAsDefaultLauncher = { viewModel.openDefaultLauncherSettings() },
                            onComplete = { viewModel.completeOnboarding() }
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh installed apps in case an app was installed or uninstalled
        viewModel.loadApps(forceRefresh = true)
    }
}
