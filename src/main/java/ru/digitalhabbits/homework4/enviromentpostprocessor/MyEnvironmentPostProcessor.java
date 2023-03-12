package ru.digitalhabbits.homework4.enviromentpostprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static org.springframework.core.io.support.ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;

public class MyEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private final static Logger log = LoggerFactory.getLogger(MyEnvironmentPostProcessor.class);

    private final PropertySourceLoader loader = new PropertiesPropertySourceLoader();
    private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private final String DEFAULT_PATH_TO_RESOURCES = "config/*.properties,config/*.yml";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (environment.containsProperty("additional.variables") &&
                environment.getProperty("additional.variables") != null) {

            String property = environment.getProperty("additional.variables");

            getSortedResources(Objects.requireNonNull(property))
                    .map(this::loadProperty)
                    .forEach(environment.getPropertySources()::addLast);
        } else {
            getSortedResources(DEFAULT_PATH_TO_RESOURCES)
                    .map(this::loadProperty)
                    .forEach(environment.getPropertySources()::addLast);
        }
    }

    private Stream<Resource> getSortedResources(@Nonnull String path) {
        List<String> listPaths = Arrays.stream(path.split(","))
                .map(it -> CLASSPATH_ALL_URL_PREFIX + it)
                .collect(Collectors.toList());

        return listPaths.stream()
                .flatMap(this::getResourcesOnPath)
                .filter(it -> it.isReadable() && it.getFilename() != null)
                .sorted(comparing(Resource::getFilename));
    }

    private Stream<Resource> getResourcesOnPath(String pathToResources) {
        try {
            return Stream.of(resolver.getResources(pathToResources));
        } catch (IOException e) {
            log.error("Invalid path to Resources: {}", pathToResources);
            return Stream.empty();
        }
    }

    private PropertySource<?> loadProperty(Resource resource) {
        Assert.isTrue(resource.exists(), () -> "Resource " + resource + " does not exist");
        try {
            return this.loader.load(resource.getFilename(), resource).get(0);
        } catch (IOException e) {
            log.error("Failed to load resource: {}", resource);
            throw new RuntimeException(e);
        }
    }
}
