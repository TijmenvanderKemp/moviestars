package com.tijmen;

import org.assertj.core.api.Assertions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Judge {
    private CompetitionRunner veronique;
    private CompetitionRunner mark;

    private enum ReadingStage {
        PROBLEM,
        PLAYER,
        BATTLE
    }

    public Judge(File in, String solution) throws FileNotFoundException {
        Data toVeroniqueData = new Data();
        Data toMarkData = new Data();
        Data fromVeroniqueData = new Data();
        Data fromMarkData = new Data();

        PlayerWriter veroniqueWriter = new PlayerWriter(Player.VERONIQUE, fromVeroniqueData);
        PlayerWriter markWriter = new PlayerWriter(Player.MARK, fromMarkData);

        PlayerReader veroniqueReader = new PlayerReader(Player.VERONIQUE, in, toVeroniqueData);
        PlayerReader markReader = new PlayerReader(Player.MARK, in, toMarkData);

        JudgeWriter judgeWriterToVeronique = new JudgeWriter(toVeroniqueData);
        JudgeWriter judgeWriterToMark = new JudgeWriter(toMarkData);

        JudgeReader judgeReaderFromVeronique = new JudgeReader(fromVeroniqueData);
        JudgeReader judgeReaderFromMark = new JudgeReader(fromMarkData);

        veronique = new CompetitionRunner(veroniqueReader, veroniqueWriter);
        mark = new CompetitionRunner(markReader, markWriter);

        veronique.start();
        mark.start();

        try {
            String firstMove = judgeReaderFromVeronique.nextLine();
            MoveChecker moveChecker = new MoveChecker(in);
            moveChecker.checkFirstMove(Player.VERONIQUE, firstMove);
            judgeWriterToMark.println(firstMove);
            while (true) {
                String marksMove = judgeReaderFromMark.nextLine();
                moveChecker.checkMove(Player.MARK, marksMove);
                judgeWriterToVeronique.println(marksMove);

                String veroniquesMove = judgeReaderFromVeronique.nextLine();
                moveChecker.checkMove(Player.VERONIQUE, veroniquesMove);
                judgeWriterToMark.println(veroniquesMove);
            }
        } catch (VeroniqueWon v) {
            stopPlayers();
            if (solution.equals("Mark")) {
                Assertions.fail("Veronique won where Mark should have");
            }
        } catch (MarkWon m) {
            stopPlayers();
            if (solution.equals("Veronique")) {
                Assertions.fail("Mark won where Veronique should have");
            }
        }
        System.out.println(solution + " won as predicted.");
    }

    private void stopPlayers() {
        veronique.stopThread();
        mark.stopThread();
    }

    private static class JudgeWriter implements Writer {
        private Data data;

        public JudgeWriter(Data data) {
            this.data = data;
        }

        @Override
        public void println(String string) {
            data.send(string);
        }
    }

    private static class JudgeReader implements Reader {
        private Data data;

        public JudgeReader(Data data) {
            this.data = data;
        }

        @Override
        public String nextLine() {
            return data.receive();
        }
    }

    private static class PlayerWriter implements Writer {
        private Player player;
        private Data data;

        public PlayerWriter(Player player, Data data) {
            this.player = player;
            this.data = data;
        }

        @Override
        public void println(String string) {
            System.out.println(player.getName() + " has written something: " + string);
            data.send(string);
        }
    }

    private static class PlayerReader implements Reader {
        private Player player;
        private Scanner scanner;
        private Data data;
        private ReadingStage stage = ReadingStage.PROBLEM;

        public PlayerReader(Player player, File in, Data data) {
            this.player = player;
            try {
                scanner = new Scanner(in);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            this.data = data;
        }

        @Override
        public String nextLine() {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            } else {
                if (stage == ReadingStage.PROBLEM) {
                    stage = ReadingStage.PLAYER;
                }
            }
            if (stage == ReadingStage.PLAYER) {
                stage = ReadingStage.BATTLE;
                return player.getName();
            }
            return data.receive();
        }
    }
}
