import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Random;

import static java.awt.event.KeyEvent.*;

public class panel extends JPanel implements ActionListener {
    //declaring variables
      static int width=1200;
      static int height=600;
      //grid unit size
      static int unit=50;
      //total units in grid
      int totalunits=(width*height)/unit;





      int score;
      //food x and y coordinates
      int fx,fy;
      //initial length and directions
      int length=3;
      char dir='R';
      //true if game is running else false
      Boolean flag=false;

      //for Spawning food randomly
      Random random;
      Timer timer;
      static int delay=160;
      //create snake as array
      int xsnake[]=new int[totalunits];
      int ysnake[]=new int[totalunits];



      panel(){
          this.setPreferredSize(new Dimension(width,height));
          this.setBackground(Color.BLACK);
          this.addKeyListener(new Mykey());
          //enables keyboard input to the application
          this.setFocusable(true);
          random=new Random();

          gameStart();



      }

      public void gameStart(){
          flag=true;
          spawnFood();
          //timer to check on game static in each 160 ms
          timer=new Timer(delay,this);
          timer.start();
      }

      public void spawnFood(){
          //random int from range(0,1200) and which is multiple by 50
          fx=random.nextInt((int) width/unit)*unit;
          //random int from(1,600) and also multiple by 50
          fy=random.nextInt((int) height/unit)*unit;

      }
      //it implicitly called when panel is created
      //help in drawing the graphics of snake ,food,score,game over screen
      public void paintComponent(Graphics graphic){
          //super is parent class,call the parent function not local,it make differnce of that shape from background
          //paintcomponent is inbuilt method in jpanel class and bind it to local paintcomponent
          super.paintComponent(graphic);
          draw(graphic);

      }

      public void draw(Graphics graphic){
          if(flag){
              //draw food particle
              graphic.setColor(Color.white);
              graphic.fillOval(fx,fy,unit,unit);


              //to spawn the snake body
              for(int i=0;i<length;i++){
                   if(i==0){
                       graphic.setColor(Color.RED);
                       graphic.fillOval(xsnake[0],ysnake[0], unit,unit) ;

                   }
                   else{
                       graphic.setColor(Color.GREEN);
                       graphic.fillOval(xsnake[i],ysnake[i],unit,unit);

                   }
              }
              //for score disply
              graphic.setColor(Color.CYAN);
              graphic.setFont(new Font("Comic sans",Font.BOLD,40));
              //extract font specifications from computer by using awt function(fontMatrics class)
              FontMetrics fme=getFontMetrics(graphic.getFont());
              graphic.drawString("Score: "+score,(width- fme.stringWidth("score: "+score))/2,graphic.getFont().getSize());
          }
          else{
              gameOver(graphic);
          }
      }
      public void gameOver(Graphics graphic){
          //for "score" disply
          graphic.setColor(Color.CYAN);
          graphic.setFont(new Font("Comic sans",Font.BOLD,40));
          //extract font specifications from computer by using awt function(fontMatrics class)
          FontMetrics fme=getFontMetrics(graphic.getFont());
          graphic.drawString("Score: "+score,(width- fme.stringWidth("score: "+score))/2,height/3);


          //game over
          graphic.setColor(Color.RED);
          graphic.setFont(new Font("Comic sans",Font.BOLD,70));
          //extract font specifications from computer by using awt function(fontMatrics class)
          FontMetrics fme1=getFontMetrics(graphic.getFont());
          graphic.drawString("GAME OVER",(width- fme1.stringWidth("GAME OVER"))/2,height/2);


          //reply prompt disply
          graphic.setColor(Color.GREEN);
          graphic.setFont(new Font("Comic sans",Font.BOLD,40));
          //extract font specifications from computer by using awt function(fontMatrics class)
          FontMetrics fme2=getFontMetrics(graphic.getFont());
          graphic.drawString("press R to reply",(width- fme2.stringWidth("press R to reply"))/2,(height/2)+100);
      }


      public void move(){
          //for all body parts
          for(int i=length;i>0;i--){
              xsnake[i]=xsnake[i-1];
              ysnake[i]=ysnake[i-1];
          }

          //for head part
          switch (dir){
              case 'R':
                  xsnake[0]=xsnake[0]+unit;
                  break;
              case 'L':
                  xsnake[0]=xsnake[0]-unit;
                  break;
              case 'U':
                  ysnake[0]=ysnake[0]-unit;
                  break;
              case 'D':
                  ysnake[0]=ysnake[0]+unit;
                  break;

          }
      }

      void check(){
          //check if head hits body
          for(int i=length;i>0;i--){
              if(xsnake[0]==xsnake[i] && ysnake[0]==ysnake[i]){
                  flag=false;
              }
          }

          //check snake hits wall
           if(xsnake[0]<0){
               flag=false;
           }
           else if(xsnake[0]>width){
               flag=false;
           }
           else if(ysnake[0]<0){
               flag=false;
           }
           else if(ysnake[0]>height){
               flag=false;
           }


           if(flag==false){
               timer.stop();
           }
      }


      public void foodeaten(){
          if(xsnake[0]==fx && ysnake[0]==fy){
              length++;
              score++;
              spawnFood();
          }

      }



    public class Mykey extends KeyAdapter{
          public void keyPressed(KeyEvent k){
              switch ((k.getKeyCode())){
                  case VK_RIGHT :
                      if(dir !='L'){
                          dir='R';
                      }
                      break;
                  case VK_LEFT :
                      if(dir !='R'){
                          dir='L';
                      }
                      break;
                  case VK_UP :
                      if(dir !='D'){
                          dir='U';
                      }
                      break;
                  case VK_DOWN :
                      if(dir !='U'){
                          dir='D';
                      }
                      break;
                  case VK_R :
                      if(!flag){
                          score=0;
                          length=3;
                          dir='R';

                          //fill snake arrays with 0
                          Arrays.fill(xsnake,0);
                          Arrays.fill(ysnake,0);
                          gameStart();
                      }
                      break;
              }

          }

    }
    //for update game as frames per time
    public void actionPerformed(ActionEvent e){
        if(flag){
            move();
            foodeaten();
            check();
        }

        //repaint is a jpanel fuction which explicitly called the paincomponent function
        repaint();
    }
}
