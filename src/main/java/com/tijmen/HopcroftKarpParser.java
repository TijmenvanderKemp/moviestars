package com.tijmen;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HopcroftKarpParser {
    public HopcroftKarpGraph parse(InputStream inputMethod) {
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
            addCollabs(in, femaleActors, maleActors, collabs);
        }

        return new HopcroftKarpGraph(femaleActors, maleActors, collabs);
    }

    private Set<Actor> getActors(Scanner in, int numberOfActors) {
        return IntStream.range(0, numberOfActors).boxed()
                .map(integer -> in.nextLine())
                .map(Actor::new)
                .collect(Collectors.toSet());
    }

    private void addCollabs(Scanner in, Set<Actor> femaleActors, Set<Actor> maleActors,
                            Map<Actor, Set<Actor>> collabs) {
        // Ignore the name of the movie
        in.nextLine();
        int castSize = Integer.parseInt(in.nextLine());
        Set<Actor> cast = getCast(in, castSize);

        Set<Actor> femaleCast = cast.stream()
                .filter(femaleActors::contains)
                .collect(Collectors.toSet());
        Set<Actor> maleCast = cast.stream()
                .filter(maleActors::contains)
                .collect(Collectors.toSet());

        femaleCast.forEach(actress -> collabs.get(actress).addAll(maleCast));
    }

    private Set<Actor> getCast(Scanner in, int castSize) {
        Set<Actor> set = new HashSet<>();
        for (int i = 0; i < castSize; i++) {
            String name = in.nextLine();
            set.add(new Actor(name));
        }
        return set;
    }
}
