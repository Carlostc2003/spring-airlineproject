package com.ilicitan_airlines.backend.config;

import org.springframework.boot.ansi.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.Locale;

@Component
public class StartupReadyMessage {
    private static final int WIDTH = 72;

    private final Environment env;

    public StartupReadyMessage(Environment env) {this.env = env;}

    @EventListener(ApplicationReadyEvent.class)
    public void print(ApplicationReadyEvent e) {
        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
        String port = env.getProperty("local.server.port", env.getProperty("server.port", "8080"));
        String baseUrl = "http://localhost:" + port;
        String startupTime = formatDuration(e.getTimeTaken());

        System.out.println();
        printLine(AnsiColor.GREEN, border());
        printLine(AnsiColor.CYAN, AnsiStyle.BOLD, box(center("ILICITAN AIRLINES BACKEND", WIDTH - 4)));
        printLine(AnsiColor.GREEN, box(""));
        printLine(AnsiColor.GREEN, AnsiStyle.BOLD, box("STATUS   READY"));
        printLine(AnsiColor.DEFAULT, box("URL      " + baseUrl));
        printLine(AnsiColor.DEFAULT, box("API      " + baseUrl + "/api"));
        printLine(AnsiColor.DEFAULT, box("MONGODB  Atlas connection verified"));
        printLine(AnsiColor.DEFAULT, box("STARTUP  " + startupTime));
        printLine(AnsiColor.GREEN, border());
        System.out.println();
    }

    private String formatDuration(Duration d) {
        if (d == null) return "n/a";
        long ms = d.toMillis();
        if (ms < 1000) return ms + " ms";
        return String.format(Locale.US, "%.2f s", ms / 1000.0);
    }

    private void printLine(Object... elements) {
        System.out.println(AnsiOutput.toString(elements));
    }

    private String border() {return "+" + "-".repeat(WIDTH - 2) + "+";}

    private String box(String content) {return "| " + padRight(content, WIDTH - 4) + " |";}

    private String center(String content, int width) {
        if (content.length() >= width) return content;
        int left = (width - content.length()) / 2;
        int right = width - content.length() - left;
        return " ".repeat(left) + content + " ".repeat(right);
    }

    private String padRight(String content, int width) {
        if (content.length() >= width) return content;
        return content + " ".repeat(width - content.length());
    }
}
