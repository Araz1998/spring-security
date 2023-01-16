package com.badalov.springsecurity.repositories;

import com.badalov.springsecurity.model.User;
import com.badalov.springsecurity.model.UserPhoto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserPhotoRepository extends CrudRepository<UserPhoto, Long> {
    Optional<UserPhoto> findByUserId(User userId);

    @Modifying
    @Query("update UserPhoto up set up.imageSource = :imageSource where up.userId = :userId")
    int updateUserPhoto(@Param("userId") User userId,
                        @Param("imageSource") String imageSource);
}
