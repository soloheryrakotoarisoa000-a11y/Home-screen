package com.example.data

import android.app.role.RoleManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.example.data.model.AppItem
import com.example.data.model.SortOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.Collator
import java.util.Locale

class AppRepository(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository
) {

    private val packageManager: PackageManager = context.packageManager
    private val cachedApps = mutableListOf<AppItem>()

    suspend fun getInstalledApps(forceRefresh: Boolean = false): List<AppItem> = withContext(Dispatchers.IO) {
        if (!forceRefresh && cachedApps.isNotEmpty()) {
            return@withContext cachedApps.toList()
        }

        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val resolveInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(
                mainIntent,
                PackageManager.ResolveInfoFlags.of(0L)
            )
        } else {
            @Suppress("DEPRECATION")
            packageManager.queryIntentActivities(mainIntent, 0)
        }

        val appList = mutableListOf<AppItem>()
        val selfPackage = context.packageName

        for (resolveInfo in resolveInfos) {
            val pkg = resolveInfo.activityInfo.packageName
            // Skip the launcher itself in the grid to avoid recursion
            if (pkg == selfPackage) continue

            val activityName = resolveInfo.activityInfo.name
            val label = try {
                resolveInfo.loadLabel(packageManager).toString().trim()
            } catch (_: Exception) {
                pkg
            }

            val icon = try {
                resolveInfo.loadIcon(packageManager)
            } catch (_: Exception) {
                null
            }

            var installTime = 0L
            var updateTime = 0L
            var sizeBytes = 0L

            try {
                val pkgInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    packageManager.getPackageInfo(pkg, PackageManager.PackageInfoFlags.of(0L))
                } else {
                    @Suppress("DEPRECATION")
                    packageManager.getPackageInfo(pkg, 0)
                }
                installTime = pkgInfo.firstInstallTime
                updateTime = pkgInfo.lastUpdateTime

                val sourceDir = pkgInfo.applicationInfo?.sourceDir
                if (!sourceDir.isNullOrBlank()) {
                    val file = File(sourceDir)
                    if (file.exists()) {
                        sizeBytes = file.length()
                    }
                }
            } catch (_: Exception) {}

            appList.add(
                AppItem(
                    packageName = pkg,
                    activityName = activityName,
                    label = if (label.isNotBlank()) label else pkg,
                    installTime = installTime,
                    updateTime = updateTime,
                    appSizeBytes = sizeBytes,
                    icon = icon
                )
            )
        }

        cachedApps.clear()
        cachedApps.addAll(appList)
        appList
    }

    fun sortApps(
        apps: List<AppItem>,
        sortOrder: SortOrder,
        appStats: Map<String, Pair<Int, Long>>,
        customOrder: List<String>
    ): List<AppItem> {
        val collator = Collator.getInstance(Locale.getDefault()).apply {
            strength = Collator.PRIMARY
        }

        return when (sortOrder) {
            SortOrder.ALPHA_AZ -> {
                apps.sortedWith { a, b -> collator.compare(a.label, b.label) }
            }
            SortOrder.ALPHA_ZA -> {
                apps.sortedWith { a, b -> collator.compare(b.label, a.label) }
            }
            SortOrder.SIZE_ASC -> {
                apps.sortedBy { it.appSizeBytes }
            }
            SortOrder.SIZE_DESC -> {
                apps.sortedByDescending { it.appSizeBytes }
            }
            SortOrder.INSTALL_NEWEST -> {
                apps.sortedByDescending { it.installTime }
            }
            SortOrder.INSTALL_OLDEST -> {
                apps.sortedBy { it.installTime }
            }
            SortOrder.RECENT_USED -> {
                apps.sortedWith(
                    compareByDescending<AppItem> { app ->
                        appStats[app.packageName]?.second ?: app.updateTime
                    }.thenByDescending { app ->
                        appStats[app.packageName]?.first ?: 0
                    }
                )
            }
            SortOrder.CUSTOM -> {
                if (customOrder.isEmpty()) {
                    apps.sortedWith { a, b -> collator.compare(a.label, b.label) }
                } else {
                    val orderMap = customOrder.withIndex().associate { it.value to it.index }
                    apps.sortedWith(
                        compareBy<AppItem> { orderMap[it.packageName] ?: Int.MAX_VALUE }
                            .thenBy { it.label }
                    )
                }
            }
        }
    }

    suspend fun launchApp(app: AppItem): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
                component = ComponentName(app.packageName, app.activityName)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            }
            context.startActivity(intent)
            preferencesRepository.recordAppLaunch(app.packageName)
            true
        } catch (_: Exception) {
            // Fallback via getLaunchIntentForPackage
            try {
                val launchIntent = packageManager.getLaunchIntentForPackage(app.packageName)
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(launchIntent)
                    preferencesRepository.recordAppLaunch(app.packageName)
                    true
                } else {
                    false
                }
            } catch (_: Exception) {
                false
            }
        }
    }

    fun openAppInfo(packageName: String) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {}
    }

    fun openDefaultHomeSettings() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val roleManager = context.getSystemService(RoleManager::class.java)
                if (roleManager != null && roleManager.isRoleAvailable(RoleManager.ROLE_HOME)) {
                    val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                    return
                }
            }
            val intent = Intent(Settings.ACTION_HOME_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            try {
                val fallbackIntent = Intent(Settings.ACTION_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(fallbackIntent)
            } catch (_: Exception) {}
        }
    }

    fun formatFileSize(bytes: Long): String {
        if (bytes <= 0) return "Tsy fantatra"
        val kb = bytes / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0
        return when {
            gb >= 1.0 -> String.format(Locale.US, "%.1f GB", gb)
            mb >= 1.0 -> String.format(Locale.US, "%.1f MB", mb)
            kb >= 1.0 -> String.format(Locale.US, "%.1f KB", kb)
            else -> "$bytes B"
        }
    }
}
