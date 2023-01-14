package com.badalov.springsecurity.repositories;

import com.badalov.springsecurity.model.UserPhoto;
import org.springframework.data.repository.CrudRepository;

public interface UserPhotoRepository extends CrudRepository<UserPhoto, Long> {
}
