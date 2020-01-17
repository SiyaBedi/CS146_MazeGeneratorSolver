package BediSlivkoff.cs146.project3;

import org.junit.jupiter.api.Test;

//tests a 4X4 maze, you can change the size manually
class MazeTest {

	@Test
	void maze1() {
		int size = 4;
		Maze perfectMaze = new Maze(size);
		perfectMaze.generatePerfectMaze();
		System.out.println(perfectMaze);
		
		Maze dfs = perfectMaze.solveDFS();
		System.out.println("DFS: ");
		System.out.println(dfs);
		dfs.labelPath();
		System.out.println(dfs);
		
		Maze bfs = perfectMaze.solveBFS();
		System.out.println("BFS: ");
		System.out.println(bfs);
		bfs.labelPath();
		System.out.println(bfs);
	}
	
}