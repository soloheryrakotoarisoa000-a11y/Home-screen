package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppRepository
import com.example.data.PreferencesRepository
import com.example.data.model.AnimationSpeed
import com.example.data.model.AppItem
import com.example.data.model.FolderItem
import com.example.data.model.IconShape
import com.example.data.model.LauncherPreferences
import com.example.data.model.PageTransitionStyle
import com.example.data.model.SortOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

data class LauncherUiState(
    val isCoverOpen: Boolean = false,
    val allApps: List<AppItem> = emptyList(),
    val displayApps: List<AppItem> = emptyList(),
    val folders: List<FolderItem> = emptyList(),
    val preferences: LauncherPreferences = LauncherPreferences(),
    val currentPageIndex: Int = 0,
    val totalPages: Int = 1,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val searchResults: List<AppItem> = emptyList(),
    val selectedAppForDetail: AppItem? = null,
    val selectedFolder: FolderItem? = null,
    val showSettings: Boolean = false,
    val showAbout: Boolean = false,
    val showOnboarding: Boolean = false,
    val showCreateFolderDialog: Boolean = false,
    val targetAppForFolder: AppItem? = null,
    val showMoveToPageDialog: AppItem? = null,
    val isLoading: Boolean = true
)

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesRepository = PreferencesRepository(application)
    val appRepository = AppRepository(application, preferencesRepository)

    private val _isCoverOpen = MutableStateFlow(false)
    private val _currentPageIndex = MutableStateFlow(0)
    private val _searchQuery = MutableStateFlow("")
    private val _isSearchActive = MutableStateFlow(false)
    private val _selectedAppForDetail = MutableStateFlow<AppItem?>(null)
    private val _selectedFolder = MutableStateFlow<FolderItem?>(null)
    private val _showSettings = MutableStateFlow(false)
    private val _showAbout = MutableStateFlow(false)
    private val _showOnboarding = MutableStateFlow(false)
    private val _showCreateFolderDialog = MutableStateFlow(false)
    private val _targetAppForFolder = MutableStateFlow<AppItem?>(null)
    private val _showMoveToPageDialog = MutableStateFlow<AppItem?>(null)
    private val _installedApps = MutableStateFlow<List<AppItem>>(emptyList())
    private val _isLoading = MutableStateFlow(true)

    val uiState: StateFlow<LauncherUiState> = combine(
        _installedApps,
        preferencesRepository.preferencesFlow,
        preferencesRepository.foldersFlow,
        preferencesRepository.appStatsFlow,
        _isCoverOpen,
        _currentPageIndex,
        _searchQuery,
        _isSearchActive,
        _selectedAppForDetail,
        _selectedFolder,
        _showSettings,
        _showAbout,
        _showOnboarding,
        _showCreateFolderDialog,
        _targetAppForFolder,
        _showMoveToPageDialog,
        _isLoading
    ) { args: Array<Any?> ->
        @Suppress("UNCHECKED_CAST")
        val apps = args[0] as List<AppItem>
        val prefs = args[1] as LauncherPreferences
        @Suppress("UNCHECKED_CAST")
        val folders = args[2] as List<FolderItem>
        @Suppress("UNCHECKED_CAST")
        val stats = args[3] as Map<String, Pair<Int, Long>>
        val isCoverOpen = args[4] as Boolean
        val pageIndex = args[5] as Int
        val query = args[6] as String
        val isSearchActive = args[7] as Boolean
        val selectedApp = args[8] as AppItem?
        val selectedFolder = args[9] as FolderItem?
        val showSettings = args[10] as Boolean
        val showAbout = args[11] as Boolean
        val showOnboarding = args[12] as Boolean
        val showCreateFolder = args[13] as Boolean
        val targetAppForFolder = args[14] as AppItem?
        val showMoveDialog = args[15] as AppItem?
        val isLoading = args[16] as Boolean

        val sortedApps = appRepository.sortApps(apps, prefs.sortOrder, stats, prefs.customOrder)

        // Calculate pages based on grid capacity
        val capacity = (prefs.columns * prefs.rows).coerceAtLeast(1)
        val calculatedTotalPages = maxOf(1, (sortedApps.size + capacity - 1) / capacity)
        val clampedPageIndex = pageIndex.coerceIn(0, calculatedTotalPages - 1)

        val searchResults = if (query.isBlank()) {
            emptyList()
        } else {
            sortedApps.filter {
                it.label.contains(query, ignoreCase = true) ||
                it.packageName.contains(query, ignoreCase = true)
            }
        }

        LauncherUiState(
            isCoverOpen = isCoverOpen,
            allApps = apps,
            displayApps = sortedApps,
            folders = folders,
            preferences = prefs,
            currentPageIndex = clampedPageIndex,
            totalPages = calculatedTotalPages,
            searchQuery = query,
            isSearchActive = isSearchActive,
            searchResults = searchResults,
            selectedAppForDetail = selectedApp,
            selectedFolder = selectedFolder,
            showSettings = showSettings,
            showAbout = showAbout,
            showOnboarding = showOnboarding,
            showCreateFolderDialog = showCreateFolder,
            targetAppForFolder = targetAppForFolder,
            showMoveToPageDialog = showMoveDialog,
            isLoading = isLoading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LauncherUiState()
    )

    init {
        loadApps()
    }

    fun loadApps(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            val apps = appRepository.getInstalledApps(forceRefresh)
            _installedApps.value = apps
            _isLoading.value = false

            // Check if first launch completed
            preferencesRepository.preferencesFlow.collect { prefs ->
                if (!prefs.isFirstLaunchCompleted) {
                    _showOnboarding.value = true
                }
            }
        }
    }

    fun openBibleCover() {
        _isCoverOpen.value = true
    }

    fun closeBibleCover() {
        _isCoverOpen.value = false
    }

    fun toggleBibleCover() {
        _isCoverOpen.value = !_isCoverOpen.value
    }

    fun goToPage(page: Int) {
        val total = uiState.value.totalPages
        _currentPageIndex.value = page.coerceIn(0, maxOf(0, total - 1))
    }

    fun nextPage() {
        val current = _currentPageIndex.value
        val total = uiState.value.totalPages
        if (current < total - 1) {
            _currentPageIndex.value = current + 1
        }
    }

    fun previousPage() {
        val current = _currentPageIndex.value
        if (current > 0) {
            _currentPageIndex.value = current - 1
        } else {
            // Turning backward from page 0 closes the Bible to the cover!
            _isCoverOpen.value = false
        }
    }

    fun launchApp(app: AppItem) {
        viewModelScope.launch {
            appRepository.launchApp(app)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSearchActive(active: Boolean) {
        _isSearchActive.value = active
        if (!active) {
            _searchQuery.value = ""
        }
    }

    fun selectAppForDetail(app: AppItem?) {
        _selectedAppForDetail.value = app
    }

    fun selectFolder(folder: FolderItem?) {
        _selectedFolder.value = folder
    }

    fun showSettings(show: Boolean) {
        _showSettings.value = show
    }

    fun showAbout(show: Boolean) {
        _showAbout.value = show
    }

    fun showOnboarding(show: Boolean) {
        _showOnboarding.value = show
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            preferencesRepository.setFirstLaunchCompleted(true)
            _showOnboarding.value = false
        }
    }

    fun openDefaultLauncherSettings() {
        appRepository.openDefaultHomeSettings()
    }

    fun openAppInfo(packageName: String) {
        appRepository.openAppInfo(packageName)
    }

    // Folder Operations
    fun openCreateFolderDialog(targetApp: AppItem? = null) {
        _targetAppForFolder.value = targetApp
        _showCreateFolderDialog.value = true
    }

    fun closeCreateFolderDialog() {
        _targetAppForFolder.value = null
        _showCreateFolderDialog.value = false
    }

    fun createFolder(name: String) {
        val targetApp = _targetAppForFolder.value
        val currentFolders = uiState.value.folders.toMutableList()
        val newFolder = FolderItem(
            id = UUID.randomUUID().toString(),
            name = if (name.isNotBlank()) name else "Lahatahiry",
            appPackageNames = if (targetApp != null) listOf(targetApp.packageName) else emptyList(),
            pageIndex = _currentPageIndex.value
        )
        currentFolders.add(newFolder)
        viewModelScope.launch {
            preferencesRepository.saveFolders(currentFolders)
            closeCreateFolderDialog()
        }
    }

    fun deleteFolder(folderId: String) {
        val currentFolders = uiState.value.folders.filter { it.id != folderId }
        viewModelScope.launch {
            preferencesRepository.saveFolders(currentFolders)
            if (_selectedFolder.value?.id == folderId) {
                _selectedFolder.value = null
            }
        }
    }

    fun renameFolder(folderId: String, newName: String) {
        val currentFolders = uiState.value.folders.map {
            if (it.id == folderId) it.copy(name = newName) else it
        }
        viewModelScope.launch {
            preferencesRepository.saveFolders(currentFolders)
            if (_selectedFolder.value?.id == folderId) {
                _selectedFolder.value = _selectedFolder.value?.copy(name = newName)
            }
        }
    }

    fun addAppToFolder(folderId: String, packageName: String) {
        val currentFolders = uiState.value.folders.map { folder ->
            if (folder.id == folderId && !folder.appPackageNames.contains(packageName)) {
                folder.copy(appPackageNames = folder.appPackageNames + packageName)
            } else {
                folder
            }
        }
        viewModelScope.launch {
            preferencesRepository.saveFolders(currentFolders)
            if (_selectedFolder.value?.id == folderId) {
                val updated = currentFolders.find { it.id == folderId }
                _selectedFolder.value = updated
            }
        }
    }

    fun removeAppFromFolder(folderId: String, packageName: String) {
        val currentFolders = uiState.value.folders.map { folder ->
            if (folder.id == folderId) {
                folder.copy(appPackageNames = folder.appPackageNames - packageName)
            } else {
                folder
            }
        }
        viewModelScope.launch {
            preferencesRepository.saveFolders(currentFolders)
            if (_selectedFolder.value?.id == folderId) {
                val updated = currentFolders.find { it.id == folderId }
                _selectedFolder.value = updated
            }
        }
    }

    // Page Move Operations
    fun showMoveToPageDialog(app: AppItem?) {
        _showMoveToPageDialog.value = app
    }

    fun moveAppToPage(app: AppItem, targetPage: Int) {
        val prefs = uiState.value.preferences
        val capacity = (prefs.columns * prefs.rows).coerceAtLeast(1)
        val sorted = uiState.value.displayApps.toMutableList()

        val currentIndex = sorted.indexOfFirst { it.packageName == app.packageName }
        if (currentIndex != -1) {
            sorted.removeAt(currentIndex)
            val insertIndex = (targetPage * capacity).coerceIn(0, sorted.size)
            sorted.add(insertIndex, app)
            val newCustomOrder = sorted.map { it.packageName }
            viewModelScope.launch {
                preferencesRepository.saveCustomOrder(newCustomOrder)
                preferencesRepository.updateSortOrder(SortOrder.CUSTOM)
                _showMoveToPageDialog.value = null
                _currentPageIndex.value = targetPage
            }
        }
    }

    // Settings Updates
    fun updateIconSize(sizeDp: Int) = viewModelScope.launch { preferencesRepository.updateIconSize(sizeDp) }
    fun updateColumns(cols: Int) = viewModelScope.launch { preferencesRepository.updateColumns(cols) }
    fun updateRows(rows: Int) = viewModelScope.launch { preferencesRepository.updateRows(rows) }
    fun updateShowAppLabels(show: Boolean) = viewModelScope.launch { preferencesRepository.updateShowAppLabels(show) }
    fun updateLabelTextSize(sizeSp: Int) = viewModelScope.launch { preferencesRepository.updateLabelTextSize(sizeSp) }
    fun updateShowPageNumbers(show: Boolean) = viewModelScope.launch { preferencesRepository.updateShowPageNumbers(show) }
    fun updateShowVerseHeader(show: Boolean) = viewModelScope.launch { preferencesRepository.updateShowVerseHeader(show) }
    fun updateThemeMode(mode: String) = viewModelScope.launch { preferencesRepository.updateThemeMode(mode) }
    fun updateAnimationSpeed(speed: AnimationSpeed) = viewModelScope.launch { preferencesRepository.updateAnimationSpeed(speed) }
    fun updatePageTransition(style: PageTransitionStyle) = viewModelScope.launch { preferencesRepository.updatePageTransition(style) }
    fun updateIconShape(shape: IconShape) = viewModelScope.launch { preferencesRepository.updateIconShape(shape) }
    fun updateSortOrder(order: SortOrder) = viewModelScope.launch { preferencesRepository.updateSortOrder(order) }
}
