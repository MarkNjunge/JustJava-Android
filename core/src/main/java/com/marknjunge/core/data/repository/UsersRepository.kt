package com.marknjunge.core.data.repository

import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.*
import com.marknjunge.core.data.network.service.FirebaseService
import com.marknjunge.core.data.network.service.GoogleSignInService
import com.marknjunge.core.data.network.service.UsersService
import com.marknjunge.core.data.network.call
import com.marknjunge.core.utils.parseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface UsersRepository {
    suspend fun getCurrentUser(): Flow<Resource<User>>

    suspend fun updateUser(firstName: String, lastName: String, mobile: String, email: String): Resource<Unit>

    suspend fun addAddress(address: Address): Resource<Address>

    suspend fun deleteAddress(address: Address): Resource<Unit>

    suspend fun updateFcmToken(): Resource<Unit>

    suspend fun deleteUser(): Resource<Unit>
}

internal class ApiUsersRepository(
    private val usersService: UsersService,
    private val preferencesRepository: PreferencesRepository,
    private val googleSignInClient: GoogleSignInService,
    private val firebaseService: FirebaseService
) : UsersRepository {

    @Suppress("TooGenericExceptionCaught")
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

    override suspend fun updateUser(
        firstName: String,
        lastName: String,
        mobile: String,
        email: String
    ): Resource<Unit> {
        return call {
            usersService.updateUser(UpdateUserDto(firstName, lastName, mobile, email))
            val updatedUser = preferencesRepository.user!!.copy(
                firstName = firstName,
                lastName = lastName,
                mobileNumber = mobile,
                email = email
            )
            preferencesRepository.user = updatedUser
        }
    }

    override suspend fun addAddress(address: Address): Resource<Address> {
        return call {
            val saveAddress = usersService.saveAddress(
                SaveAddressDto(address.streetAddress, address.deliveryInstructions, address.latLng)
            )
            val addresses = preferencesRepository.user!!.address.toMutableList()
            addresses.add(saveAddress)
            preferencesRepository.user = preferencesRepository.user!!.copy(address = addresses)
            saveAddress
        }
    }

    override suspend fun deleteAddress(address: Address): Resource<Unit> {
        return call {
            usersService.deleteAddress(address.id)
            val addresses = preferencesRepository.user!!.address.toMutableList()
            addresses.remove(address)
            preferencesRepository.user = preferencesRepository.user!!.copy(address = addresses)
        }
    }

    override suspend fun updateFcmToken(): Resource<Unit> {
        return call {
            val token = firebaseService.getFcmToken()
            usersService.updateFcmToken(UpdateFcmTokenDto(token))
            val updatedUser = preferencesRepository.user!!.copy(fcmToken = token)
            preferencesRepository.user = updatedUser
        }
    }

    override suspend fun deleteUser(): Resource<Unit> {
        return call {
            usersService.deleteUser()
            if (preferencesRepository.user!!.signInMethod == "GOOGLE") {
                googleSignInClient.signOut()
            }
            preferencesRepository.user = null
            preferencesRepository.sessionId = ""
        }
    }
}
