package ru.digitalhabbits.homework4.enviromentpostprocessor;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

public class MyEnvironmentPostProcessorV2 implements EnvironmentPostProcessor {

    private static final String CONFIG_EXTENSION = ".properties";

    private static final String[] CONFIG_PATHS = {"file:config/*", "classpath:config/*"};

    private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private final PropertySourceLoader loader = new PropertiesPropertySourceLoader();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Arrays.stream(CONFIG_PATHS)
                .flatMap(this::getResources)
                .filter(it -> it.getFilename() != null)
                .sorted(Comparator.comparing(Resource::getFilename))
                .map(this::loadResource)
                .forEach(environment.getPropertySources()::addLast);
    }

    private PropertySource<?> loadResource(Resource resource) {
        try {
            return loader.load(resource.getFilename(), resource).get(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<Resource> getResources(String configPath) {
        try {
            return Stream.of(resolver.getResources(configPath + CONFIG_EXTENSION));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
