package br.com.fernandouchoa.qa.utils;

import java.io.InputStream;

import br.com.fernandouchoa.qa.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestDataManager {

    private static final ObjectMapper mapper =
            new ObjectMapper();

    private static JsonNode rootNode;

    static {

        try {

            InputStream inputStream =
                    TestDataManager.class
                            .getClassLoader()
                            .getResourceAsStream("testdata/users.json");

            rootNode = mapper.readTree(inputStream);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao carregar arquivo users.json",
                    e);
        }
    }

    public static User getUser(String userType) {

        return mapper.convertValue(
                rootNode.get(userType),
                User.class);
    }

}