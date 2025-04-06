package br.com.dio.model;

import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Generator {
    private static final int SIZE = 9;
    private String[][] board = new String[SIZE][SIZE];
    private boolean[][] fixed = new boolean[SIZE][SIZE];
    private Random random = new Random();

    public Generator(Integer level) {
        // Gera um Sudoku válido completo
        generateBoard();        

        //Verificar se é válido
        // for (int i = 0; i < 9; i++) {
        //     for (int j = 0; j < 9; j++) {
        //         System.out.print((board[i][j].isEmpty() ? "." : board[i][j]) + " ");
        //     }
        //     System.out.println();
        // }
        markFixedNumbers(getDifficulty(level));  // Marca alguns como fixo
        //System.out.println(getBoardAsString());
    }

    //retorna a dificuldade
    private Integer getDifficulty(Integer level) {
        if (level == 0) {
            return 40;
        } else if (level == 1) {
            return 30;
        } else if (level == 2) {
            return 20;
        }
        return 10;
    }

    // Gera o tabuleiro completamente preenchido
    private void generateBoard() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = ""; // Limpa tudo
        solve(0, 0); // Preenche o tabuleiro
    }

    // Algoritmo de backtracking
    private boolean solve(int row, int col) {
        if (row == SIZE) return true;

        int nextRow = (col == SIZE - 1) ? row + 1 : row;
        int nextCol = (col == SIZE - 1) ? 0 : col + 1;

        if (!board[row][col].isEmpty())
            return solve(nextRow, nextCol);

        int[] numbers = getShuffledNumbers();
        for (int num : numbers) {
            if (isValid(row, col, String.valueOf(num))) {
                board[row][col] = String.valueOf(num);
                if (solve(nextRow, nextCol)) return true;
                board[row][col] = ""; // Backtrack
            }
        }
        return false;
    }

    // Verifica se o número é válido na posição
    private boolean isValid(int row, int col, String num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i].equals(num) || board[i][col].equals(num))
                return false;
        }

        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[boxRow + i][boxCol + j].equals(num))
                    return false;

        return true;
    }

    // Embaralha os números de 1 a 9
    private int[] getShuffledNumbers() {
        int[] numbers = new int[SIZE];
        for (int i = 0; i < SIZE; i++) numbers[i] = i + 1;
        for (int i = SIZE - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }
        return numbers;
    }
      
    // Marca alguns como fixos
    private void markFixedNumbers(int count) {
        int removed = 0;
        while (removed < count) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if ((!fixed[row][col]) && (!board[row][col].isEmpty())) {
                fixed[row][col] = true;
                //board[row][col] = "";
                removed++;
            }
        }
        /*for (int row = 0; row < SIZE; row++)
            for (int col = 0; col < SIZE; col++)
                fixed[row][col] = !board[row][col].isEmpty();*/
    }

    // Exporta o tabuleiro no formato "col,row;valor,fixed"
    public String getBoardAsString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String value = board[row][col].isEmpty() ? "0" : board[row][col];
                sb.append(col).append(",").append(row).append(";")
                  .append(value).append(",").append(fixed[row][col]).append(" ");
            }
        }
        return sb.toString().trim();
    }

    // Mapeia como "0,0" → "5,true"
    public Map<String, String> getBoardAsMap() {
        String[] list = getBoardAsString().split(" ");
        return Stream.of(list)
                .collect(Collectors.toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1]
                ));
    }

    // Teste
    public static void main(String[] args) {
        Generator sudoku = new Generator(1);
        System.out.println(sudoku.getBoardAsString());        
    }
}

