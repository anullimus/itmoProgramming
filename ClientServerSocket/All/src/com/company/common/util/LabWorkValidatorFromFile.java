package com.company.common.util;

import javax.xml.validation.Validator;
import java.util.Map;

/*
import com.lab.common.Data.Route;
import com.lab.common.Exceptions.RouteValidateException;
import javax.validation.Validator;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.Map;

public final class RouteValidator {
    private static final Map<Long, Long> ID_MAP;
    private static final Validator VALIDATOR;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        VALIDATOR = factory.getValidator();
        ID_MAP = new HashMap<>();
    }

    private RouteValidator() {
        throw new UnsupportedOperationException("This class should not be instantiated");
    }

    public static void validateRoutes(Route... routes) {
        for (Route route : routes) {
            ID_MAP.put(route.getId(), ID_MAP.getOrDefault(route.getId(), 0L) + 1);
            if (VALIDATOR.validate(route).size() > 0 || VALIDATOR.validate(route.getFrom()).size() > 0
                    || (route.getTo() != null && VALIDATOR.validate(route.getTo()).size() > 0)
                    || VALIDATOR.validate(route.getCoordinates()).size() > 0 || ID_MAP.get(route.getId()) > 1) {
                throw new RouteValidateException("В данных содержатся ошибки");
            }
        }
    }
}
 */
public class LabWorkValidatorFromFile {
//    private static final Map<Long, Long> ID_MAP;
//    private static final Validator VALIDATOR;
}
