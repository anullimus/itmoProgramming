package database;

public enum DBRequest {

    CREATE_CLIENT_TABLE("create table if not exists s335100db_clients\n"
            + "(\n"
            + "client_name text not null primary key,\n"
            + "password text not null\n"
            + ");"),
    CREATE_LABWORK_TABLE("create table if not exists labworks\n"
            + "(\n"
            + "client_name text references s335100db_clients(client_name) on delete cascade,\n"
            + "client_birthday timestamp not null,\n"
            + "client_country object not null, \n"
            + "x_location integer not null,\n"
            + "y_location double precision not null,\n"
            + "z_location integer not null,\n"
            + "id bigserial primary key,\n"
            + "title text not null check (char_length(name_from) > 0),\n"
            + "x_coordinate integer not null,\n"
            + "y_coordinate double precision not null,\n"
            + "z_coordinate integer not null,\n"
            + "minimal_point float precision check (minimal_point > 0)\n"
            + "difficulty object not null, \n"
            + "creation_date timestamp not null,\n"
            + ");\n"),
    GET_All_LABWORKS("select * from labworks"),
    ADD_NEW_LABWORK("insert into labworks (\"client_name\", \"client_birthday\", \"client_country\", \"x_location\", " +
            "\"y_location\", \"z_location\", \"title\", \"x_coordinate\", \"y_coordinate\", \"z_coordinate\", " +
            "\"minimal_point\", \"difficulty\", \"creation_date\") values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"),
    UPDATE_LABWORK("update labworks set (\"client_name\", \"client_birthday\", \"client_country\", \"x_location\", " +
            "\"y_location\", \"z_location\", \"title\", \"x_coordinate\", \"y_coordinate\", \"z_coordinate\", " +
            "\"minimal_point\", \"difficulty\", \"creation_date\") values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) where id=?"),
    DELETE_CLIENT_LABWORKS("delete from labworks where client_name=?"),
    DELETE_LABWORK_BY_ID("delete from labworks where id=?"),
    DELETE_ALL_GREATER_THAN_MINIMAL_POINT_LABWORKS("delete from labworks where client_name=? and minimal_point>?"),
    DELETE_ALL_LOWER_THAN_MINIMAL_POINT_LABWORKS("delete from labworks where client_name=? and minimal_point<?"),
    CHECK_IF_CLIENT_EXIST("select from s335100db_clients where client_name=?"),
    CHECK_IF_CLIENT_ENTER_RIGHT_PASSWORD("select from s335100db_clients where client_name=? and password=?"),
    REGISTER_NEW_CLIENT("insert into s335100db_clients (\"client_name\", \"password\") values(?, ?)");

    private final String request;

    DBRequest(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }
}
