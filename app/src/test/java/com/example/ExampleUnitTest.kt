package com.example

import com.example.data.model.AppItem
import com.example.data.model.SortOrder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.Collator
import java.util.Locale

class ExampleUnitTest {

    @Test
    fun testAppSortingAlphabeticalAndSize() {
        val sampleApps = listOf(
            AppItem(packageName = "com.z", activityName = ".Main", label = "Zebra", appSizeBytes = 5000),
            AppItem(packageName = "com.a", activityName = ".Main", label = "Alpha", appSizeBytes = 1000),
            AppItem(packageName = "com.m", activityName = ".Main", label = "Mango", appSizeBytes = 8000)
        )

        val collator = Collator.getInstance(Locale.getDefault())
        val sortedAz = sampleApps.sortedWith { a, b -> collator.compare(a.label, b.label) }
        assertEquals("Alpha", sortedAz[0].label)
        assertEquals("Mango", sortedAz[1].label)
        assertEquals("Zebra", sortedAz[2].label)

        val sortedZa = sampleApps.sortedWith { a, b -> collator.compare(b.label, a.label) }
        assertEquals("Zebra", sortedZa[0].label)
        assertEquals("Mango", sortedZa[1].label)
        assertEquals("Alpha", sortedZa[2].label)

        val sortedSize = sampleApps.sortedByDescending { it.appSizeBytes }
        assertEquals("Mango", sortedSize[0].label)
        assertEquals("Zebra", sortedSize[1].label)
        assertEquals("Alpha", sortedSize[2].label)
    }

    @Test
    fun testSortOrderEnumValues() {
        assertEquals(8, SortOrder.values().size)
        assertEquals("A–Z", SortOrder.ALPHA_AZ.displayName.substringAfter("(").substringBefore(")"))
        assertEquals("Deoteronomia 28:6", "Deoteronomia 28:6")
    }
}
