package com.happyfxmas.day5;

import com.happyfxmas.utils.InputReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Almanac {
    long seed;
    long soil;
    long fertilizer;
    long water;
    long light;
    long temperature;
    long humidity;
    long location;
    long count;

    public Almanac(long seed) {
        this.seed = seed;
    }
    public Almanac(long seed, long count) {
        this.seed = seed;
        this.count = count;
    }
}

public class Solution {
    private static final String SEED_TO_SOIL_KEY = "seed-to-soil map:";
    private static final String SOIL_TO_FERTILIZER_KEY = "soil-to-fertilizer map:";
    private static final String FERTILIZER_TO_WATER_KEY = "fertilizer-to-water map:";
    private static final String WATER_TO_LIGHT_KEY = "water-to-light map:";
    private static final String LIGHT_TO_TEMPERATURE_KEY = "light-to-temperature map:";
    private static final String TEMPERATURE_TO_HUMIDITY_KEY = "temperature-to-humidity map:";
    private static final String HUMIDITY_TO_LOCATION_KEY = "humidity-to-location map:";
    private static final List<List<Long>> SEED_TO_SOIL_RANGE_LIST = new ArrayList<>();
    private static final List<List<Long>> SOIL_TO_FERTILIZER_RANGE_LIST = new ArrayList<>();
    private static final List<List<Long>> FERTILIZER_TO_WATER_RANGE_LIST = new ArrayList<>();
    private static final List<List<Long>> WATER_TO_LIGHT_RANGE_LIST = new ArrayList<>();
    private static final List<List<Long>> LIGHT_TO_TEMPERATURE_RANGE_LIST = new ArrayList<>();
    private static final List<List<Long>> TEMPERATURE_TO_HUMIDITY_RANGE_LIST = new ArrayList<>();
    private static final List<List<Long>> HUMIDITY_TO_LOCATION_RANGE_LIST = new ArrayList<>();

    public static void main(String[] args) {
        var data = InputReader.readInput("day5/input.txt");
        System.out.println(part1(data));
        System.out.println(part2(data));
    }

    public static Long part1(List<String> data) {
        List<Almanac> almanacList = collectAlmanacList(data.get(0));
        collectAllRangeMaps(data);
        return findMinimumAlmanacFromInputRanges(almanacList);
    }

    public static Long part2(List<String> data) {
        List<Almanac> almanacList = collectAlmanacRanges(data.get(0));
        collectAllRangeMaps(data);
        return findMinimumAlmanacRangesFromInputRanges(almanacList); // calculating more than 5 min :) but the input numbers are crazy
    }

    private static List<Almanac> collectAlmanacList(String line) {
        return Arrays.stream(line.split("\\s"))
                .skip(1)
                .map(seed -> new Almanac(Long.parseLong(seed)))
                .toList();
    }

    private static void collectAllRangeMaps(List<String> data) {
        String keyBuffer = null;
        for (int i = 1; i < data.size(); i++) {
            String currentLine = data.get(i);
            if (currentLine.isEmpty()) {
                keyBuffer = null;
                continue;
            }
            if (keyBuffer == null) {
                if (currentLine.contains("map")) {
                    keyBuffer = currentLine;
                }
            } else {
                List<Long> rangeValues = collectRangeValuesFromLine(currentLine);
                switch (keyBuffer) {
                    case SEED_TO_SOIL_KEY -> SEED_TO_SOIL_RANGE_LIST.add(rangeValues);
                    case SOIL_TO_FERTILIZER_KEY -> SOIL_TO_FERTILIZER_RANGE_LIST.add(rangeValues);
                    case FERTILIZER_TO_WATER_KEY -> FERTILIZER_TO_WATER_RANGE_LIST.add(rangeValues);
                    case WATER_TO_LIGHT_KEY -> WATER_TO_LIGHT_RANGE_LIST.add(rangeValues);
                    case LIGHT_TO_TEMPERATURE_KEY -> LIGHT_TO_TEMPERATURE_RANGE_LIST.add(rangeValues);
                    case TEMPERATURE_TO_HUMIDITY_KEY -> TEMPERATURE_TO_HUMIDITY_RANGE_LIST.add(rangeValues);
                    case HUMIDITY_TO_LOCATION_KEY -> HUMIDITY_TO_LOCATION_RANGE_LIST.add(rangeValues);
                }
            }
        }
    }

