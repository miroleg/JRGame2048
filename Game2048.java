package com.javarush.games.game2048;

import com.javarush.engine.cell.*;


public class Game2048 extends Game {
    
    private static final int SIDE = 4;
    private  int[][] gameField = new int[SIDE][SIDE];
    private  int[][] tempF = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score = 0; 
    
    
    public void  initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }
    
    private void createGame(){
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
        getMaxTileValue();
    }
    
    private void drawScene(){
        for(int x = 0; x < SIDE; x++)
            for(int y = 0; y < SIDE; y++)
                setCellColoredNumber(x, y, gameField[y][x]);
    }
    
    private boolean canUserMove(){
       
        for (int i = 0; i < SIDE; i++)
            for (int j = 0; j < SIDE; j++)
                if (gameField[i][j] == 0) return true;
                
        int i = 0;
        while (i < SIDE)   {
            for (int j = 0; j < SIDE-1; j++)
              if (gameField[i][j] == gameField[i][j+1]) 
                    return true;
            i++;        
        }
        
        int j = 0;
        while (j < SIDE)   {
            for (i = 0; i < SIDE-1; i++)
              if (gameField[i][j] == gameField[i+1][j]) 
                    return true;
            j++;        
        }
        
        return false;
    }
    
    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.NONE, "ПОБЕДА !", Color.RED, 60);
    }
    
    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.NONE, "проиграли, однако", Color.BLUE, 40);
    }
        
    
    private void createNewNumber(){
       if (getMaxTileValue() == 2048)  win();     
       int x;
       int y;
       int num;
       while (true){
            x = getRandomNumber(SIDE);
            y = getRandomNumber(SIDE);
            if (gameField[y][x] == 0){
               if (getRandomNumber(10) == 9)  gameField[y][x] = 4;
               else gameField[y][x] = 2;
            break;   
            }
        
       }
       
    }
    private Color getColorByValue(int value) {
        switch (value){
            case 0: return Color.WHITE;
            case 2:  return Color.PINK;
            case 4:  return Color.PURPLE;
            case 8:  return Color.BLUE;
            case 16:  return Color.LIGHTBLUE;
            case 32:  return Color.DARKGREEN;
            case 64:  return Color.GREEN;
            case 128:  return Color.ORANGE;
            case 256:  return Color.SALMON;
            case 512:  return Color.DARKORANGE;
            case 1024:  return Color.FIREBRICK;
            case 2048:  return Color.CRIMSON;
        }
        return Color.WHITE;
       
    }
    
    
    private void setCellColoredNumber(int x, int y, int value) {
        if (value == 0)
            setCellValueEx(x, y, getColorByValue(value), "");
        else
            setCellValueEx(x, y, getColorByValue(value), String.valueOf(value));

        
    }
    
    private boolean compressRow(int[] r) {
        boolean rez = false;
        int t;
        for (int i = 0; i < r.length; i++)
	    for (int j = i + 1; j < r.length; j++)
		    if (r[i] == 0 && r[j] > 0) {
			    t = r[i];
			    r[i] = r[j];
			    r[j] = t;
			    rez = true;
		    }
        return rez;
    }
    
    private boolean mergeRow(int[] r) {
        boolean rez = false;
        if (r.length >1) {
            for (int i = 0; i < r.length-1; i++){
    	        int j = i + 1;
    		    if (r[i] > 0 && r[j] > 0 && r[i] == r[j]) {
    			    r[i] += r[j];
    			    r[j] = 0;
    			    rez = true;
    			    score += r[i];
    			    setScore(score);
    		    }
            }
        }    
        return rez;
    }
    
    private void rotateClockwise() { // поворот матрицы
        int n = SIDE;
        for (int i = 0; i < n/2; i++) {
            for (int j = i; j < n-i-1; j++) {
                int tmp = gameField[i][j];
                tempF[i][j]=gameField[n-j-1][i];
                tempF[n-j-1][i]=gameField[n-i-1][n-j-1];
                tempF[n-i-1][n-j-1]=gameField[j][n-i-1];
                tempF[j][n-i-1]=tmp;
            }
        }
        gameField = tempF;
    }
        
    private  int getMaxTileValue(){
        
        int max = gameField[0][0];
        
        for (int i = 0; i < SIDE; i++)
            for (int j = 0; j < SIDE; j++)
                if (gameField[i][j] > max) 
                    max = gameField[i][j];
        return max;
    }    

    private void moveLeft(){
        boolean change = false;
        for (int i = 0; i < SIDE; i++) {
            if( compressRow( gameField[i] )) change = true;
            if( mergeRow( gameField[i] )) change = true;
            if( compressRow( gameField[i] )) change = true;
        }
        if( change ) createNewNumber();
      
    }
   
    
    private void moveRight(){
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();

        
    }
    
    private void moveUp(){
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();

    }
    
    private void moveDown(){
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();

        
    }
    
    @Override
    public  void onKeyPress(Key key) {
        if(key == Key.SPACE){
            isGameStopped = false;
            createGame();
            drawScene();
            score = 0;
            setScore(score);
            return;
        }
        if (canUserMove()) {
            if (isGameStopped == false) {
                if (key == Key.RIGHT) {
                    moveRight();
                    drawScene();
                }             
                if (key == Key.LEFT) {
                    moveLeft();
                    drawScene();
                }      
                if (key == Key.UP) {
                    moveUp();
                    drawScene();
                } 
                if (key == Key.DOWN) {
                    moveDown();
                    drawScene();
                }
            } 
        } 
        else gameOver();                      
    }

}