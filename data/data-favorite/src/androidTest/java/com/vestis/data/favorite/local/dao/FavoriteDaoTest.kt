package com.vestis.data.favorite.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.vestis.data.favorite.local.db.FavoriteRoomDatabase
import com.vestis.data.favorite.local.entity.FavoriteEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(value = AndroidJUnit4::class)
class FavoriteDaoTest {

    private lateinit var sut: FavoriteDao

    private lateinit var database: FavoriteRoomDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            klass = FavoriteRoomDatabase::class.java,
        ).allowMainThreadQueries()
            .build()

        sut = database.favoriteDAO()
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
    fun givenFavoriteWhenInsertThenShouldBePresentInList() = runTest {
        // Given
        val favorite = FavoriteEntity(
            productId = 1
        )

        sut.observeAll().test {
            assert(awaitItem().isEmpty())

            // When
            sut.insert(favorite)


            val result = awaitItem()

            // Then
            assert(result.size == 1)
            assert(result[0].productId == 1)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenExistingFavoriteWhenDeleteThenShouldEmitEmptyList() = runTest {
        // Given
        val productId = 5

        sut.insert(FavoriteEntity(productId = productId))

        sut.observeAll().test {
            assert(awaitItem().size == 1)

            // When
            sut.delete(productId)

            // Then
            assert(awaitItem().isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenFavoriteWhenExistsThenShouldReturnTrue() = runTest {
        // Given
        val productId = 10

        sut.insert(FavoriteEntity(productId = productId))

        // When
        val exists = sut.exists(productId)

        // Then
        assert(exists)
    }

    @Test
    fun givenNonExistingFavoriteWhenExistsThenShouldReturnFalse() = runTest {
        // When
        val exists = sut.exists(productId = 99)

        // Then
        assert(!exists)
    }

    @After
    fun tearDown() {
        database.close()
    }
}