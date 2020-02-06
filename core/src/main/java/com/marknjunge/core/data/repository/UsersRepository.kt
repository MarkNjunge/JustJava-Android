package com.marknjunge.core.data.repository

import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.*
import com.marknjunge.core.data.model.UpdateFcmTokenDto
import com.marknjunge.core.data.model.UpdateUserDto
import com.marknjunge.core.data.network.FirebaseService
import com.marknjunge.core.data.network.GoogleSignInClientStub
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

    suspend fun updateFcmToken(): Resource<Unit>

    suspend fun deleteUser():Resource<Unit>
}

internal class ApiUsersRepository(
    private val usersService: UsersService,
    private val preferencesRepository: PreferencesRepository,
    private val googleSignInClient: GoogleSignInClientStub,
    private val firebaseService: FirebaseService
) : UsersRepository {
    override suspend fun getCurrentUser(): Flow<Resource<User>> = flow {
        emit(Resource.Success(preferencesRepository.user!!))

        try {
            val user = usersService.getCurrentUser(preferencesRepository.sessionId)
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
                usersService.updateUser(
                    preferencesRepository.sessionId,
                    UpdateUserDto(
                        firstName,
                        lastName,
                        mobile,
                        email
                    )
                )
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
                preferencesRepository.sessionId,
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
            usersService.deleteAddress(preferencesRepository.sessionId, address.id)
            val addresses = preferencesRepository.user!!.address.toMutableList()
            addresses.remove(address)
            preferencesRepository.user = preferencesRepository.user!!.copy(address = addresses)
            Resource.Success(Unit)
        }
    }

    override suspend fun updateFcmToken() = withContext(Dispatchers.IO) {
        call {
            val token = firebaseService.getFcmToken()
            usersService.updateFcmToken(preferencesRepository.sessionId, UpdateFcmTokenDto(token))
            val updatedUser = preferencesRepository.user!!.copy(fcmToken = token)
            preferencesRepository.user = updatedUser
            Resource.Success(Unit)
        }
    }

    override suspend fun deleteUser(): Resource<Unit> = withContext(Dispatchers.IO) {
        call {
            val res = usersService.deleteUser(preferencesRepository.sessionId)
            if (preferencesRepository.user!!.signInMethod == "GOOGLE"){
                googleSignInClient.signOut()
            }
            preferencesRepository.user = null
            preferencesRepository.sessionId = ""
            Resource.Success(res)
        }
    }

}