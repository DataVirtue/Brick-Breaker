/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.brickbreaker2;

import com.mycompany.brickbreaker2.BlockGrid.Block;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author aarya
 */
public class GamePlay extends JPanel implements KeyListener, ActionListener {

    Paddle paddle;
    Ball ball;
    BlockGrid blockgrid;
    boolean play;
    int size;
    int score;
    Timer timer;

    public GamePlay() {
        timer = new Timer(2, this);
        startGame();
    }

    private void startGame() {
        play = true;
        paddle = new Paddle(100, 10);
        ball = new Ball(20, 20, 10);

        ball.setDirX(1);
        ball.setDirY(1);
        
        score= 0;
        size = 32;

        blockgrid = new BlockGrid(100, 100, this.size);

        setBackground(Color.BLUE); // Set the panel background color
        setLayout(null);
        paddle.setPosition(200, 400);

        this.add(paddle);

        this.add(ball);

        this.add(blockgrid);

        timer.start();

        addKeyListener(this); // Add the key listener to the frame

        setVisible(true);

        setFocusable(true); // set focusable to true to listen for actions 
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // score display
        g.setColor(Color.WHITE);
        g2d.setFont(new Font("Verdana", 500, 30));
        g2d.drawString("Score: " + this.score, 300, 50);

        // blockgrid painting 
        for (Block rect : blockgrid.blocks) {
            if (rect.isVisible == false) {
                continue;
            }

            g.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(3));
            g.drawRect(blockgrid.getX() + rect.curr_x, blockgrid.getY() + rect.curr_y, blockgrid.blockWidth, blockgrid.blockHeight);

            g.setColor(Color.RED);
            g.fillRect(blockgrid.getX() + rect.curr_x, blockgrid.getY() + rect.curr_y, blockgrid.blockWidth, blockgrid.blockHeight);

        }
        //game over screen
        if (!play) {
            Color c = new Color(0, 0, 0, 200);
            g2d.setColor(c);

            g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

            g2d.setColor(Color.yellow);
            g2d.setFont(new Font("Verdana", 500, 50));
            g2d.drawString("Game Over", 150, 100);
            g2d.setFont(new Font("Verdana", 500, 30));
            g2d.drawString("Score: " + this.score, 150, 150);
            g2d.drawString("Press Enter To Restart", 150, 200);

        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        
        if (!play) {
            return;
        }
        
        // avoid paddle out of bounds
        if (paddle.getX() >= this.getWidth() - paddle.width || paddle.getX() < 0) {
            if (paddle.getX() > this.getWidth() - paddle.width) {
                paddle.setPosition(this.getWidth() - paddle.width, paddle.getY());
            }

            if (paddle.getX() < 0) {
                paddle.setPosition(0, paddle.getY());
            }

        }

        // check if ball is inside the grid
        for (Block rect : blockgrid.blocks) {
            if (rect.isVisible == true && ball.rect.intersects(new Rectangle(blockgrid.getX() + rect.curr_x, blockgrid.getY() + rect.curr_y, blockgrid.blockWidth, blockgrid.blockHeight))) {
                ball.setDirX(-ball.getDirX());
                ball.setDirY(-ball.getDirY());
                blockgrid.disableBlock(rect);
                score += 10;
                System.out.println();
                repaint();
            }
        }

        //ball hits the paddle
        if (this.paddle.rect.intersects(ball.rect)) //            ball.setDirX(-ball.getDirX());
        {
            ball.setDirY(-ball.getDirY());
        }

        // ball goes out of bounds throught width
        if (ball.getX() >= this.getWidth() - 10 || ball.getX() <= 10) {
            ball.setDirX(-ball.getDirX());
        }

        // prevent ball out of bound from top
        if (ball.getY() <= 10) {
            ball.setDirY(-ball.getDirY());
        }

        this.ball.setPosition(ball.getX() + ball.getDirX(), ball.getY() + ball.getDirY());

        //end game
        if (blockgrid.getBlocksLeft() == 0 || ball.getY() > this.getHeight()) {
            play = false;
            score = 10 * (size - blockgrid.getBlocksLeft());
            repaint();

        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyChar() == 'a') {
            this.paddle.setPosition(paddle.getX() - 10, paddle.getY());
        }

        if (e.getKeyChar() == 'd') {
            this.paddle.setPosition(paddle.getX() + 10, paddle.getY());
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

            this.removeAll();
            startGame();
            validate();

            repaint();

            ball.repaint();
            paddle.repaint();
        }
        
        //for testing only 
//        if (e.getKeyChar() == 'l') {
//            play = false;
//            repaint();
//        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
