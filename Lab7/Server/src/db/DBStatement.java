package db;

public enum DBRequest {
    CREATE_CLIENT_TABLE("create table if not exists s335100db_clients\n"
            + "(\n"
            + "client_name text not null primary key,\n"
            + "password text not null\n"
            + ");"),
    CREATE_LABWORK_TABLE("create table if not exists labworks\n"
            + "(\n"
            + "id bigserial primary key,\n"
            + "client_name text references s335100db_clients(client_name) on delete cascade,\n"
            + "birthday timestamp not null,\n"
            + "country text not null check (char_length(country) > 0),\n"
            + "x_location integer not null,\n"
            + "y_location double precision not null,\n"
            + "z_location integer not null,\n"

            + "labwork_title text not null check (char_length(labwork_title) > 0),\n"
            + "x_coordinate bigint not null,\n"
            + "y_coordinate integer not null,\n"
            + "min_point decimal not null check (x_coordinate > 0),\n"
            + "difficulty text not null check (char_length(difficulty) > 0),\n"
            + ");\n"),
    GET_All_LABWORKS("select * from labworks"),
    ADD_NEW_LABWORK("insert into labworks (\"client_name\", \"birthday\", \"country\", " +
            "\"x_location\", \"y_location\", \"z_location\", " +
            "\"labwork_title\", \"x_coordinate\", \"y_coordinate\", \"min_point\", \"difficulty\")"
            + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"),
    UPDATE_LABWORK("update labworks set (\"client_name\", \"birthday\", \"country\", " +
            "\"x_location\", \"y_location\", \"z_location\", " +
            "\"labwork_title\", \"x_coordinate\", \"y_coordinate\", \"min_point\", \"difficulty\")"
            + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"),
    DELETE_CLIENT_LABWORKS("delete from labworks where client_name=?"),
    DELETE_LABWORK_BY_ID("delete from labworks where id=?"),
    DELETE_ALL_GREATER_THAN_MIN_POINT_LABWORKS("delete from labworks where client_name=? and min_point>?"),
    DELETE_ALL_LOWER_THAN_MIN_POINT_LABWORKS("delete from labworks where client_name=? and min_point<?"),
    CHECK_IF_CLIENT_EXIST("select from s335100db_clients where client_name=?"),
    CHECK_IF_CLIENT_ENTER_RIGHT_PASSWORD("select from s335100db_clients where client_name=? and password=?"),
    REGISTER_NEW_CLIENT("insert into s335100db_clients (\"client_name\", \"password\") values(?, ?)"),
    FIND_USERNAME_REQUEST("SELECT COUNT(*) AS count FROM s335100db_clients WHERE username = ?");

    private final String request;

    DBRequest(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

}
