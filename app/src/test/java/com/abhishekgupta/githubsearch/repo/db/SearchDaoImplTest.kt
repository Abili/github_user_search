package com.abhishekgupta.githubsearch.repo.db

import com.abhishekgupta.githubsearch.model.Follower
import com.abhishekgupta.githubsearch.model.Following
import com.abhishekgupta.githubsearch.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchDaoImplTest {

    private val dao = mockk<SearchDbDao>(relaxed = true)

    private val userName = "abhishekgupta311287"

    private val testUser = User(
        1,
        userName,
        "Abhishek Gupta",
        "I am software engineer",
        0,
        10,
        10,
        10,
        "avatar_url"
    )

    private val testFollowerList = listOf(
        Follower(userName, 2, "ab", "url"),
        Follower("abhishekgupta311287", 3, "abs", "url")
    )

    private val testFollowingList = listOf(
        Following(userName, 4, "abi", "url"),
        Following(userName, 5, "rub", "url")
    )

    @Test
    fun `test user`() = runBlockingTest {
        val searchDao: SearchDao = SearchDaoImpl(dao)

        coEvery { dao.getUser(userName) }.returns(testUser)

        val user = searchDao.getUser(userName)

        assertEquals(userName, user?.login)
        assertEquals(10, user?.followersCount)

    }

    @Test
    fun `test followers`() = runBlockingTest {
        val searchDao: SearchDao = SearchDaoImpl(dao)

        coEvery { dao.getUserFollowers(userName, 10, 0) }.returns(emptyList())

        var followers = searchDao.getUserFollowers(userName, 10, 0)

        assertEquals(0, followers.size)

        coEvery { dao.getUserFollowers(userName, 10, 0) }.returns(testFollowerList)

        followers = searchDao.getUserFollowers(userName, 10, 0)

        assertEquals(2, followers.size)
    }

    @Test
    fun `test user following`() = runBlockingTest {

        val searchDao: SearchDao = SearchDaoImpl(dao)

        coEvery { dao.getUserFollowing(userName, 10, 0) }.returns(emptyList())

        var following = searchDao.getUserFollowing(userName, 10, 0)

        assertEquals(0, following.size)

        coEvery { dao.getUserFollowing(userName, 10, 0) }.returns(testFollowingList)

        following = searchDao.getUserFollowing(userName, 10, 0)

        assertEquals(2, following.size)
    }

    @Test
    fun `test user insert in db`() = runBlockingTest {
        val searchDao: SearchDao = SearchDaoImpl(dao)

        searchDao.insertUser(testUser)

        coVerify { dao.insertUser(testUser) }
    }

    @Test
    fun `test followers list insert in db`() = runBlockingTest {
        val searchDao: SearchDao = SearchDaoImpl(dao)

        searchDao.insertUserFollowers(userName, testFollowerList)

        coVerify { dao.insertUserFollowers(userName, testFollowerList) }
    }

    @Test
    fun `test following list insert in db`() = runBlockingTest {
        val searchDao: SearchDao = SearchDaoImpl(dao)

        searchDao.insertUserFollowing(userName, testFollowingList)

        coVerify { dao.insertUserFollowing(userName, testFollowingList) }
    }
}