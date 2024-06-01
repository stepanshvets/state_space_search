public class Main {
    public static void main(String[] args) {
//        Problem problem = new ProblemPuzzleEight(new StatePuzzleEight(new byte[] {7, 4, 1, 8, 5, 2, 6, 3, 0}));
//
//        DFS dfs = new DFS(new CheckerCycleMemory());
//        Solution solutionDFS = dfs.search(problem);
//        System.out.println(solutionDFS.toString(5, 5));
//
//        BFS bfs = new BFS();
//        Solution solutionBFS = bfs.search(problem);
//        System.out.println(solutionBFS);
//
//        Problem problem1 = new ProblemPuzzleEight(new StatePuzzleEight(new byte[] {8, 7, 6, 5, 4, 3, 2, 1, 0}),
//                new StatePuzzleEight(new byte[] {8, 7, 6, 5, 4, 3, 2, 0, 1}));
//
//        Solution solutionOfProblem1BFS = bfs.search(problem1);
//        System.out.println(solutionOfProblem1BFS);
//
//        DFSLim dfsLim = new DFSLim(2);
//        Solution solutionOfProblem1DFSLim = dfsLim.search(problem1);
//        System.out.println(solutionOfProblem1DFSLim);
//
//        DFSLim dfsLim1 = new DFSLim(20);
//        Solution solutionOfProblem1DFSLim1 = dfsLim1.search(problem1);
//        System.out.println(solutionOfProblem1DFSLim1);

//        Problem problem = new ProblemPuzzleEight(
//                new StatePuzzleEight(new byte[] {3, 6, 4, 2, 5, 8, 0, 1, 7}),
//                new StatePuzzleEight(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8}));
        Problem problem = new ProblemPuzzleEight(
                new StatePuzzleEight(new byte[] {8, 7, 6, 5, 4, 3, 2, 1, 0}),
                new StatePuzzleEight(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8}));
//        DFS dfs = new DFS(new CheckerCycleMemory());
//        Solution solutionDFS = dfs.search(problem);
//        System.out.println(solutionDFS.toString(3, 3));

        BFS bfs = new BFS(new CheckerCycleMemory());
        Solution solutionBFS = bfs.search(problem);
        System.out.println(solutionBFS);

//        DFSLim dfsLim = new DFSLim(new CheckerCycleLastParent(), 25);
//        Solution solutionDFSLim = dfsLim.search(problem);
//        System.out.println(solutionDFSLim);
    }
}
