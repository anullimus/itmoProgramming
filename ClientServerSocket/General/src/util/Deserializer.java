package util;

import exception.DeserializeException;
import com.company.server.Request;
import com.company.server.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Deserializer {
    private Deserializer() {
    }

    public static Response deserializeResponse(byte[] serializedObject) {
        try (ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(serializedObject);
             ObjectInputStream objectInputStream = new ObjectInputStream(arrayInputStream)) {
            return (Response) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new DeserializeException("Невозможно десериализовать объект");
        }
    }

    public static Request deserializeRequest(byte[] serializedObject) {
        try (ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(serializedObject);
             ObjectInputStream objectInputStream = new ObjectInputStream(arrayInputStream)) {
            return (Request) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new DeserializeException("Невозможно десериализовать объект");
        }
    }
}
