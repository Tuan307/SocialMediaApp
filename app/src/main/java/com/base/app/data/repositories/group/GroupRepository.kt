package com.base.app.data.repositories.group

import com.base.app.base.network.BaseRemoteService
import com.base.app.base.network.NetworkResult
import com.base.app.data.apis.DatingAPI
import com.base.app.data.models.group.request.CreateGroupInvitationRequest
import com.base.app.data.models.group.request.CreateGroupPostRequest
import com.base.app.data.models.group.request.CreateGroupRequest
import com.base.app.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 10/10/2023
 */
class GroupRepository @Inject constructor(
    private val api: DatingAPI,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseRemoteService() {

    suspend fun createGroup(request: CreateGroupRequest) = withContext(dispatcher) {
        when (val result = callApi { api.createGroup(request) }) {
            is NetworkResult.Success -> {
                result.data
            }
            is NetworkResult.Error -> {
                throw result.exception
            }
        }
    }

    suspend fun createGroupInvitation(request: CreateGroupInvitationRequest) =
        withContext(dispatcher) {
            when (val result = callApi { api.addGroupInvitation(request) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun getYourOwnGroup(userId: String, pageCount: Int, pageNumber: Int) =
        withContext(dispatcher) {
            when (val result = callApi { api.getGroupByOwnerId(userId, pageCount, pageNumber) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun getYourJoinedGroup(userId: String, pageCount: Int, pageNumber: Int) =
        withContext(dispatcher) {
            when (val result = callApi { api.getGroupByUserId(userId, pageCount, pageNumber) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun checkIfJoinedGroup(userId: String, groupId: Long) =
        withContext(dispatcher) {
            when (val result = callApi { api.checkIfJoinedGroup(userId, groupId) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun getGroupById(groupId: Long) =
        withContext(dispatcher) {
            when (val result = callApi { api.getGroupById(groupId) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun getAllGroupPost(groupId: Long, pageCount: Int, pageNumber: Int) =
        withContext(dispatcher) {
            when (val result = callApi { api.getAllGroupPost(groupId, pageCount, pageNumber) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun getAllGroupMemberByGroupId(groupId: Long) =
        withContext(dispatcher) {
            when (val result = callApi { api.getAllGroupMemberByGroupId(groupId) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun addGroupPost(request: CreateGroupPostRequest) =
        withContext(dispatcher) {
            when (val result = callApi { api.addGroupPost(request) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }
}