package com.vestis.data.products.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.vestis.core.data.local.room.AppRoomDatabase
import com.vestis.core.data.local.room.dao.ProductDao
import com.vestis.core.data.local.room.entity.ProductEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(value = AndroidJUnit4::class)
class ProductDaoTest {

    private lateinit var sut: ProductDao

    private lateinit var database: AppRoomDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            klass = AppRoomDatabase::class.java,
        ).allowMainThreadQueries()
            .build()

        sut = database.productDAO()
    }

    @Test
    fun givenEmptyDatabaseWhenObserveAllThenShouldEmitEmptyList() = runTest {
        // Given
        sut.observeAll().test {
            assert(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenProductsWhenUpsertAllThenShouldEmitInsertedProducts() = runTest {
        // Given
        val products = listOf(
            ProductEntity(
                id = 1,
                title = "Shirt",
                price = 22.3f,
                description = "",
                imageUrl = "",
                category = "men's clothing"
            ),
            ProductEntity(
                id = 2,
                title = "Jacket",
                price = 55.9f,
                description = "",
                imageUrl = "",
                category = "women's clothing"
            ),
        )

        sut.observeAll().test {
            assert(awaitItem().isEmpty())

            // When
            sut.upsertAll(products)

            val result = awaitItem()

            // Then
            assert(result.size == 2)
            assert(result[0].title == "Shirt")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenExistingProductWhenUpsertSameIdThenShouldUpdateProduct() = runTest {
        // Given
        val original = ProductEntity(
            id = 1,
            title = "Shirt",
            price = 22.3f,
            description = "",
            imageUrl = "",
            category = "men's clothing"
        )

        val updated = original.copy(title = "Updated Shirt")

        sut.observeAll().test {
            assert(awaitItem().isEmpty())

            // When
            sut.upsertAll(listOf(original))
            assert(awaitItem().size == 1)
            sut.upsertAll(listOf(updated))

            // Then
            val result = awaitItem()

            assert(result.size == 1)
            assert(result[0].title == "Updated Shirt")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {
        database.close()
    }
}