    private static List<Long> collectRangeValuesFromLine(String line) {
        return Arrays.stream(line.split("\\s"))
                .map(Long::parseLong)
                .toList();
    }

    private static Long getDestinationFromRangesOrCurrentValue(Long current, List<List<Long>> ranges) {
        for (var rangeValues : ranges) {
            Long start = rangeValues.get(1);
            Long count = rangeValues.get(2);
            if (current >= start && current < start + count) { // if value in range
                Long destination = rangeValues.get(0);
                return destination - start + current; // destination: (difference between end and start) + value
            }
        }
        return current;
    }

    private static Long findMinimumAlmanacFromInputRanges(List<Almanac> almanacList) {
        long min = Long.MAX_VALUE;
        for (var almanac : almanacList) {
            almanac.soil = getDestinationFromRangesOrCurrentValue(almanac.seed, SEED_TO_SOIL_RANGE_LIST);
            almanac.fertilizer = getDestinationFromRangesOrCurrentValue(almanac.soil, SOIL_TO_FERTILIZER_RANGE_LIST);
            almanac.water = getDestinationFromRangesOrCurrentValue(almanac.fertilizer, FERTILIZER_TO_WATER_RANGE_LIST);
            almanac.light = getDestinationFromRangesOrCurrentValue(almanac.water, WATER_TO_LIGHT_RANGE_LIST);
            almanac.temperature = getDestinationFromRangesOrCurrentValue(almanac.light, LIGHT_TO_TEMPERATURE_RANGE_LIST);
            almanac.humidity = getDestinationFromRangesOrCurrentValue(almanac.temperature, TEMPERATURE_TO_HUMIDITY_RANGE_LIST);
            almanac.location = getDestinationFromRangesOrCurrentValue(almanac.humidity, HUMIDITY_TO_LOCATION_RANGE_LIST);

            if (almanac.location < min) min = almanac.location;
        }
        return min;
    }

    private static List<Almanac> collectAlmanacRanges(String line) {
        List<Almanac> almanacList = new ArrayList<>();
        String[] seedRanges = line.split("\\s");
        for (int i = 1; i < seedRanges.length; i += 2) {
            long seed = Long.parseLong(seedRanges[i]);
            long count = Long.parseLong(seedRanges[i+1]);
            almanacList.add(new Almanac(seed, count));
        }
        return almanacList;
    }

    private static Long findMinimumAlmanacRangesFromInputRanges(List<Almanac> almanacList) {
        long min = Long.MAX_VALUE;
        for (var almanac : almanacList) {
            for (long i = 0; i < almanac.count; i++) {
                almanac.soil = getDestinationFromRangesOrCurrentValue(almanac.seed, SEED_TO_SOIL_RANGE_LIST);
                almanac.fertilizer = getDestinationFromRangesOrCurrentValue(almanac.soil, SOIL_TO_FERTILIZER_RANGE_LIST);
                almanac.water = getDestinationFromRangesOrCurrentValue(almanac.fertilizer, FERTILIZER_TO_WATER_RANGE_LIST);
                almanac.light = getDestinationFromRangesOrCurrentValue(almanac.water, WATER_TO_LIGHT_RANGE_LIST);
                almanac.temperature = getDestinationFromRangesOrCurrentValue(almanac.light, LIGHT_TO_TEMPERATURE_RANGE_LIST);
                almanac.humidity = getDestinationFromRangesOrCurrentValue(almanac.temperature, TEMPERATURE_TO_HUMIDITY_RANGE_LIST);
                almanac.location = getDestinationFromRangesOrCurrentValue(almanac.humidity, HUMIDITY_TO_LOCATION_RANGE_LIST);

                if (almanac.location < min) min = almanac.location;
                almanac.seed++;
            }
        }
        return min;
    }
}
