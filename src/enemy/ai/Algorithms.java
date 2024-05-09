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

    private static boolean isCached(Renderer.CachedMapData map, Vector2 start, Vector2 end, IntelligenceEnum intelligence) {
        return (cachedMap == map &&
                cachedStart == start &&
                cachedEnd == end &&
                cachedIntelligence == intelligence);
    }
    private static void setCache(Renderer.CachedMapData map, Vector2 start, Vector2 end, IntelligenceEnum intelligence, LinkedList<Vector2> path) {
        cachedMap = map;
        cachedStart = start;
        cachedEnd = end;
        cachedIntelligence = intelligence;
        cachedPath = path;
    }

    public static boolean hasLineOfSight(Renderer.CachedMapData map, Vector2 start, Vector2 end) {
        try {
            getLineOfSightPath(map, start, end);
        } catch (NoPathAvailable e) {
            return false;
        }
        return true;
    }

    // TODO
    public static LinkedList<Vector2> getLineOfSightPath(Renderer.CachedMapData map, Vector2 start, Vector2 end) throws NoPathAvailable {
        return getPath(map, start, end, IntelligenceEnum.NOOB_INTELLIGENCE);
    }

    public static LinkedList<Vector2> getPath(Renderer.CachedMapData map, Vector2 start, Vector2 end, IntelligenceEnum intelligence) throws NoPathAvailable {
        if (start == null || end == null) return new LinkedList<>();
        if (isCached(map, start, end, intelligence)) return cachedPath;  // just computed?

        LinkedList<Vector2> path;

        path = aStar(map, start, end);  // get detailed path

        // TODO: uncomment code
        //int turns = path.size() - 2;  // -2 since 2 points make a straight line, e.g. 3 points would have one corner
        //if (turns > intelligence.maxTurns) throw new NoPathAvailable(start, end, intelligence);

        setCache(map, start, end, intelligence, path);
        return path;
    }

    /**
     * Implementation based on pseudocode from <a href="https://en.wikipedia.org/wiki/A*_search_algorithm#Pseudocode">Wikipedia <i>(A_Star)</i></a>
     */
    private static LinkedList<Vector2> aStar(Renderer.CachedMapData map, Vector2 start, Vector2 end) throws NoPathAvailable {
        start = new Vector2(Math.round(start.x), Math.round(start.y));
        end = new Vector2(Math.round(end.x), Math.round(end.y));

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
        }

        throw new NoPathAvailable(start, end);
    }

    /**
     * Implementation based on pseudocode from <a href="https://en.wikipedia.org/wiki/A*_search_algorithm#Pseudocode">Wikipedia <i>(reconstruct_path)</i></a>
     */
    private static LinkedList<Vector2> reconstructAStarPath(LinkedHashMap<Vector2, Vector2> cameFrom, Vector2 current) {
        LinkedList<Vector2> total_path = new LinkedList<>(List.of(current));

        while ((current = cameFrom.get(current)) != null) total_path.addFirst(current);

        return total_path;
    }

    private static Double calculateCost(Vector2 current, Vector2 target) {
        return current.subtract(target).magnitude();
    }
}
