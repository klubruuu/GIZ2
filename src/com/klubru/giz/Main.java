package com.klubru.giz;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        Mode mode = displayModePickerWindow();
        File inputFile = displayInputFilePickerWindow();
        proceed(mode, inputFile);
    }

    private enum Mode {
        BRIDGE, AR_POINTS
    }

    private static Mode displayModePickerWindow() throws Exception {
        Mode[] modes = Mode.values();
        Mode mode = (Mode) JOptionPane.showInputDialog(
                null,
                "Wybierz tryb pracy:",
                null,
                JOptionPane.QUESTION_MESSAGE,
                null,
                modes,
                modes[0]
        );

        if (Objects.isNull(mode))
            throw new Exception("Nie wybrano trybu");

        return mode;
    }

    private static File displayInputFilePickerWindow() throws Exception {
        JFileChooser fc = new JFileChooser();
        int response = fc.showOpenDialog(null);

        if (response == JFileChooser.APPROVE_OPTION)
            return fc.getSelectedFile();
        else
            throw new Exception("Nie wybrano pliku");
    }

    private static void proceed(Mode mode, File inputFile) throws IOException {
        if (mode == Mode.BRIDGE)
            proceedWithBridgeMode(inputFile);
        else if (mode == Mode.AR_POINTS)
            proceedWihArMode(inputFile);
    }

    private static void proceedWithBridgeMode(File inputFile) throws IOException {
        List<String> fileLines = getFileLines(inputFile);
        int nodesCount = getNodesCount(fileLines);

        fileLines.remove(0);
        Graph graph = Graph.buildGraph(nodesCount, fileLines);

        graph.findBridges();
    }

    private static void proceedWihArMode(File inputFile) throws IOException {
        List<String> fileLines = getFileLines(inputFile);
        int nodesCount = getNodesCount(fileLines);

        fileLines.remove(0);
        Graph graph = Graph.buildGraph(nodesCount, fileLines);

        graph.findArticulationPoints();
    }

    private static int getNodesCount(List<String> fileLines) {
        String nodesCount = fileLines.get(0);
        return Integer.parseInt(nodesCount);
    }

    private static List<String> getFileLines(File inputFile) throws IOException {
        return Files.lines(inputFile.toPath()).collect(Collectors.toList());
    }
}
