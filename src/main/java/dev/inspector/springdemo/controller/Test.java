package dev.inspector.springdemo.controller;

import java.time.Instant;

public class Test {

    public static void main(String args[]) {
        //long currentTimeMillis = System.currentTimeMillis();
        //System.out.println(currentTimeMillis);

        Instant now = Instant.now();
        Double epochSeconds = (now.getEpochSecond() + now.getNano() / 1_000_000_000.0) * 1_000;
        System.out.printf("%.1f\n", epochSeconds);




    }

}
