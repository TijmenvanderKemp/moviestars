package com.tijmen;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class HopcroftKarpParser {

    private ActorRepository actorRepository = new ActorRepository();

    public Problem parse(InputStream inputMethod) {
        Scanner in = new Scanner(inputMethod);

        String s = in.nextLine();
        String[] actorsAndMovies = s.split(" ");
        int numberOfActors = Integer.parseInt(actorsAndMovies[0]);
        int numberOfMovies = Integer.parseInt(actorsAndMovies[1]);
        actorRepository.femaleActors = getActors(in, numberOfActors, 0);
        actorRepository.maleActors = getActors(in, numberOfActors, numberOfActors);

        Set<Actor> allActors = actorRepository.getAllActors();

        Map<Actor, Map<Actor, Integer>> collabCount = allActors.stream()
                .collect(Collectors.toMap(actor -> actor, actor -> new HashMap<>()));
        for (int i = 0; i < numberOfMovies; i++) {
            addCollabs(in, collabCount);
        }

        Map<Actor, Set<Actor>> collabs = new HashMap<>();
        for (Actor actor : collabCount.keySet()) {
            collabs.put(actor, new HashSet<>(collabCount.get(actor).keySet()));
        }

        return new Problem(actorRepository, collabs, collabCount);
    }

    private Set<Actor> getActors(Scanner in, int numberOfActors, int startOfHash) {
        Set<Actor> set = new HashSet<>();
        for (int i = 0; i < numberOfActors; i++) {
            Integer integer = i;
            String name = in.nextLine();
            Actor actor = new Actor(name, startOfHash + i);
            actorRepository.customHashes.put(name, startOfHash + i);
            set.add(actor);
        }
        return set;
    }

    private void addCollabs(Scanner in, Map<Actor, Map<Actor, Integer>> collabs) {
        // Ignore the name of the movie
        in.nextLine();
        int castSize = Integer.parseInt(in.nextLine());
        Set<Actor> cast = getCast(in, castSize);

        Set<Actor> femaleCast = cast.stream()
                .filter(actorRepository.femaleActors::contains)
                .collect(Collectors.toSet());
        Set<Actor> maleCast = cast.stream()
                .filter(actorRepository.maleActors::contains)
                .collect(Collectors.toSet());


        for (Actor actress : femaleCast) {
            Map<Actor, Integer> collabsWithActress = collabs.get(actress);
            for(Actor actor : maleCast) {
                if(collabsWithActress.containsKey(actor)) {
                    int number = collabsWithActress.get(actor);
                    collabsWithActress.put(actor, number + 1);
                } else {
                    collabsWithActress.put(actor, 1);
                }
            }
        }
    }

    private Set<Actor> getCast(Scanner in, int castSize) {
        Set<Actor> set = new HashSet<>();
        for (int i = 0; i < castSize; i++) {
            String name = in.nextLine();
            set.add(new Actor(name, actorRepository.customHashes.get(name)));
        }
        return set;
    }
}
