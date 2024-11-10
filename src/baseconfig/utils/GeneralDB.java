package baseconfig.utils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import baseconfig.annotations.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class GeneralDB<T> {
    private final Class<T> type;

    public GeneralDB(Class<T> type) {
        this.type = type;
    }

    // Récupérer la colonne qui est primary key
    static String getPrimaryKeyColumn(Connection conn, String table) throws SQLException {
        try (ResultSet rs = conn.getMetaData().getPrimaryKeys(null, null, table)) {
            if (rs.next()) {
                return rs.getString("COLUMN_NAME");
            }
        }
        return null;
    }

    // Récupérer tous les fields des mères
    static Field[] allFields(Class<?> objClass) {
        ArrayList<Field> lsFields = new ArrayList<>();

        while (objClass != Object.class) {
            for (Field field : objClass.getDeclaredFields()) {
                lsFields.add(field);
            }
            objClass = objClass.getSuperclass();
        }

        return lsFields.toArray(new Field[0]);
    }

    // Méthode pour récupérer tous les enregistrements avec une condition WHERE
    public List<T> getWhere(Connection conn, String whereClause, Object... params) throws Exception {
        List<T> results = new ArrayList<>();
        String tableName = type.getAnnotation(Table.class).name();
        String sql = "SELECT * FROM " + tableName + " WHERE " + whereClause;

        try (Connection connection = conn;
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    T instance = type.getDeclaredConstructor().newInstance();
                    for (Field field : allFields(type)) {
                        if (field.isAnnotationPresent(Column.class)) {
                            Column column = field.getAnnotation(Column.class);
                            String columnName = column.name();
                            String setterName = "set" + capitalize(field.getName());
                            Method setter = type.getMethod(setterName, field.getType());
                            setter.invoke(instance, resultSet.getObject(columnName));
                        }
                    }
                    results.add(instance);
                }
            }
        }
        return results;
    }

    // CREATE: Ajout d'une nouvelle entrée dans la base de données
    public void create(Connection conn, T entity) throws SQLException, IllegalAccessException {
        Table table = type.getAnnotation(Table.class);
        if (table == null) throw new RuntimeException("No Table annotation present.");

        StringBuilder query = new StringBuilder("INSERT INTO " + table.name() + " (");
        StringBuilder values = new StringBuilder("VALUES (");

        Field[] fields = allFields(type);
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            DefaultValue defaultValue = field.getAnnotation(DefaultValue.class);
            if (column != null && defaultValue == null) {
                query.append(column.name()).append(",");
                values.append("?,");
            }
        }

        query.setLength(query.length() - 1);
        values.setLength(values.length() - 1);
        query.append(") ").append(values).append(")");

        try (Connection connection = conn;
             PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            
            int index = 1;
            for (Field field : fields) {
                field.setAccessible(true);
                Column column = field.getAnnotation(Column.class);
                DefaultValue defaultValue = field.getAnnotation(DefaultValue.class);
                if (column != null && defaultValue == null) {
                    stmt.setObject(index++, field.get(entity));
                }
            }
            stmt.executeUpdate();
        }
    }

    // READ: Lecture d'une entrée à partir de son identifiant
    public T read(Connection conn, String id) throws Exception {
        Table table = type.getAnnotation(Table.class);
        if (table == null) throw new RuntimeException("No Table annotation present.");

        String query = "SELECT * FROM " + table.name() + " WHERE " + getPrimaryKeyColumn(conn, table.name()) + " = ?";
        T entity = null;

        try (Connection connection = conn;
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Constructor<T> constructor = type.getDeclaredConstructor();
                    entity = constructor.newInstance();

                    Field[] fields = allFields(type);
                    for (Field field : fields) {
                        Column column = field.getAnnotation(Column.class);
                        if (column != null) {
                            String setterName = "set" + capitalize(field.getName());
                            Method setter = type.getMethod(setterName, field.getType());
                            setter.invoke(entity, rs.getObject(column.name()));
                        }
                    }
                }
            }
        }
        return entity;
    }

    // UPDATE: Mise à jour d'une entrée dans la base de données
    public void update(Connection conn, T entity, String id) throws Exception {
        Table table = type.getAnnotation(Table.class);
        if (table == null) throw new RuntimeException("No Table annotation present.");

        StringBuilder query = new StringBuilder("UPDATE " + table.name() + " SET ");
        Field[] fields = allFields(type);

        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                query.append(column.name()).append(" = ?,");
            }
        }

        query.setLength(query.length() - 1);
        query.append(" WHERE " + getPrimaryKeyColumn(conn, table.name()) + " = ?");

        try (PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            int index = 1;
            for (Field field : fields) {
                field.setAccessible(true);
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    stmt.setObject(index++, field.get(entity));
                }
            }
            stmt.setString(index, id);
            stmt.executeUpdate();
        }
    }

    // DELETE: Suppression d'une entrée
    public void delete(Connection conn, String id) throws SQLException {
        Table table = type.getAnnotation(Table.class);
        if (table == null) throw new RuntimeException("No Table annotation present.");

        String query = "DELETE FROM " + table.name() + " WHERE " + getPrimaryKeyColumn(conn, table.name()) + " = ?";
        
        try (Connection connection = conn;
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    // FIND ALL: Récupération de toutes les entrées
    public List<T> findAll(Connection conn) throws Exception {
        Table table = type.getAnnotation(Table.class);
        if (table == null) throw new RuntimeException("No Table annotation present.");

        String query = "SELECT * FROM " + table.name();
        List<T> results = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Constructor<T> constructor = type.getDeclaredConstructor();
                T entity = constructor.newInstance();

                Field[] fields = allFields(type);
                for (Field field : fields) {
                    Column column = field.getAnnotation(Column.class);
                    if (column != null) {
                        String setterName = "set" + capitalize(field.getName());
                        Method setter = type.getMethod(setterName, field.getType());
                        if (field.getType() == int.class) {
                            setter.invoke(entity, rs.getInt(column.name()));
                        } else if (field.getType() == double.class) {
                            setter.invoke(entity, rs.getDouble(column.name()));
                        } else {
                            setter.invoke(entity, rs.getObject(column.name()));
                        }
                    }
                }
                results.add(entity);
            }
        }
        return results;
    }

    // Méthode auxiliaire pour capitaliser la première lettre
    private String capitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
