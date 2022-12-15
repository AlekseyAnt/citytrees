package io.citytrees.repository.extension.rowmapper;

import io.citytrees.model.Tree;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TreeRowMapper implements RowMapper<Tree> {
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    @Override
    @SneakyThrows
    public Tree mapRow(ResultSet rs, int rowNum) {
        Tree.TreeBuilder builder = Tree.builder();
        PGobject geoPoint = rs.getObject("geo_point", PGobject.class);
        Point point = (Point) PGgeometry.geomFromString(Objects.requireNonNull(geoPoint.getValue()));

        return builder
            .id(rs.getObject("id", UUID.class))
            .userId(rs.getObject("user_id", UUID.class))
            .geoPoint(GEOMETRY_FACTORY.createPoint(new Coordinate(point.getX(), point.getY())))
            .build();
    }
}
