package co.edu.uptc.animals_rest.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import co.edu.uptc.animals_rest.exception.InvalidRangeException;
import co.edu.uptc.animals_rest.models.Animal;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Service
public class AnimalService {
    private static final Logger logger = LoggerFactory.getLogger(AnimalService.class);
    @Value("${animal.file.path}")
    private String filePath;

    @Autowired
    private HttpServletRequest request;

    public List<Animal> getAnimalInRange(int from, int to) throws IOException {
        List<String> listAnimal = Files.readAllLines(Paths.get(filePath));
        List<Animal> animales = new ArrayList<>();
        String ipclient = request.getRemoteAddr();
        animales.add(new Animal("IP del cliente: ", ipclient));

        if (from < 0 || to >= listAnimal.size() || from > to) {
            logger.warn("Invalid range: Please check the provided indices. Range: 0 to {}", listAnimal.size());
            throw new InvalidRangeException("Invalid range: Please check the provided indices.");
        }

        for (String line : listAnimal) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                String categoria = parts[0].trim();
                String nombre = parts[1].trim();
                animales.add(new Animal(nombre, categoria));
            }
        }

        return animales.subList(from, to + 1);
    }

    public List<Animal> getAnimalAll() throws IOException {
        List<String> listAnimal = Files.readAllLines(Paths.get(filePath));
        List<Animal> animales = new ArrayList<>();
        String ipclient = request.getRemoteAddr();
        animales.add(new Animal("IP del cliente: ", ipclient));
        for (String line : listAnimal) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                String category = parts[0].trim();
                String name = parts[1].trim();
                animales.add(new Animal(name, category));
            }
        }

        return animales;
    }

    public Map<String, Integer> getAnimalCount() throws IOException {
        List<String> listAnimal = Files.readAllLines(Paths.get(filePath));
        Map<String, Integer> categoryCountMap = new HashMap<>();
        String ipclient = request.getRemoteAddr();
        categoryCountMap.put(ipclient, 1);
        for (String line : listAnimal) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                String category = parts[0].trim();
                categoryCountMap.put(category, categoryCountMap.getOrDefault(category, 0) + 1);
            }
        }
        return categoryCountMap;
    }

}