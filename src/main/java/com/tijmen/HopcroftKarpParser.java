package com.tijmen;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HopcroftKarpParser {

    private ActorRepository actorRepository = new ActorRepository();
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
        for (int i = 0; i < numberOfMovies; i++) {
            addCollabs(collabCount);
        }

        Map<Actor, Set<Actor>> hopcroftKarpCollabs = actorRepository.getAllActors().stream()
                .collect(Collectors.toMap(actor -> actor, actor -> new HashSet<>()));
        for (Actor actor : actorRepository.femaleActors) {
            hopcroftKarpCollabs.put(actor, new HashSet<>(collabCount.get(actor).keySet()));
        }

        return new Problem(actorRepository, hopcroftKarpCollabs, collabCount);
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

    private void addCollabs(Map<Actor, Map<Actor, Integer>> collabs) {
        // Ignore the name of the movie
        in.nextLine();
        int castSize = Integer.parseInt(in.nextLine());
        Set<Actor> cast = getCast(castSize);

        Set<Actor> femaleCast = cast.stream()
                .filter(actorRepository.femaleActors::contains)
                .collect(Collectors.toSet());
        Set<Actor> maleCast = cast.stream()
                .filter(actorRepository.maleActors::contains)
                .collect(Collectors.toSet());


        for (Actor actress : femaleCast) {
            Map<Actor, Integer> collabsWithActress = collabs.get(actress);
            for(Actor actor : maleCast) {
                Map<Actor, Integer> collabsWithActor = collabs.get(actor);
                if(collabsWithActress.containsKey(actor)) {
                    int number = collabsWithActress.get(actor);
                    collabsWithActress.put(actor, number + 1);
                } else {
                    collabsWithActress.put(actor, 1);
                }
                if(collabsWithActor.containsKey(actress)) {
                    int number = collabsWithActor.get(actress);
                    collabsWithActor.put(actress, number + 1);
                } else {
                    collabsWithActor.put(actress, 1);
                }
            }
        }
    }

    private Set<Actor> getCast(int castSize) {
        Set<Actor> set = new HashSet<>();
        for (int i = 0; i < castSize; i++) {
            String name = in.nextLine();
            set.add(new Actor(name, actorRepository.customHashes.get(name)));
        }
        return set;
    }
}
