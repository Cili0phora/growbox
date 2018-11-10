package com.lab.growbox.growbox.repository;

import com.lab.growbox.growbox.entity.Data;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  DataRepository extends CrudRepository<Data, Long> {
    @Override
    <S extends Data> S save(S s);

    @Override
    boolean existsById(Long aLong);

    @Override
    List<Data> findAll();

    @Override
    void delete(Data data);

    @Query(value = "SELECT * FROM (\n" +
            "    SELECT * FROM data ORDER BY id DESC LIMIT 5\n" +
            ") sub\n" +
            "ORDER BY id ASC", nativeQuery = true)
    List<Data> findTop5();
}
