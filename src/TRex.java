import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class TRex implements ActionListener, KeyListener {

    public static TRex dino;
    private Renderer renderer;
    private final int WIDTH = 800, HEIGHT = 600;       //dimensiune fereastra
    private ArrayList<Obiect> obstacle;  // array obstacole
    private Random random;
    private int moment=0,cadere=0;       //moment folosit pentru temporizare. cadere- coborarea dinozaurului pe OY
    private boolean Over,Play;           // determina starea jocului
    private int score=0;
    private int highScore=0;
    private Image background;
    private int which=0;         // alege o imagine pt rex
    private int actiune=0;       // determina urcarea/coborarea pe Oy, nu e utilizat eficient
    private Obiect dinozaur;
    private int cd=0;  // cadere amortizata
    private int nrSpace=0; // nr apasari space pt reincepere joc
    String[] imag= {"E:\\Work\\Java\\Joculet\\1.png",
            "E:\\Work\\Java\\Joculet\\2.png",
            "E:\\Work\\Java\\Joculet\\3.png",
            "E:\\Work\\Java\\Joculet\\4.png"
    };//imagini pt rex

    private TRex() {
        //alocare
        JFrame Fereastra = new JFrame();
        renderer = new Renderer();
        Fereastra.add(renderer);
        random = new Random();
        dinozaur=new Obiect(100,HEIGHT-125,90,90,imag[which]);
        obstacle = new ArrayList<Obiect>();
        obstacle.add(new Obiect(-100, HEIGHT - 110, 70, 70,"E:\\Work\\Java\\Joculet\\5.png"));
            background=new ImageIcon("E:\\Work\\Java\\Joculet\\pict1.jpg").getImage();
        Fereastra.setTitle("T-Rex Runner");
        Timer timer = new Timer(20, this);  //redesenare la 20 milisec
        Fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Fereastra.setSize(WIDTH, HEIGHT);
        Fereastra.addKeyListener(this);
        Fereastra.setResizable(false);
        Fereastra.setVisible(true);
        timer.start();

        addObstacle(true);
        addObstacle(true);
        //adaugare obstacole
    }

    private void addObstacle(boolean start) {
        int obs=random.nextInt(60);
        int widthC = 30 + random.nextInt(60); // latime variabila cactus
        int difObs=450+random.nextInt(650);// distanta dintre obstacole
        if(obs%7!=0)// densitatea cu care apare un ptero
        if(start)
            obstacle.add(new Obiect(WIDTH + widthC + obstacle.size() * difObs, HEIGHT - 110, widthC, 70,"E:\\Work\\Java\\Joculet\\5.png"));
            //adaugere cactus
        else {
            obstacle.add(new Obiect(obstacle.get(obstacle.size()-1).x+difObs, HEIGHT - 110, widthC,70,"E:\\Work\\Java\\Joculet\\5.png"));
        }
        else
        if(start)
            obstacle.add(new Obiect(WIDTH + widthC + obstacle.size() * difObs, HEIGHT - 175, 90, 70,"E:\\Work\\Java\\Joculet\\6.png"));
            //adaugare ptero
        else {
            obstacle.add(new Obiect(obstacle.get(obstacle.size()-1).x+difObs, HEIGHT - 175 , 90, 70,"E:\\Work\\Java\\Joculet\\6.png"));
        }
    }

    public void actionPerformed(ActionEvent e) {
        if(Play) {
            int spead = 17;  // viteza de deplasare obstacole
            for (int i = 0; i < obstacle.size(); i++) {
                Obiect aux = obstacle.get(i);
                aux.x -= spead;  // modificare pe axa ox la apelarea aP
            }
            for (int i = 0; i < obstacle.size() && !Over; i++) {
                Obiect aux = obstacle.get(i);
                if (aux.x + aux.widt < 0) {
                    obstacle.remove(aux);
                    addObstacle(false); // obstacolul dispare din cadru
                }
            }
            dinozaur = new Obiect(100, HEIGHT - 125, 90, 90, imag[which]);  //declarare
            moment++;
            if(moment%3==0)
                score=score+1;          // scor marit la 3 apelari
            if (((moment % 2) == 0) && (cadere < 18)) {
                cadere = cadere + 30; //30 viteza de cadere, mutarea pe axa OY
            }
            if (cadere < 18) {
                if (actiune == 0) {
                    dinozaur.y += cadere;
                } else {
                    dinozaur.y -= cadere;
                }
            } else {
                dinozaur.y = HEIGHT - 125;
            }
            if (dinozaur.y == HEIGHT - 125) {
                actiune = 0;
                if(which==0 || which==1) {    // dinozaur ridicat
                    which++;
                    if (which > 1) {
                        which = 0;
                    }
                }
                else
                if(which==2 || which==3){           //dinozaur aplecat
                    dinozaur.y = HEIGHT - 96;
                    which++;
                    if (which > 3) {
                        which = 2;
                    }
                }
            }
        }
            for(Obiect colum: obstacle){
                if(colum.intersects(dinozaur) ){
                    Over=true;
                    Play=false;     //incheiere joc
                   // dinozaur.x=colum.x-dinozaur.widt;
                }
            }
        renderer.repaint();
        }

    public void repaint(Graphics g) {
        g.drawImage(background,0,0,null);
        g.setColor(Color.gray);
        g.fillRect(0, HEIGHT-45, WIDTH, 5);    //sol
        if(!Over) {
            g.drawImage(dinozaur.imj,dinozaur.x,dinozaur.y,null);       // daca se intersecteaza de ceva, rex moare
        }

        for (Obiect colum : obstacle) {
                g.drawImage(colum.imj,colum.x,colum.y,colum.widt,colum.heigh,null);     // desenare obstacole
        }

        g.setColor(Color.white.darker() );
        g.setFont(new Font("Arial",1,25));
        if(!Play){
            g.drawString("Use Space to jump",280,100);
           /*
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {

            }
            */
        }
        g.setColor(Color.gray.brighter());
        g.setFont(new Font("Arial",1,20));
        if(!Over && Play){
            g.drawString("HI:"+String.valueOf(highScore)+" "+String.valueOf(score),WIDTH-110,40);  // desenare score si Hi

        }
        g.setColor(Color.white.darker().darker());
        g.setFont(new Font("Arial",1,100));
        if(Over) {
            g.drawString("Game Over", 120, HEIGHT / 2 - 50);        // desenare Game Over

/*
            long endTime = System.currentTimeMillis() + 2000;
           while (System.currentTimeMillis() < endTime) ;
            try {
                Thread.sleep(160);
            } catch (InterruptedException e) {
            }
*/
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_DOWN && dinozaur.y==HEIGHT-125){
            which=2;
         }//dinozaurul se lasa jos la apasarea down

        if(e.getKeyCode()==KeyEvent.VK_SPACE && dinozaur.y==HEIGHT-125 &&!Over &&(which==0 || which==1)){
            jump();
        }// saritura la apasare space
    }
    @Override
    public void keyReleased(KeyEvent e) {

        if(e.getKeyCode()==KeyEvent.VK_DOWN ){
            which=1;
        }//revenire dupa ce nu mai apasam down

        if(e.getKeyCode()==KeyEvent.VK_SPACE && Over){
            nrSpace++;
            actiune=1;
            if(nrSpace==2) {        //la 2 apasari de space jocul reincepe
                jump();
            }
        }
    }
    private void jump() {
        if(Over) {
            nrSpace=0;
            Over = false;
            dinozaur = new Obiect(100, HEIGHT-125, 90, 90,imag[which]);
            obstacle.clear(); //inlaturare obstacole
            addObstacle(true);
            addObstacle(true);
            if(score>highScore)
            highScore=score;  // calculare Highscore
            score=0;
            cadere=0;
        }
        if(!Play)
            Play=true;
        else if(!Over){
            if(cadere>0){
                cadere=0;
            }
            cadere-=cd;
            if(cd<100) {
                cd+=20;
                jump();
            }
            else{
               cd=0;
            }
        }
    }
    public static void main(String[] args) {
        dino = new TRex();
    }
}

