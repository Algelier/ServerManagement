package net.algelier.servermanagement.common.log;

import java.util.ArrayList;
import java.util.List;

public class StackTraceData {

    private List<String> data;

    public StackTraceData(String header) {
        this.data = new ArrayList<>();
        this.data.add(header);
    }

    public void addData(String trace) {
        this.data.add(trace);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        data.forEach((line) -> builder.append(line + "\n"));

        return builder.toString();
    }
}
