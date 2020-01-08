package com.tijmen.part2;

import java.util.Map;
import java.util.Set;

public class ProblemContext {
    private int allowedDepth;
    private Problem problem;
    private Map<Actor, Map<Actor, Integer>> collabCount;
    private Set<Actor> options;
    private Actor theirMove;
    private Map<Actor, Integer> relevantScores;
    private Score score;
    private Map<Actor, Actor> matching;

    public int getAllowedDepth() {
        return allowedDepth;
    }

    public ProblemContext withAllowedDepth(int allowedDepth) {
        this.allowedDepth = allowedDepth;
        return this;
    }

    public Problem getProblem() {
        return problem;
    }

    public ProblemContext withProblem(Problem problem) {
        this.problem = problem;
        collabCount = problem.collabCount;
        return this;
    }

    public Set<Actor> getOptions() {
        return options;
    }

    public ProblemContext withOptions(Set<Actor> options) {
        this.options = options;
        return this;
    }

    public Actor getTheirMove() {
        return theirMove;
    }

    public ProblemContext withTheirMove(Actor theirMove) {
        this.theirMove = theirMove;
        relevantScores = collabCount.get(theirMove);
        return this;
    }

    public Map<Actor, Integer> getRelevantScores() {
        return relevantScores;
    }

    public Score getScore() {
        return score;
    }

    public ProblemContext withScore(Score score) {
        this.score = score;
        return this;
    }

    public Map<Actor, Actor> getMatching() {
        return matching;
    }

    public ProblemContext withMatching(Map<Actor, Actor> matching) {
        this.matching = matching;
        return this;
    }

    public ProblemContext copy() {
        return new ProblemContext()
                .withAllowedDepth(allowedDepth)
                .withProblem(problem)
                .withOptions(options)
                .withTheirMove(theirMove)
                .withScore(score)
                .withMatching(matching);
    }
}
