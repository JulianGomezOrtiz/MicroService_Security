package com.ucaldas.mssecurity.Repositories;

import com.ucaldas.mssecurity.Models.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
    @Query("{'phone_number':?0}")
    Optional<UserProfile> getProfile(String phone_number);

    @Query("{'theUser.$id': ObjectId(?0)}")
    UserProfile getProfilebyUserId(String userId);
}
