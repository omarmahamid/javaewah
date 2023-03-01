package com.googlecode.javaewah.datastructure;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgs = {"-Xms4G", "-Xmx4G"})
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
public class PriorityQueueBenchmark{


    @State(Scope.Thread)
    public static class Data {
        PriorityQueue<Integer> javaPriorityQueue;
        PriorityQ<Integer> lemirePriorityQueue;

        @Setup(Level.Iteration)
        public void setUp(){
            int n = 1000000;
            Random random = new Random();
            javaPriorityQueue = new PriorityQueue<>();
            lemirePriorityQueue = new PriorityQ<>(1000000, Integer::compare);
            for (int i = 0; i < n; i++) {
                int x = random.nextInt();
                javaPriorityQueue.offer(x);
                lemirePriorityQueue.toss(x);
            }
            lemirePriorityQueue.buildHeap();
        }
    }

    @Benchmark
    public void testPriorityQueue(Data data) {
        PriorityQueue<Integer> pq = data.javaPriorityQueue;
        while (!pq.isEmpty()) {
            pq.poll();
        }
    }

    @Benchmark
    public void testPriorityQ(Data data) {
        PriorityQ<Integer> pq = data.lemirePriorityQueue;
        while (!pq.isEmpty()) {
            pq.poll();
        }
    }


    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(PriorityQueueBenchmark.class.getSimpleName())
                .mode(Mode.AverageTime)
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(options).run();
    }
}
