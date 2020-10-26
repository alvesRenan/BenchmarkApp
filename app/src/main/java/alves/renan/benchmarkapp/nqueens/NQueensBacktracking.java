package alves.renan.benchmarkapp.nqueens;

public class NQueensBacktracking implements NQueens{

    public boolean canPlaceQueen(int r, int c, int[] x) {
        for (int i = 0; i < r; i++) {
            if (x[i] == c || (i - r) == (x[i] - c) ||(i - r) == (c - x[i]))
            {
                return false;
            }
        }
        return true;
    }

    public int placeNqueens(int r, int[] x) {
        int n = x.length;
        int count = 0;
        for (int c = 0; c < n; c++) {
            if (canPlaceQueen(r, c, x)) {
                x[r] = c;
                if (r == n - 1) {
                    count += 1;
                    return count;
                } else {
                    count += placeNqueens(r + 1, x);
                }
            }
        }
        return count;
    }

    public int callplaceNqueens(int n) {
        int[] x = new int[n];
        return placeNqueens(0, x);
    }
}
