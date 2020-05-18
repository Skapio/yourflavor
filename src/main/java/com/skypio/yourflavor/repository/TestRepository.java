package com.skypio.yourflavor.repository;

import com.skypio.yourflavor.entity.Test;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestRepository extends CrudRepository<Test, Integer> {
    List<Test> findByUserId(String userId);

    Test save(Test test);

    void deleteById(Integer id);

}


