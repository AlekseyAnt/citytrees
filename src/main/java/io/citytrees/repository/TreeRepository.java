package io.citytrees.repository;

import io.citytrees.model.Tree;
import io.citytrees.repository.extension.TreeRepositoryExtension;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TreeRepository extends CrudRepository<Tree, UUID>, TreeRepositoryExtension {
    @Query("""
        INSERT INTO ct_tree(id, user_id, geo_point)
        VALUES (:id, :userId, ST_SetSRID(ST_MakePoint(:x, :y), :srid))
        RETURNING id
        """)
    UUID create(UUID id, UUID userId, double x, double y, Integer srid);

    @Modifying
    @Query("DELETE FROM ct_tree where id = :id")
    void deleteById(@NotNull UUID id);
}
