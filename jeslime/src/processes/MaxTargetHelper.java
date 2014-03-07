/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package processes;

import structural.identifiers.Coordinate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Selects a subset of candidates, when needed, to allow
 * processes to respect a target maximum.
 * <p/>
 * Created by dbborens on 3/7/14.
 */
public abstract class MaxTargetHelper {

    public static Coordinate[] respectMaxTargets(Coordinate[] candidates, int maxTargets, Random random) {
        // If maxTargets is < 0, it means that there is no maxTargets; return all.
        if (maxTargets < 0) {
            return candidates;
        }
        // If there the number of candidates does not exceed the max, return.
        if (candidates.length <= maxTargets) {
            return candidates;
        }

        // Otherwise, permute and choose the first n, where n = maxTargets.
        permute(candidates, random);

        Coordinate[] reduced = Arrays.copyOfRange(candidates, 0, maxTargets);

        return reduced;
    }

    /**
     * Fischer-Yates shuffling algorithm for permuting the contents of
     * a coordinate array.
     */
    private static void permute(Coordinate[] arr, Random random) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            swap(arr, i, j);
        }
    }

    private static void swap(Coordinate[] arr, int i, int j) {
        Coordinate temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }

    public static Coordinate[] respectMaxTargets(ArrayList<Coordinate> candidates, int maxTargets, Random random) {
        // This method is a target for optimization.
        Coordinate[] candidateArr = candidates.toArray(new Coordinate[0]);
        return respectMaxTargets(candidateArr, maxTargets, random);
    }
}
