package com.tijmen.part1;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        Set<Integer> allActors = actorRepository.getAllActors();

        Collabs collabs = new Collabs(allActors.size());
        for (int i = 0; i < numberOfMovies; i++) {
            addCollabs(collabs);
        }
        collabs.constructMap();

        return new Problem(actorRepository, null, collabs);
    }

    private Set<Integer> getActors(int numberOfActors, int startOfHash) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < numberOfActors; i++) {
            String name = in.nextLine();
            Integer actor = startOfHash + i;
            actorRepository.customHashes.put(name, startOfHash + i);
            set.add(actor);
        }
        return set;
    }

    private void addCollabs(Collabs collabs) {
        // Ignore the name of the movie
        in.nextLine();
        int castSize = Integer.parseInt(in.nextLine());
        List<Integer> femaleCast = new ArrayList<>();
        List<Integer> maleCast = new ArrayList<>();

        for (int i = 0; i < castSize; i++) {
            Integer actor = actorRepository.getByName(in.nextLine()).hashCode;
            if (actorRepository.femaleActors.contains(actor)) {
                femaleCast.add(actor);
            } else {
                maleCast.add(actor);
            }
        }

        for (Integer actress : femaleCast) {
            for (Integer actor : maleCast) {
                collabs.add(actress, actor);
            }
        }

    }

}
