package database;
import data.initial.*;
import encryption.IEncryptor;
import serverLogic.ServerLogger;
import utility.Request;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.ZoneId;
import java.util.LinkedHashSet;
import java.util.NavigableSet;
import java.util.TreeSet;

public class DBManager {
    private final Connection connection;
    private final IEncryptor encryptor;

    public DBManager(String dbUrl, String username, String password, IEncryptor encryptor) throws SQLException {
        connection = DriverManager.getConnection(dbUrl, username, password);
        if (connection != null) {
            this.encryptor = encryptor;
            createDB();
        } else {
            throw new SQLException("Соединение с базой данных не установлено");
        }
    }

    private void createDB() {
        try {
            PreparedStatement statement1 = connection.prepareStatement(DBRequest.CREATE_CLIENT_TABLE.getRequest());
            PreparedStatement statement2 = connection.prepareStatement(DBRequest.CREATE_LABWORK_TABLE.getRequest());

            statement1.execute();
            statement2.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public LinkedHashSet<LabWork> readElementsFromDB() throws SQLException {
        LinkedHashSet<LabWork> list = new LinkedHashSet<LabWork>();
        PreparedStatement statement = connection.prepareStatement(DBRequest.GET_All_LABWORKS.getRequest());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            LabWork labWork = new LabWork();
            int columnIndex = 1;

            labWork.setClientName(resultSet.getString(columnIndex++));
            labWork.setClientBirthday(resultSet.getDate(columnIndex++).toLocalDate());
            labWork.setClientCountry(Country.valueOf(resultSet.getString(columnIndex++)));
            labWork.setClientLocation(new Location(resultSet.getInt(columnIndex++), resultSet.getDouble(columnIndex++),
                    resultSet.getInt(columnIndex++)));

            labWork.setId(resultSet.getLong(columnIndex++));
            labWork.setName(resultSet.getString(columnIndex++));
            labWork.setCoordinates(new Coordinates(resultSet.getLong(columnIndex++), resultSet.getInt(columnIndex++)));
            labWork.setMinimalPoint(resultSet.getFloat(columnIndex++));
            labWork.setDifficulty(Difficulty.valueOf(resultSet.getString(columnIndex++)));
            labWork.setCreationDate(resultSet.getDate(columnIndex).toLocalDate());

            list.add(labWork);
        }
        return list;
    }

    public long addLabworkToDB(LabWork labWork) {
        try {
            PreparedStatement preparedStatement = fillLabworkStatement(connection
                    .prepareStatement(DBRequest.ADD_NEW_LABWORK.getRequest(), Statement.RETURN_GENERATED_KEYS), labWork);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return -1;
        }
    }

    public boolean updateLabworkByID(LabWork labWork) {
        try {
            PreparedStatement preparedStatement = fillLabworkStatement(connection
                    .prepareStatement(DBRequest.UPDATE_LABWORK.getRequest()), labWork);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    public boolean deleteClientLabworks(String clientName) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest.DELETE_CLIENT_LABWORKS.getRequest());
            preparedStatement.setString(1, clientName);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    public boolean deleteLabworkByID(long id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest.DELETE_LABWORK_BY_ID.getRequest());
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    public boolean deleteLabworksGreaterThanMinimalPoint(String clientName, float minimalPoint) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest
                    .DELETE_ALL_GREATER_THAN_MINIMAL_POINT_LABWORKS.getRequest());
            preparedStatement.setString(1, clientName);
            preparedStatement.setDouble(2, minimalPoint);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    public boolean deleteLabworksLowerThanMinimalPoint(String clientName, float minimalPoint) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest
                    .DELETE_ALL_LOWER_THAN_MINIMAL_POINT_LABWORKS.getRequest());
            preparedStatement.setString(1, clientName);
            preparedStatement.setDouble(2, minimalPoint);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    public boolean checkIfUserExist(String clientName) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest.CHECK_IF_CLIENT_EXIST.getRequest());
            preparedStatement.setString(1, clientName);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    public boolean checkIfUserConnect(Request request) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest.CHECK_IF_CLIENT_ENTER_RIGHT_PASSWORD.getRequest());
            preparedStatement.setString(1, request.getClientName());
            preparedStatement.setString(2, encryptor.encrypt(request.getClientPassword()));
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException | NoSuchAlgorithmException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    public boolean registerNewUser(Request request) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest.REGISTER_NEW_CLIENT.getRequest());
            preparedStatement.setString(1, request.getClientName());
            preparedStatement.setString(2, encryptor.encrypt(request.getClientPassword()));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException | NoSuchAlgorithmException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    private PreparedStatement fillLabworkStatement(PreparedStatement preparedStatement, LabWork labWork) throws SQLException {
        int parameterIndex = 1;

        preparedStatement.setString(parameterIndex++, labWork.getClientName());

        preparedStatement.setDate(parameterIndex++, (Date) Date.from(labWork.getClientBirthday().atStartOfDay()
                .atZone(ZoneId.systemDefault()).toInstant()));
        preparedStatement.setObject(parameterIndex++, labWork.getClientCountry());

        preparedStatement.setInt(parameterIndex++, labWork.getClientLocation().getX());
        preparedStatement.setDouble(parameterIndex++, labWork.getClientLocation().getY());
        preparedStatement.setInt(parameterIndex++, labWork.getClientLocation().getZ());

        preparedStatement.setLong(parameterIndex++, labWork.getId());

        preparedStatement.setString(parameterIndex++, labWork.getName());

        preparedStatement.setLong(parameterIndex++, labWork.getCoordinates().getX());
        preparedStatement.setInt(parameterIndex++, labWork.getCoordinates().getY());

        preparedStatement.setFloat(parameterIndex++, labWork.getMinimalPoint());

        preparedStatement.setObject(parameterIndex++, labWork.getDifficulty());

        preparedStatement.setDate(parameterIndex, (Date) Date.from(labWork.getCreationDate().atStartOfDay()
                .atZone(ZoneId.systemDefault()).toInstant()));

        return preparedStatement;
    }
}
