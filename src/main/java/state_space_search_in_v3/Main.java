package lab211;

public class Main {
    public static void main(String[] args) {
        State state1 = new StatePuzzleEight(new byte[] {3, 6, 4, 2, 5, 8, 0, 1, 7});
        State state2 = new StatePuzzleEight(new byte[] {4, 1, 2, 3, 0, 5, 6, 7, 8});
//        System.out.println(state1.distance(state2));

        Problem problem1 = new ProblemPuzzleEight(
                state1,
                state2);
        Problem problem = new ProblemPuzzleEight(state2, state1);

        // Solution solution = generalSearch.generalSearch(problem1, new CheckerCycleMemory(), new BestQueuing());
        // System.out.println(solution);
//
//        GeneralSearch generalSearch = new GeneralSearch();
//        Solution solution1 = generalSearch.generalSearch(problem1,
//                new CheckerCycleMemory(),
//                new BestQueuing(new AStarComparator()),
//                new ManhattanDistancePuzzleEight());
//        System.out.println(solution1);
//        System.out.println(generalSearch.count);

        AStarS aStarS = new AStarS(new CheckerCycleMemory(), new ManhattanDistancePuzzleEight());
        Solution solution = aStarS.search(problem1);
        System.out.println(solution);

    }
}
