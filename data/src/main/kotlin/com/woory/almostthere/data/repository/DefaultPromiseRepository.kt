package com.woory.almostthere.data.repository

import com.woory.almostthere.data.model.AddedUserHpModel
import com.woory.almostthere.data.model.GeoPointModel
import com.woory.almostthere.data.model.LocationSearchModel
import com.woory.almostthere.data.model.MagneticInfoModel
import com.woory.almostthere.data.model.PromiseAlarmModel
import com.woory.almostthere.data.model.PromiseDataModel
import com.woory.almostthere.data.model.PromiseHistoryModel
import com.woory.almostthere.data.model.PromiseModel
import com.woory.almostthere.data.model.UserLocationModel
import com.woory.almostthere.data.model.UserModel
import com.woory.almostthere.data.model.UserRankingModel
import com.woory.almostthere.data.source.DatabaseDataSource
import com.woory.almostthere.data.source.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultPromiseRepository @Inject constructor(
    private val databaseDataSource: DatabaseDataSource,
    private val networkDataSource: NetworkDataSource
) : PromiseRepository {

    override suspend fun setPromise(promiseDataModel: PromiseDataModel): Result<String> {
        val result = networkDataSource.setPromise(promiseDataModel)
            .onSuccess { code ->
                setPromiseAlarmByPromiseModel(PromiseModel(code, promiseDataModel))
            }
            .onFailure {
                return Result.failure(it)
            }
        return result
    }

    override suspend fun getPromiseAlarm(promiseCode: String): Result<PromiseAlarmModel> =
        databaseDataSource.getPromiseAlarmWhereCode(promiseCode)

    override suspend fun getAllPromiseAlarms(): Result<List<PromiseAlarmModel>> =
        databaseDataSource.getAll()

    override suspend fun setPromiseAlarmByPromiseModel(promiseModel: PromiseModel): Result<Unit> =
        databaseDataSource.setPromiseAlarmByPromiseModel(promiseModel)

    override suspend fun setPromiseAlarmByPromiseAlarmModel(promiseAlarmModel: PromiseAlarmModel): Result<Unit> =
        databaseDataSource.setPromiseAlarmByPromiseAlarmModel(promiseAlarmModel)

    override suspend fun getAddressByPoint(geoPointModel: GeoPointModel): Result<String> =
        networkDataSource.getAddressByPoint(geoPointModel)

    override suspend fun getPromiseByCode(promiseCode: String): Result<PromiseModel> =
        networkDataSource.getPromiseByCode(promiseCode)

    override suspend fun getPromiseByCodeAndListen(promiseCode: String): Flow<Result<PromiseModel>> =
        networkDataSource.getPromiseByCodeAndListen(promiseCode)

    override suspend fun setUserLocation(userLocationModel: UserLocationModel): Result<Unit> =
        networkDataSource.setUserLocation(userLocationModel)

    override suspend fun setUserHp(gameToken: String, userHpModel: AddedUserHpModel): Result<Unit> =
        networkDataSource.setUserHp(gameToken, userHpModel)

    override suspend fun getUserLocation(userId: String): Flow<Result<UserLocationModel>> =
        networkDataSource.getUserLocationById(userId)

    override suspend fun addPlayer(code: String, user: UserModel): Result<Unit> =
        networkDataSource.addPlayer(code, user)

    override suspend fun getSearchedLocationByKeyword(keyword: String): Result<List<LocationSearchModel>> =
        networkDataSource.searchLocationByKeyword(keyword)

    override suspend fun getJoinedPromiseList(): Result<List<PromiseAlarmModel>> =
        databaseDataSource.getAll()

    override suspend fun getMagneticInfoByCode(promiseCode: String): Result<MagneticInfoModel> =
        networkDataSource.getMagneticInfoByCode(promiseCode)

    override suspend fun getMagneticInfoByCodeAndListen(promiseCode: String): Flow<Result<MagneticInfoModel>> =
        networkDataSource.getMagneticInfoByCodeAndListen(promiseCode)

    override suspend fun updateMagneticRadius(gameCode: String, radius: Double): Result<Unit> =
        networkDataSource.updateMagneticRadius(gameCode, radius)

    override suspend fun updateInitialMagneticRadius(gameCode: String): Result<Unit> =
        networkDataSource.updateInitialMagneticRadius(gameCode)

    override suspend fun decreaseMagneticRadius(
        gameCode: String,
        minusValue: Double
    ): Result<Unit> =
        networkDataSource.decreaseMagneticRadius(gameCode, minusValue)

    override suspend fun checkReEntryOfGame(gameCode: String, token: String): Result<Boolean> =
        networkDataSource.checkReEntryOfGame(gameCode, token)

    override suspend fun sendOutUser(gameCode: String, token: String): Result<Unit> =
        networkDataSource.sendOutUser(gameCode, token)

    override suspend fun setUserInitialHpData(gameCode: String, token: String): Result<Unit> =
        networkDataSource.setUserInitialHpData(gameCode, token)

    override suspend fun decreaseUserHp(gameCode: String, token: String): Result<Long> =
        networkDataSource.decreaseUserHp(gameCode, token)

    override suspend fun getUserHpAndListen(
        gameCode: String,
        token: String
    ): Flow<Result<AddedUserHpModel>> =
        networkDataSource.getUserHpAndListen(gameCode, token)

    override suspend fun setPlayerArrived(gameCode: String, token: String): Result<Unit> =
        networkDataSource.setPlayerArrived(gameCode, token)

    override suspend fun getPlayerArrived(gameCode: String, token: String): Flow<Result<Boolean>> =
        networkDataSource.getPlayerArrived(gameCode, token)

    override suspend fun getGameRealtimeRanking(gameCode: String): Flow<Result<List<AddedUserHpModel>>> =
        networkDataSource.getGameRealtimeRanking(gameCode)

    override suspend fun setIsFinishedPromise(gameCode: String): Result<Unit> =
        networkDataSource.setIsFinishedPromise(gameCode)

    override suspend fun getIsFinishedPromise(gameCode: String): Flow<Result<Boolean>> =
        networkDataSource.getIsFinishedPromise(gameCode)

    override suspend fun setIsStartedGame(gameCode: String): Result<Unit> =
        networkDataSource.setIsStartedGame(gameCode)

    override suspend fun getIsStartedGame(gameCode: String): Flow<Result<Boolean>> =
        networkDataSource.getIsStartedGame(gameCode)

    override suspend fun getUserRankings(gameCode: String): Result<List<UserRankingModel>> {
        return runCatching {
            val userHps = networkDataSource.getUserHpList(gameCode).getOrThrow()
            val userProfiles = networkDataSource.getUserInfoList(gameCode).getOrThrow().associate {
                it.userId to it.data
            }

            val mapUserRankingModel = { addedUserHpModel: AddedUserHpModel, rankingNumber: Int ->
                UserRankingModel(
                    addedUserHpModel.userId,
                    userProfiles[addedUserHpModel.userId]
                        ?: throw NullPointerException("userProfiles 의 key 값이 비어있습니다."),
                    hp = addedUserHpModel.hp,
                    rankingNumber = rankingNumber
                )
            }

            val arrivedPartition = userHps.partition { it.arrived }
            val firstRankingUser = arrivedPartition.first.sortedByDescending { it.hp }.map {
                mapUserRankingModel(it, 1)
            }

            val lostPartition = arrivedPartition.second.partition { it.lost }

            val temp = lostPartition.second.sortedByDescending { it.hp }
                .mapIndexed { index, addedUserHpModel ->
                    mapUserRankingModel(
                        addedUserHpModel,
                        index + firstRankingUser.size + 1
                    )
                }
            val middleRankingUser = temp.mapIndexed { index, userRankingModel ->
                if (index != 0 && temp[index - 1].hp == userRankingModel.hp) {
                    userRankingModel.copy(rankingNumber = temp[index - 1].rankingNumber)
                } else {
                    userRankingModel
                }
            }

            val lastRankingUser = lostPartition.first.map {
                mapUserRankingModel(it, middleRankingUser.last().rankingNumber + 1)
            }

            firstRankingUser + middleRankingUser + lastRankingUser
        }
    }

    override suspend fun setUserReady(gameCode: String, token: String): Result<Unit> =
        networkDataSource.setUserReady(gameCode, token)

    override suspend fun getIsReadyUser(gameCode: String, token: String): Flow<Result<Boolean>> =
        networkDataSource.getIsReadyUser(gameCode, token)

    override suspend fun getReadyUsers(gameCode: String): Flow<Result<List<String>>> =
        networkDataSource.getReadyUsers(gameCode)

    override suspend fun getReadyUserList(code: String): Result<List<UserModel>> =
        networkDataSource.getReadyUserList(code)

    override fun getJoinedPromises(): Flow<List<PromiseAlarmModel>> =
        databaseDataSource.getJoinedPromises()

    override suspend fun getPromisesByCodes(codes: List<String>): Flow<List<PromiseHistoryModel>?> =
        networkDataSource.getPromisesByCodes(codes)
}