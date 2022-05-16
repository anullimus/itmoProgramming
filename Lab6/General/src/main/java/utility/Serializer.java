package utility;

import exception.SerializeException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public final class Serializer {
    private Serializer() {
    }

    public static byte[] serializeRequest(Request request) {
        try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(arrayOutputStream)) {
            outputStream.writeObject(request);
            outputStream.flush();
            return arrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new SerializeException("Невозможно сериализовать запрос");
        }
    }

    public static byte[] serializeResponse(Response response) {
        try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(arrayOutputStream)) {
            outputStream.writeObject(response);
            outputStream.flush();
            return arrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new SerializeException("Невозможно сериализовать ответ");
        }
    }
}
