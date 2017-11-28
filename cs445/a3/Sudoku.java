package cs445.a3;

import java.util.List;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Sudoku {
	private static int[][] initialBoard = new int[9][9];
	private static int[][] testBoard = new int[9][9];
	private static int previousRowX;
	private static int previousColY;
	
	// Begin isFullSolution()
    static boolean isFullSolution(int[][] board) {
    	// Loop through the board to check for any 0s. If zero exists, break from loop and return false.
        for (int row = 0; row < board.length; row++) {
        	for (int column = 0; column < board.length; column++) {
        		if (board[row][column] == 0) {
        			return false;
        		}
        	}
        }
        return true;
    }

    // Begin reject()
    static boolean reject(int[][] board, int row, int column) {
        if (row == -1) {
        	return false;
        }
        
        if (notInRowOrCol(board, row, column, board[row][column]) && !isInSmallGrid(board, row, column, board[row][column])) {
           	return false;
        }
    	

        return true;
    }

    // Begin extend()
    static int[][] extend(int[][] board) {
        int[][] newPartialSolution = new int[9][9];
        
        
        // Add all current entries from "board" partialSolution to newPartialSolution.
        for (int row = 0; row < newPartialSolution.length; row++) {
        	for (int column = 0; column < newPartialSolution.length; column++) {
        		newPartialSolution[row][column] = board[row][column];
        	}
        }
        
        // If there's a 0, set equal to 1.
        for (int row = 0; row < board.length; row++) {
        	for (int col = 0; col < board.length; col++) {
        		if (newPartialSolution[row][col] == 0) {
        			newPartialSolution[row][col] = 1;
        			previousRowX = row;
        			previousColY = col;
        			return newPartialSolution;
        		}
        	}
        }
        
        return null;
    }

    static int[][] next(int[][] board, int line, int col) {
    	// Confirm this is not a fixed spot being passed in.
    	if (board[line][col] < 9) {
    		board[line][col] += 1;
    		return board;
    	}

    	return null;
    }
	
	
	// Check to see if cell value is repeated in it's row or column.
	static boolean notInRowOrCol(int[][] board, int testRow, int testCol, int cellValue) {

		// Test to see if value reappears in the tested column.
		for (int row = 0; row < board.length; row++) {
			if (board[row][testCol] == cellValue && row != testRow) {
				
				return false;
			}
		}
		
		// Test to see if value reappears in the tested row.
		for (int col = 0; col < board.length; col++) {
			if (board[testRow][col] == cellValue && col != testCol) {
				
				return false;
			}
		}
		
		return true;
	}
	
	// Check to see if the cell value passed in is contained in it's own 3x3 grid.
	static boolean isInSmallGrid(int[][] currentBoard, int testRow, int testCol, int cellValue) {

		int topLeftX = testRow / 3 * 3;
		int topLeftY = testCol / 3 * 3;
		for (int i = topLeftX; i < topLeftX + 3; i++) {
			for (int j = topLeftY; j < topLeftY + 3; j++) {
				if (i != testRow && j != testCol) {
					if (currentBoard[i][j] == cellValue) {
						return true;
					}
				}
			}
		}
		
		return false;
	}

    static void testIsFullSolution() {
        
    	// TEST A SOLVED BOARD.
    	testBoard = readBoard("testSolvedBoard.su");
        printBoard(testBoard);
        boolean isFullSolutionWorked = isFullSolution(testBoard);
        
        if (isFullSolutionWorked == true) {
        	System.out.println("The board contains no zeroes and is a full solution.");
        } else {
        	System.out.println("The board contains zeroes...is not full solution.");
        }
        
        // TEST AN UNSOLVED BOARD.
        testBoard = readBoard("testIsFullSolutionUnsolved.su");
        printBoard(testBoard);
        isFullSolutionWorked = isFullSolution(testBoard);
        
        if (isFullSolutionWorked == true) {
        	System.out.println("The board contains no zeroes and is a full solution.");
        } else {
        	System.out.println("The board contains zeroes...is not full solution.");
        }
        
    }

    static void testReject() {
    	// SAME ROW TEST
    	System.out.println("TWO 8s IN FIRST ROW");
    	testBoard = readBoard("testRejectSameRow.su");
        printBoard(testBoard);
        boolean shouldReject = reject(testBoard, 0, 2);
        System.out.println("SAME ROW TEST.");
        if (shouldReject == true) {
        	System.out.println("The board was rejected, and should have been rejected.");
        } else {
        	System.out.println("The board was not rejected, and should have been rejected.");
        }
        
        // SAME COLUMN TEST
        System.out.println("TWO 6s IN THIRD COLUMN");
        testBoard = readBoard("testRejectSameColumn.su");
        printBoard(testBoard);
        shouldReject = reject(testBoard, 0, 2);
        System.out.println("SAME COLUMN TEST.");
        if (shouldReject == true) {
        	System.out.println("The board was rejected, and should have been rejected.");
        } else {
        	System.out.println("The board was not rejected, and should have been rejected.");
        }
        
     // SAME GRID TEST
        System.out.println("TWO 2s IN FIRST SMALL GRID");
        testBoard = readBoard("testRejectSameGrid.su");
        printBoard(testBoard);
        shouldReject = reject(testBoard, 2, 1);
        System.out.println("SAME GRID TEST.");
        if (shouldReject == true) {
        	System.out.println("The board was rejected, and should have been rejected.");
        } else {
        	System.out.println("The board was not rejected, and should have been rejected.");
        }
        
    }

    static void testExtend() {
    	// TEST AN EMPTY BOARD
    	System.out.println("EMPTY BOARD TEST.");
    	testBoard = readBoard("testExtendEmpty.su");
        System.out.println("Before Extend: \n");
    	printBoard(testBoard);
    	System.out.println("\nAfter Extend: \n");
    	testBoard = extend(testBoard);
    	printBoard(testBoard);
    	
    	// TEST A FULL BOARD
    	System.out.println("FULL BOARD TEST");
    	testBoard = readBoard("testSolvedBoard.su");
        System.out.println("Before Extend: \n");
    	printBoard(testBoard);
    	System.out.println("\nAfter Extend: \n");
    	testBoard = extend(testBoard);
    	printBoard(testBoard);
    	
    	
    }

    static void testNext() {
    	// TEST AN ARBITRARY BOARD AT ARBITRARY LOCATION.
    	System.out.println("TEST AN ARBITRARY BOARD AT ARBITRARY LOCATION.");
    	testBoard = readBoard("testNext.su");
        System.out.println("Before Next: \n");
    	printBoard(testBoard);
    	System.out.println("\nAfter Next: \n");
    	testBoard = next(testBoard, 3, 6);
    	printBoard(testBoard);
    	
    	// TEST AN ARBITRARY BOARD AT ARBITRARY LOCATION.
    	System.out.println("TEST LOCATION WITH VALUE 9.");
    	testBoard = readBoard("testNextWith9.su");
        System.out.println("Before Next: \n");
    	printBoard(testBoard);
    	System.out.println("\nAfter Next: \n");
    	testBoard = next(testBoard, 3, 6);
    	printBoard(testBoard);
    	
    }

    static void printBoard(int[][] board) {
        if (board == null) {
            System.out.println("The board is complete.");
            return;
        }
        for (int i = 0; i < 9; i++) {
            if (i == 3 || i == 6) {
                System.out.println("----+-----+----");
            }
            for (int j = 0; j < 9; j++) {
                if (j == 2 || j == 5) {
                    System.out.print(board[i][j] + " | ");
                } else {
                    System.out.print(board[i][j]);
                }
            }
            System.out.print("\n");
        }
    }

    static int[][] readBoard(String filename) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());
        } catch (IOException e) {
            return null;
        }
        int[][] board = new int[9][9];
        int val = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                try {
                    val = Integer.parseInt(Character.toString(lines.get(i).charAt(j)));
                } catch (Exception e) {
                    val = 0;
                }
                board[i][j] = val;
                initialBoard[i][j] = val;
            }
        }
        return board;
    }

    static int[][] solve(int[][] board, int row, int column) {
        if (reject(board, row, column))  {
        	return null;
        }
        if (isFullSolution(board)) {
        	return board;
        }
        int[][] attempt = extend(board);
        row = previousRowX;
        column = previousColY;
        while (attempt != null) {
            int[][] solution = solve(attempt, row, column);
            if (solution != null) return solution;
            attempt = next(attempt, row, column);
        }
        return null;
    }

    public static void main(String[] args) {
        if (args[0].equals("-t")) {
            testIsFullSolution();
            //testReject();
            testExtend();
            testNext();
        } else {
            int[][] board = readBoard(args[0]);
            printBoard(solve(board, -1, -1));
        }
    }
}
