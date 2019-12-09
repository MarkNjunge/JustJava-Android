package com.marknjunge.core.data.repository

import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.*
import com.marknjunge.core.data.model.UpdateFcmTokenDto
import com.marknjunge.core.data.model.UpdateUserDto
import com.marknjunge.core.data.network.UsersService
import com.marknjunge.core.utils.call
import com.marknjunge.core.utils.parseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

interface UsersRepository {
    suspend fun getCurrentUser(): Flow<Resource<User>>

    suspend fun updateUser(firstName: String, lastName: String, mobile: String, email: String): Resource<Unit>

    suspend fun addAddress(address: Address): Resource<Address>

    suspend fun deleteAddress(address: Address): Resource<Unit>

    suspend fun updateFcmToken(token: String): Resource<Unit>

    suspend fun logout(): Resource<Unit>
}

internal class ApiUsersRepository(
    private val usersService: UsersService,
    private val preferencesRepository: PreferencesRepository
) : UsersRepository {
    override suspend fun getCurrentUser(): Flow<Resource<User>> = flow {
        emit(Resource.Success(preferencesRepository.user!!))


        try {
            val user = usersService.getCurrentUser()
            preferencesRepository.user = user
            emit(Resource.Success(user))
        } catch (e: Exception) {
            val failure: Resource<User> = parseException(e)
            emit(failure)
        }
    }

    override suspend fun updateUser(firstName: String, lastName: String, mobile: String, email: String) =
        withContext(Dispatchers.IO) {
            call {
                usersService.updateUser(UpdateUserDto(firstName, lastName, mobile, email))
                val updatedUser = preferencesRepository.user!!.copy(
                    firstName = firstName,
                    lastName = lastName,
                    mobileNumber = mobile,
                    email = email
                )
                preferencesRepository.user = updatedUser

                Resource.Success(Unit)
            }
        }

    override suspend fun addAddress(address: Address): Resource<Address> = withContext(Dispatchers.IO) {
        call {
            val saveAddress = usersService.saveAddress(
                SaveAddressDto(
                    address.streetAddress,
                    address.deliveryInstructions,
                    address.latLng
                )
            )
            val addresses = preferencesRepository.user!!.address.toMutableList()
            addresses.add(saveAddress)
            preferencesRepository.user = preferencesRepository.user!!.copy(address = addresses)
            Resource.Success(saveAddress)
        }
    }

    override suspend fun deleteAddress(address: Address): Resource<Unit> = withContext(Dispatchers.IO) {
        call {
            usersService.deleteAddress(address.id)
            val addresses = preferencesRepository.user!!.address.toMutableList()
            addresses.remove(address)
            preferencesRepository.user = preferencesRepository.user!!.copy(address = addresses)
            Resource.Success(Unit)
        }
    }

    override suspend fun updateFcmToken(token: String) = withContext(Dispatchers.IO) {
        call {
            usersService.updateFcmToken(UpdateFcmTokenDto(token))
            val updatedUser = preferencesRepository.user!!.copy(fcmToken = token)
            preferencesRepository.user = updatedUser
            Resource.Success(Unit)
        }
    }

    override suspend fun logout(): Resource<Unit> {
        preferencesRepository.user = null
        preferencesRepository.sessionId = ""
        return Resource.Success(Unit)
    }
}