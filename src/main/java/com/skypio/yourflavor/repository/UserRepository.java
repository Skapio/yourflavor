package com.skypio.yourflavor.repository;

import com.skypio.yourflavor.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

}
