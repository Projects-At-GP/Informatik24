package enemy.ai;

import Redfoot.Renderer;
import Redfoot.Tile;
import enemy.ai.Exceptions.NoPathAvailable;
import vector.Vector2;

import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.PriorityQueue;
import java.util.List;
import java.util.Arrays;


public class Algorithms {
    private static Renderer.CachedMapData cachedMap;
    private static Vector2 cachedStart;
    private static Vector2 cachedEnd;
    private static IntelligenceEnum cachedIntelligence;
    private static LinkedList<Vector2> cachedPath;

    /**
     * Checks if the path was already calculated to prevent redundant calculations
     */
    private static boolean isCached(Renderer.CachedMapData map, Vector2 start, Vector2 end, IntelligenceEnum intelligence) {
        return (cachedMap == map &&
                cachedStart == start &&
                cachedEnd == end &&
                cachedIntelligence == intelligence);
    }

    /**
     * Sets the cache to reduce redundant calculations
     */
    private static void setCache(Renderer.CachedMapData map, Vector2 start, Vector2 end, IntelligenceEnum intelligence, LinkedList<Vector2> path) {
        cachedMap = map;
        cachedStart = start;
        cachedEnd = end;
        cachedIntelligence = intelligence;
        cachedPath = path;
    }

    /**
     * Determines a walkable path from a starting position to an ending position if one is available
     * @param map the cached map data containing the chunk with corresponding tiles to determine walkable paths
     * @param start the starting position
     * @param end the ending position
     * @param intelligence information about the available intelligence
     * @return the walkable path with the first element being the start
     * @throws NoPathAvailable if no path was found throws this exception
     */
    public static LinkedList<Vector2> getPath(Renderer.CachedMapData map, Vector2 start, Vector2 end, IntelligenceEnum intelligence) throws NoPathAvailable {
        if (start == null || end == null) return new LinkedList<>();

        // just computed?
        if (isCached(map, start, end, intelligence)) return cachedPath;

        // out of range?
        if (end.subtract(start).magnitude() > intelligence.range) throw new NoPathAvailable(start, end, intelligence);

        LinkedList<Vector2> path = aStar(map, start, end, (int) Math.pow(intelligence.range, 1.75));  // get detailed path

        setCache(map, start, end, intelligence, path);
        return path;
    }

    /**
     * Implementation based on pseudocode from <a href="https://en.wikipedia.org/wiki/A*_search_algorithm#Pseudocode">Wikipedia <i>(A_Star)</i></a>
     * @param map the cached map data containing the chunk with corresponding tiles to determine walkable paths
     * @param start the starting position
     * @param end the ending position
     * @return the walkable path with the first element being the start
     * @throws NoPathAvailable if no path was found throws this exception
     */
    private static LinkedList<Vector2> aStar(Renderer.CachedMapData map, Vector2 start, Vector2 end, int maxIterations) throws NoPathAvailable {
        start = new Vector2(Math.round(start.x), Math.round(start.y));
        end = new Vector2(Math.round(end.x), Math.round(end.y));

        int iterations = 0;

        // cameFrom
        LinkedHashMap<Vector2, Vector2> cameFrom = new LinkedHashMap<>();

        // gScore
        LinkedHashMap<Vector2, Double> gScore = new LinkedHashMap<>();
        gScore.put(start, 0d);

        // fScore
        LinkedHashMap<Vector2, Double> fScore = new LinkedHashMap<>();
        fScore.put(start, calculateCost(start, end));

        PriorityQueue<Vector2> openSet = new PriorityQueue<>((o1, o2) -> (int) ((fScore.get(o1) - fScore.get(o2)) * 100));
        openSet.offer(start);

        Vector2 current;
        double tentativeGScore;
        while (!openSet.isEmpty()) {
            // cap the calculations
            if (iterations > maxIterations) throw new NoPathAvailable(start, end);

            current = openSet.poll();
            if (current.equals(end)) return reconstructAStarPath(cameFrom, current);

            Vector2 finalCurrent = current;  // good practice to make a somewhat final version for lambdas
            LinkedList<Vector2> possibleNeighbors = new LinkedList<>(List.of(
                    new Vector2(finalCurrent.x+1, finalCurrent.y),
                    new Vector2(finalCurrent.x-1, finalCurrent.y),
                    new Vector2(finalCurrent.x, finalCurrent.y+1),
                    new Vector2(finalCurrent.x, finalCurrent.y-1))
            );
            Vector2[] neighbors = Arrays.stream(map.mapData).flatMap(Arrays::stream)
                                                            .filter(tile -> possibleNeighbors.contains(tile.pos))
                                                            .filter(Tile::isWalkable)
                                                            .map(tile -> tile.pos)
                                                            .toArray(Vector2[]::new);
            for (Vector2 neighbor : neighbors) {
                tentativeGScore = gScore.get(current) + 1;  // 1 is default value for walking up/down/left/right; no diagonals here
                if (tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + calculateCost(neighbor, end));
                    if (!openSet.contains(neighbor)) openSet.add(neighbor);
                }
            }
            iterations++;
        }

        throw new NoPathAvailable(start, end);
    }

    /**
     * Implementation based on pseudocode from <a href="https://en.wikipedia.org/wiki/A*_search_algorithm#Pseudocode">Wikipedia <i>(reconstruct_path)</i></a>
     * @param cameFrom the originating position
     * @param current the current position
     * @return the walkable path with the first element being the start
     */
    private static LinkedList<Vector2> reconstructAStarPath(LinkedHashMap<Vector2, Vector2> cameFrom, Vector2 current) {
        LinkedList<Vector2> total_path = new LinkedList<>(List.of(current));

        while ((current = cameFrom.get(current)) != null) total_path.addFirst(current);

        return total_path;
    }

    /**
     * Calculate the cost to go from current to target
     * @param current the current position
     * @param target the target position
     * @return the cost calculated based on the distance from current to target
     */
    private static Double calculateCost(Vector2 current, Vector2 target) {
        return current.subtract(target).magnitude();
    }
}
