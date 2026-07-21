package br.com.fernandouchoa.qa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fernandouchoa.qa.model.User;

public final class TestDataManager {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Pattern ENVIRONMENT_PLACEHOLDER =
            Pattern.compile("^\\$\\{([A-Z0-9_]+)}$");

    private static final JsonNode ROOT_NODE = loadUsers();

    private TestDataManager() {
    }

    public static User getUser(String userType) {
        JsonNode userNode = ROOT_NODE.get(userType);

        if (userNode == null || userNode.isNull()) {
            throw new IllegalArgumentException(
                    "Usuário de teste não encontrado: " + userType
            );
        }

        User user = MAPPER.convertValue(userNode, User.class);
        user.setEmail(resolveValue(user.getEmail()));
        user.setPassword(resolveValue(user.getPassword()));

        return user;
    }

    private static JsonNode loadUsers() {
        try (InputStream inputStream = TestDataManager.class
                .getClassLoader()
                .getResourceAsStream("testdata/users.json")) {

            if (inputStream == null) {
                throw new IllegalStateException(
                        "Arquivo testdata/users.json não foi encontrado."
                );
            }

            return MAPPER.readTree(inputStream);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Erro ao carregar o arquivo users.json.",
                    exception
            );
        }
    }

    private static String resolveValue(String configuredValue) {
        if (configuredValue == null) {
            return null;
        }

        Matcher matcher = ENVIRONMENT_PLACEHOLDER.matcher(configuredValue);

        if (!matcher.matches()) {
            return configuredValue;
        }

        String variableName = matcher.group(1);
        String resolvedValue = System.getenv(variableName);

        if (resolvedValue == null || resolvedValue.isBlank()) {
            resolvedValue = System.getProperty(variableName);
        }

        if (resolvedValue == null || resolvedValue.isBlank()) {
            throw new IllegalStateException(
                    "Defina a variável de ambiente ou propriedade Java "
                            + variableName
                            + " antes de executar os testes."
            );
        }

        return resolvedValue;
    }
}
