package lab2;

public class MainTest {
    public static void main(String[] args) {
        State state1 = new StatePuzzleEight(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8});
        State state2 = new StatePuzzleEight(new byte[] {8, 7, 6, 5, 4, 3, 2, 1, 0});
//        System.out.println(state1.distance(state2));

        Problem problem1 = new ProblemPuzzleEight(
                new StatePuzzleEight(new byte[] {3, 6, 4, 2, 5, 8, 0, 1, 7}),
                new StatePuzzleEight(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8}));
        Problem problem = new ProblemPuzzleEight(state2, state1);
        GeneralSearch generalSearch = new GeneralSearch();
        Solution solution = generalSearch.generalSearch(problem1, new CheckerCycleParents(), new BestQueuing());
        System.out.println(solution);

    }
}
