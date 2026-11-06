package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.model.AnimationSpeed
import com.example.data.model.FolderItem
import com.example.data.model.IconShape
import com.example.data.model.LauncherPreferences
import com.example.data.model.PageTransitionStyle
import com.example.data.model.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "holy_home_preferences")

class PreferencesRepository(private val context: Context) {

    private object Keys {
        val ICON_SIZE = intPreferencesKey("icon_size_dp")
        val COLUMNS = intPreferencesKey("columns")
        val ROWS = intPreferencesKey("rows")
        val SHOW_APP_LABELS = booleanPreferencesKey("show_app_labels")
        val LABEL_TEXT_SIZE = intPreferencesKey("label_text_size_sp")
        val SHOW_PAGE_NUMBERS = booleanPreferencesKey("show_page_numbers")
        val SHOW_VERSE_HEADER = booleanPreferencesKey("show_verse_header")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val ANIMATION_SPEED = stringPreferencesKey("animation_speed")
        val PAGE_TRANSITION = stringPreferencesKey("page_transition")
        val ICON_SHAPE = stringPreferencesKey("icon_shape")
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val FIRST_LAUNCH_COMPLETED = booleanPreferencesKey("first_launch_completed")
        val CUSTOM_ORDER = stringPreferencesKey("custom_order")
        val FOLDERS_JSON = stringPreferencesKey("folders_json")
        val APP_STATS_JSON = stringPreferencesKey("app_stats_json")
    }

    val preferencesFlow: Flow<LauncherPreferences> = context.dataStore.data.map { prefs ->
        LauncherPreferences(
            iconSizeDp = prefs[Keys.ICON_SIZE] ?: 56,
            columns = (prefs[Keys.COLUMNS] ?: 4).coerceIn(3, 5),
            rows = (prefs[Keys.ROWS] ?: 5).coerceIn(4, 7),
            showAppLabels = prefs[Keys.SHOW_APP_LABELS] ?: true,
            labelTextSizeSp = (prefs[Keys.LABEL_TEXT_SIZE] ?: 12).coerceIn(10, 16),
            showPageNumbers = prefs[Keys.SHOW_PAGE_NUMBERS] ?: true,
            showVerseHeader = prefs[Keys.SHOW_VERSE_HEADER] ?: true,
            themeModeName = prefs[Keys.THEME_MODE] ?: "MIDNIGHT_BIBLE",
            animationSpeed = try {
                AnimationSpeed.valueOf(prefs[Keys.ANIMATION_SPEED] ?: "NORMAL")
            } catch (e: Exception) {
                AnimationSpeed.NORMAL
            },
            pageTransitionStyle = try {
                PageTransitionStyle.valueOf(prefs[Keys.PAGE_TRANSITION] ?: "PAGE_CURL")
            } catch (e: Exception) {
                PageTransitionStyle.PAGE_CURL
            },
            iconShape = try {
                IconShape.valueOf(prefs[Keys.ICON_SHAPE] ?: "SQUIRCLE")
            } catch (e: Exception) {
                IconShape.SQUIRCLE
            },
            sortOrder = try {
                SortOrder.valueOf(prefs[Keys.SORT_ORDER] ?: "ALPHA_AZ")
            } catch (e: Exception) {
                SortOrder.ALPHA_AZ
            },
            isFirstLaunchCompleted = prefs[Keys.FIRST_LAUNCH_COMPLETED] ?: false,
            customOrder = (prefs[Keys.CUSTOM_ORDER] ?: "")
                .split(",")
                .filter { it.isNotBlank() }
        )
    }

    suspend fun updateIconSize(sizeDp: Int) {
        context.dataStore.edit { it[Keys.ICON_SIZE] = sizeDp.coerceIn(44, 76) }
    }

    suspend fun updateColumns(cols: Int) {
        context.dataStore.edit { it[Keys.COLUMNS] = cols.coerceIn(3, 5) }
    }

    suspend fun updateRows(rows: Int) {
        context.dataStore.edit { it[Keys.ROWS] = rows.coerceIn(4, 7) }
    }

    suspend fun updateShowAppLabels(show: Boolean) {
        context.dataStore.edit { it[Keys.SHOW_APP_LABELS] = show }
    }

    suspend fun updateLabelTextSize(sizeSp: Int) {
        context.dataStore.edit { it[Keys.LABEL_TEXT_SIZE] = sizeSp.coerceIn(10, 16) }
    }

