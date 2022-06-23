package db;

import data.initial.*;
import db.encription.IEncryptor;
import serverLogic.ServerLogger;
import utility.Request;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;

public class DatabaseHandler {
    private String URL;
    private String username;
    private String password;
    private Connection connection;
    private IEncryptor encryptor;

    public DatabaseHandler(String URL, String username, String password, IEncryptor encryptor) {
        this.URL = URL;
        this.username = username;
        this.password = password;
        this.encryptor = encryptor;
    }

    public void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(URL, username, password);
            System.out.println("Подключение к базе данных установлено.");
            if (connection != null) {
                createDB();
            } else {
                throw new SQLException("Соединение с базой данных не установлено");
            }
        } catch (SQLException sqlException) {
            System.err.println("Не удалось подключиться к базе данных. Завершение работы.");
            System.exit(-1);
        }
    }

    /**
     * Создание таблицы
     */
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

    /**
     * Чтение объектов ЛабРабот из базы данных
     */
    public LinkedHashSet<LabWork> readElementsFromDB() throws SQLException {
        LinkedHashSet<LabWork> set = new LinkedHashSet<>();
        PreparedStatement statement = connection.prepareStatement(DBRequest.GET_All_LABWORKS.getRequest());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            LabWork labWork = new LabWork();
            int columnIndex = 1;
            labWork.setId(resultSet.getLong(columnIndex++));
            Location location = new Location(resultSet.getInt(columnIndex++), resultSet.getDouble(columnIndex++),
                    resultSet.getInt(columnIndex++));
            labWork.setAuthor(new Person(resultSet.getString(columnIndex++),
                    LocalDate.parse(resultSet.getString(columnIndex++)),
                    Country.valueOf(resultSet.getString(columnIndex++)),
                    location));

            labWork.setName(resultSet.getString(columnIndex++));
            labWork.setCoordinates(new Coordinates((long) resultSet.getInt(columnIndex++),
                    resultSet.getInt(columnIndex++)));
            labWork.setMinimalPoint(resultSet.getFloat(columnIndex++));
            labWork.setDifficulty(Difficulty.valueOf(resultSet.getString(columnIndex)));

            set.add(labWork);
        }
        return set;
    }

    // Далее нужно создать методы для работы с коллекцией в базе данных
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

    public boolean deleteLabworksGreaterThanMinPoint(String clientName, double distance) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest.DELETE_ALL_GREATER_THAN_MIN_POINT_LABWORKS.getRequest());
            preparedStatement.setString(1, clientName);
            preparedStatement.setDouble(2, distance);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    public boolean deleteLabworksLowerThanMinPoint(String clientName, double distance) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest.DELETE_ALL_LOWER_THAN_MIN_POINT_LABWORKS.getRequest());
            preparedStatement.setString(1, clientName);
            preparedStatement.setDouble(2, distance);
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

    public boolean checkIfUserConnect(String username, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest.CHECK_IF_CLIENT_ENTER_RIGHT_PASSWORD.getRequest());
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, encryptor.encrypt(password));
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException | NoSuchAlgorithmException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    public boolean registerUser(String username, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest.REGISTER_NEW_CLIENT.getRequest());
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, encryptor.encrypt(password));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException | NoSuchAlgorithmException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }


    /**
     * Отправка запросов на сервер с базой данных
     */
    private PreparedStatement fillLabworkStatement(PreparedStatement preparedStatement, LabWork labWork) throws SQLException {
        int parameterIndex = 1;
        preparedStatement.setString(parameterIndex++, labWork.getName());
        preparedStatement.setDate(parameterIndex++, Date.valueOf(labWork.getAuthor().getBirthday()));
        preparedStatement.setObject(parameterIndex++, labWork.getAuthor().getNationality());
        //filed "location" setting
        preparedStatement.setInt(parameterIndex++, labWork.getAuthor().getLocation().getX());
        preparedStatement.setDouble(parameterIndex++, labWork.getAuthor().getLocation().getY());
        preparedStatement.setInt(parameterIndex++, labWork.getAuthor().getLocation().getZ());

        preparedStatement.setString(parameterIndex++, labWork.getAuthor().getName());


        //filed "coordinates" setting
        preparedStatement.setLong(parameterIndex++, labWork.getCoordinates().getX());
        preparedStatement.setInt(parameterIndex++, labWork.getCoordinates().getY());

        preparedStatement.setFloat(parameterIndex++, labWork.getMinimalPoint());
        preparedStatement.setObject(parameterIndex, labWork.getDifficulty());
        return preparedStatement;
    }
}
