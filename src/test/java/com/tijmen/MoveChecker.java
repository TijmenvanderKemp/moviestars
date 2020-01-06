package com.tijmen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class MoveChecker {

    private Problem problem;
    private final Set<String> chosenActors = new HashSet<>();
    private String previousMove;

    public MoveChecker(File inFile) throws FileNotFoundException {
        HopcroftKarpParser parser = new HopcroftKarpParser();
        problem = parser.parse(new FileInputStream(inFile));
    }

    public void checkFirstMove(Player player, String move) {
        if (problem.actorRepository.femaleActors.stream().map(Actor::getName).noneMatch(move::equals)) {
            illegalMove(player, move);
        }
        chosenActors.add(move);
        previousMove = move;
    }

    public void checkMove(Player player, String move) {
        if (problem.actorRepository.getAllActors().stream().map(Actor::getName).noneMatch(move::equals)) {
            illegalMove(player, move);
        }
        if (chosenActors.contains(move)) {
            illegalMove(player, move);
        }
        if (!problem.collabCount.get(getActor(previousMove)).containsKey(getActor(move))) {
            illegalMove(player, move);
        }
        chosenActors.add(move);
        previousMove = move;
    }

    private Actor getActor(String name) {
        return problem.actorRepository.getByName(name);
    }

    private void illegalMove(Player player, String move) {
        System.out.println(String.format("Player %s made an illegal move: %s", player.getName(), move));
        switch (player) {
            case MARK:
                throw new VeroniqueWon();
            case VERONIQUE:
                throw new MarkWon();
        }
    }
}
