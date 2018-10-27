package com.lab.growbox.growbox.repository;

import com.lab.growbox.growbox.entity.Data;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  DataRepository extends CrudRepository<Data, Long> {
    @Override
    <S extends Data> S save(S s);

    @Override
    boolean existsById(Long aLong);

    @Override
    Iterable<Data> findAll();

    @Override
    void delete(Data data);
}
