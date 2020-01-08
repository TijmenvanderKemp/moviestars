package com.tijmen.part2;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HopcroftKarpParser {

    private final ActorRepository actorRepository = new ActorRepository();
    private Reader in;

    public Problem parse(InputStream inputMethod) {
        in = new ReaderImpl(inputMethod);
        return parse();
    }

    public Problem parse(Reader scanner) {
        in = scanner;
        return parse();
    }

    public Problem parse() {
        String s = in.nextLine();
        String[] actorsAndMovies = s.split(" ");
        int numberOfActors = Integer.parseInt(actorsAndMovies[0]);
        int numberOfMovies = Integer.parseInt(actorsAndMovies[1]);
        actorRepository.femaleActors = getActors(numberOfActors, 0);
        actorRepository.maleActors = getActors(numberOfActors, numberOfActors);

        Set<Actor> allActors = actorRepository.getAllActors();

        Map<Actor, Map<Actor, Integer>> collabCount = allActors.stream()
                .collect(Collectors.toMap(actor -> actor, actor -> new HashMap<>()));
        Collabs collabs = new Collabs(allActors);
        for (int i = 0; i < numberOfMovies; i++) {
            addCollabs(collabCount, collabs);
        }

        return new Problem(actorRepository, collabCount, collabs);
    }

    private Set<Actor> getActors(int numberOfActors, int startOfHash) {
        Set<Actor> set = new HashSet<>();
        for (int i = 0; i < numberOfActors; i++) {
            String name = in.nextLine();
            Actor actor = new Actor(name, startOfHash + i);
            actorRepository.customHashes.put(name, startOfHash + i);
            set.add(actor);
        }
        return set;
    }

    private void addCollabs(Map<Actor, Map<Actor, Integer>> collabCount, Collabs collabs) {
        // Ignore the name of the movie
        in.nextLine();

        Set<Actor> femaleCast = new HashSet<>();
        Set<Actor> maleCast = new HashSet<>();

        int castSize = Integer.parseInt(in.nextLine());
        for (int i = 0; i < castSize; i++) {
            String name = in.nextLine();
            Actor newActor = new Actor(name, actorRepository.customHashes.get(name));
            if (actorRepository.femaleActors.contains(newActor)) {
                femaleCast.add(newActor);
            } else {
                maleCast.add(newActor);
            }
        }

        for (Actor actress : femaleCast) {
            Map<Actor, Integer> collabsWithActress = collabCount.get(actress);
            for (Actor actor : maleCast) {
                collabs.add(actress, actor);
                Map<Actor, Integer> collabsWithActor = collabCount.get(actor);
                collabsWithActress.compute(actor, this::increaseOrDefault1);
                collabsWithActor.compute(actress, this::increaseOrDefault1);
            }
        }
    }

    private int increaseOrDefault1(Actor actor, Integer numberOfCollabs) {
        return numberOfCollabs != null ? numberOfCollabs + 1 : 1;
    }

}
