package com.marknjunge.core.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.marknjunge.core.model.DatabaseKeys
import com.marknjunge.core.model.UserDetails
import kotlinx.coroutines.tasks.await

internal class UserServiceImpl(private val firestore: FirebaseFirestore) : UserService {

    override suspend fun saveUserDetails(userDetails: UserDetails) {
        firestore.collection("users")
                .document(userDetails.id)
                .set(userDetails)
                .await()
    }

    override suspend fun updateUserDetails(id: String, name: String, phone: String, address: String) {
        val userDetailsMap = mapOf(
                DatabaseKeys.User.name to name,
                DatabaseKeys.User.phone to phone,
                DatabaseKeys.User.address to address
        )

        firestore.collection("users")
                .document(id)
                .update(userDetailsMap)
    }

    override suspend fun updateUserFcmToken(userId: String) {
        val instanceIdResult = FirebaseInstanceId.getInstance().instanceId.await()
        val data = mapOf(
                DatabaseKeys.User.fcmToken to instanceIdResult.token
        )
        firestore.collection("users")
                .document(userId)
                .update(data).await()
    }

    override suspend fun getUserDetails(id: String): UserDetails {
        return firestore.collection("users")
                .document(id)
                .get()
                .await()
                .let { snapshot ->
                    UserDetails(
                            snapshot[DatabaseKeys.User.userId] as String,
                            snapshot[DatabaseKeys.User.email] as String,
                            snapshot[DatabaseKeys.User.name] as String,
                            snapshot[DatabaseKeys.User.phone] as String,
                            snapshot[DatabaseKeys.User.address] as String
                    )
                }
    }

}