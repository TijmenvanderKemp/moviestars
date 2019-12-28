package com.tijmen;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StandardWinning implements Strategy {

    private Map<Actor, Actor> matching;

    public StandardWinning(Map<Actor, Actor> MtoFmatching, Player player) {
        if(player.equals(Player.MARK)) {
            MtoFmatching.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        }
        this.matching = matching;
    }

    @Override
    public Actor nextMove(int allowedDepth, Triple<Problem, Set<Actor>, Actor> problemOptionsAndMove, PlayingLineValue score) {
        return matching.get(problemOptionsAndMove.getRight());
    }
}