    suspend fun updateShowPageNumbers(show: Boolean) {
        context.dataStore.edit { it[Keys.SHOW_PAGE_NUMBERS] = show }
    }

    suspend fun updateShowVerseHeader(show: Boolean) {
        context.dataStore.edit { it[Keys.SHOW_VERSE_HEADER] = show }
    }

    suspend fun updateThemeMode(mode: String) {
        context.dataStore.edit { it[Keys.THEME_MODE] = mode }
    }

    suspend fun updateAnimationSpeed(speed: AnimationSpeed) {
        context.dataStore.edit { it[Keys.ANIMATION_SPEED] = speed.name }
    }

    suspend fun updatePageTransition(style: PageTransitionStyle) {
        context.dataStore.edit { it[Keys.PAGE_TRANSITION] = style.name }
    }

    suspend fun updateIconShape(shape: IconShape) {
        context.dataStore.edit { it[Keys.ICON_SHAPE] = shape.name }
    }

    suspend fun updateSortOrder(order: SortOrder) {
        context.dataStore.edit { it[Keys.SORT_ORDER] = order.name }
    }

    suspend fun setFirstLaunchCompleted(completed: Boolean) {
        context.dataStore.edit { it[Keys.FIRST_LAUNCH_COMPLETED] = completed }
    }

    suspend fun saveCustomOrder(packageNames: List<String>) {
        context.dataStore.edit { it[Keys.CUSTOM_ORDER] = packageNames.joinToString(",") }
    }

    // Folders
    val foldersFlow: Flow<List<FolderItem>> = context.dataStore.data.map { prefs ->
        val jsonStr = prefs[Keys.FOLDERS_JSON] ?: "[]"
        parseFolders(jsonStr)
    }

    suspend fun saveFolders(folders: List<FolderItem>) {
        val array = JSONArray()
        for (f in folders) {
            val obj = JSONObject()
            obj.put("id", f.id)
            obj.put("name", f.name)
            obj.put("pageIndex", f.pageIndex)
            val appsArray = JSONArray()
            f.appPackageNames.forEach { appsArray.put(it) }
            obj.put("apps", appsArray)
            array.put(obj)
        }
        context.dataStore.edit { it[Keys.FOLDERS_JSON] = array.toString() }
    }

    private fun parseFolders(jsonStr: String): List<FolderItem> {
        val list = mutableListOf<FolderItem>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val apps = mutableListOf<String>()
                val appsArray = obj.optJSONArray("apps")
                if (appsArray != null) {
                    for (j in 0 until appsArray.length()) {
                        apps.add(appsArray.getString(j))
                    }
                }
                list.add(
                    FolderItem(
                        id = obj.optString("id", System.currentTimeMillis().toString()),
                        name = obj.optString("name", "Lahatahiry"),
                        appPackageNames = apps,
                        pageIndex = obj.optInt("pageIndex", 0)
                    )
                )
            }
        } catch (_: Exception) {}
        return list
    }

    // App Launch stats (count + last timestamp)
    val appStatsFlow: Flow<Map<String, Pair<Int, Long>>> = context.dataStore.data.map { prefs ->
        val jsonStr = prefs[Keys.APP_STATS_JSON] ?: "{}"
        val map = mutableMapOf<String, Pair<Int, Long>>()
        try {
            val obj = JSONObject(jsonStr)
            val keys = obj.keys()
            while (keys.hasNext()) {
                val pkg = keys.next()
                val stat = obj.getJSONObject(pkg)
                val count = stat.optInt("c", 0)
                val time = stat.optLong("t", 0L)
                map[pkg] = Pair(count, time)
            }
        } catch (_: Exception) {}
        map
    }

    suspend fun recordAppLaunch(packageName: String) {
        context.dataStore.edit { prefs ->
            val jsonStr = prefs[Keys.APP_STATS_JSON] ?: "{}"
            val obj = try { JSONObject(jsonStr) } catch (_: Exception) { JSONObject() }
            val current = obj.optJSONObject(packageName)
            val newCount = (current?.optInt("c", 0) ?: 0) + 1
            val newTime = System.currentTimeMillis()
            val newStat = JSONObject().apply {
                put("c", newCount)
                put("t", newTime)
            }
            obj.put(packageName, newStat)
            prefs[Keys.APP_STATS_JSON] = obj.toString()
        }
    }
}
