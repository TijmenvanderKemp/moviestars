package com.tijmen;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MinMaxParser {
    public MinMaxAlgorithm parse(InputStream inputMethod) {
        Scanner in = new Scanner(inputMethod);

        String s = in.nextLine();
        String[] actorsAndMovies = s.split(" ");
        int numberOfActors = Integer.parseInt(actorsAndMovies[0]);
        int numberOfMovies = Integer.parseInt(actorsAndMovies[1]);
        Set<Actor> femaleActors = getActors(in, numberOfActors);
        Set<Actor> maleActors = getActors(in, numberOfActors);

        Set<Actor> allActors = new HashSet<>(femaleActors);
        allActors.addAll(maleActors);

        Map<Actor, Set<Actor>> collabs = allActors.stream()
                .collect(Collectors.toMap(actor -> actor, actor -> new HashSet<>()));
        for (int i = 0; i < numberOfMovies; i++) {
            addCollabs(in, femaleActors, maleActors, allActors, collabs);
        }

        return new MinMaxAlgorithm(collabs, femaleActors);
    }

    private Set<Actor> getActors(Scanner in, int numberOfActors) {
        return IntStream.range(0, numberOfActors).boxed()
                .map(integer -> in.nextLine())
                .map(Actor::new)
                .collect(Collectors.toSet());
    }

    private void addCollabs(Scanner in, Set<Actor> femaleActors, Set<Actor> maleActors, Set<Actor> allActors,
                            Map<Actor, Set<Actor>> collabs) {
        // Ignore the name of the movie
        in.nextLine();
        int castSize = Integer.parseInt(in.nextLine());
        Set<Actor> cast = getCast(in, allActors, castSize);

        Set<Actor> femaleCast = cast.stream()
                .filter(femaleActors::contains)
                .collect(Collectors.toSet());
        Set<Actor> maleCast = cast.stream()
                .filter(maleActors::contains)
                .collect(Collectors.toSet());

        femaleCast.forEach(actress -> collabs.get(actress).addAll(maleCast));
        maleCast.forEach(actor -> collabs.get(actor).addAll(femaleCast));
    }

    private Set<Actor> getCast(Scanner in, Set<Actor> allActors, int castSize) {
        return IntStream.range(0, castSize).boxed()
                .map(integer -> in.nextLine())
                .map(name -> allActors.stream()
                        .filter(actor -> name.equals(actor.name))
                        .findFirst().orElseThrow(() -> new IllegalStateException("acteurs niet goed ingelezen")))
                .collect(Collectors.toSet());
    }
}
