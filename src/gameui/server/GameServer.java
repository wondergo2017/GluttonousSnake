package gameui.server;


import core.GameCore;
import core.event.GameEvents;
import gameui.GameUIConstants;
import gameui.IData;
import gameui.event.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Vector;

import static gameui.GameUIConstants.*;
import static gameui.GameUIConstants.RANK_NUM;

public class GameServer {
    /**
     * this main is for testing,
     * actually the server needs no ui,
     * but for the convenience of closing the server,
     * server is wrapped in a frame to shut down the server;
     * @param args for port number
     */
    public static void main(String args[]) {
        class MyFrame extends JFrame{
            MyFrame(){
                JButton jButton=new JButton("exit");
                add("Center",jButton);
                pack();
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
                addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        super.windowClosed(e);
                    }
                });
                try {
                    if(args.length>1)
                    {
                        try{
                            int _iPort=Integer.parseInt(args[1]);
                            GameServer gameServer = new GameServer(_iPort);

                        }catch (NumberFormatException eNF)
                        {
                            System.out.println(eNF);
                            JOptionPane.showMessageDialog(null,"Wrong in Port!");
                        }
                    }
                    else{
                        GameServer gameServer = new GameServer();
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
        MyFrame myFrame= new MyFrame();
    }

    //network
    private int iPort =PORT_INIT;
    public int getiPort() {
        return iPort;
    }
    public void setiPort(int iPort) {
        this.iPort = iPort;
    }
    ServerSocket serverSocket;
    Vector<Socket> sockets = new Vector<Socket>();
    IData iReadyNum = new IData();

    //gameCore
    GameCore gameCore;
    Timer timePainter;
    boolean isPause=false;
    /**
     * game speed, actual speed is based one secpertime ,see more in gameuiconstants;
     */
    private int speed=GameUIConstants.SPEED_INIT;
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {

        timePainter.setDelay(SPEED_TIME/speed);
        this.speed = speed;
    }
    Vector<Integer> ranks=new Vector<>();

    //constructor
    public GameServer() throws IOException{
        this(PORT_INIT);
    }
    public GameServer(int _iPort) throws IOException {
        //1 wait for clients to connect
        //2 make all the threads are already
        //3 send mes to tell clients to start the game ,and send initialized battleground,speed and so on;
        //4 start the game timer, action per time period till the end

        //1 wait for clients to connect
        serverSocket = new ServerSocket(iPort);
        sockets.add(serverSocket.accept());
        sockets.add(serverSocket.accept());
        GameServerThread thread1 = new GameServerThread(sockets.elementAt(0), 0, iReadyNum);
        GameServerThread thread2 = new GameServerThread(sockets.elementAt(1), 1, iReadyNum);
        thread1.start();
        thread2.start();

        //2 make all the threads are already
        while (true) {
            synchronized (iReadyNum) {
                try {
                    if (iReadyNum.iData >= 2) break;
                    else iReadyNum.wait();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }

        //start game
        System.out.println("gameStart!");
        gameCore = new GameCore(GAME_M_INIT, GAME_N_INIT);
        gameCore.start();
        //3 send mes to tell clients to start the game ,and send initialized battleground,speed and so on;
        for (int i = 0; i < sockets.size(); i++) {
            try{
                ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(sockets.elementAt(i).getOutputStream()));
                oos.writeObject(new GameStartGameEvent(gameCore.getM(),gameCore.getN(),i));
                oos.writeObject(new GameMapEvent(gameCore.bg.wallVec,gameCore.bg.holeVec,gameCore.bg.foodVec,gameCore.snakes));
                oos.writeObject(new GameSpeedEvent(speed));
                oos.flush();
            }catch (IOException eIO)
            {
                System.out.println(eIO);
            }
        }

        //4 start the game timer, action per time period till the end
        ActionListener actionTimer=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //action per timeperiod
                gameCore.snakes.elementAt(0).setNextDir(thread1.nextDir);
                gameCore.snakes.elementAt(1).setNextDir(thread2.nextDir);
                GameEvents gameEvents= gameCore.actOnePeriod();
                if(gameCore.isEnd())
                ranks=getRankings(gameCore.getBestScores());
                for (Socket socket : sockets) {
                    try{
                        ObjectOutputStream oo = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                        if(gameCore.isEnd()) {
                            System.out.println("End!!!!!");
                            oo.writeObject(new GameOverEvent(gameCore.getWinPlayer(),ranks));
                        }
                        else {
                            //wrap and send
                            oo.writeObject(new GameMapEvent(gameCore.bg.wallVec, gameCore.bg.holeVec, gameCore.bg.foodVec, gameCore.snakes));
                            oo.flush();
                            oo = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                            oo.writeObject(gameEvents);
                        }
                        oo.flush();
                    }catch (IOException eIO)
                    {
                        System.out.println(eIO);
                    }
                }
                if (gameCore.isEnd())
                {
                    timePainter.stop();
                }
            }
        };
        timePainter=new Timer(100,actionTimer);
        setSpeed(speed);
        timePainter.start();
    }

    /**
     * @param scoreNew is the newly best score, which will be newly added to the ranking list
     * @return rankinglist
     */
    //ranking
    Vector<Integer> getRankings(int scoreNew)
    {
        int scores=scoreNew;
        Vector<Integer> ranks=new Vector<Integer>();
        int index = -1;
        try {
            File file=new File("ranking");
            if(!file.exists()) {
                file.createNewFile();
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            Scanner scanner=new Scanner(fileInputStream);
            while(scanner.hasNextInt())
            {
                ranks.add(scanner.nextInt());
            }
            Collections.sort(ranks, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    if(o1>o2) return -1;
                    else if(o1<o2) return 1;
                    else return 0;
                }
            });

            for (int i = 0; i < ranks.size(); i++) {
                if (ranks.elementAt(i) < scores) {
                    index = i;
                    break;
                }
            }
            if(ranks.size()<RANK_NUM||index==-1)
            {
                ranks.add(scores);
                index=ranks.size()-1;
            }
            else {
                ranks.setElementAt(scores,index);
            }
            fileInputStream.close();

            FileOutputStream fos=new FileOutputStream(file);
            PrintStream ps=new PrintStream(fos);
            for (int i = 0; i < ranks.size(); i++) {
                if(i<RANK_NUM)
                {
                    ps.println(ranks.elementAt(i));
                }
            }
        }catch (FileNotFoundException eFNF)
        {
            System.out.println(eFNF);
        }catch (IOException eIO)
        {
            System.out.println(eIO);
        }
        return ranks;
    }

    //network
    /**
     * threads with respective sockets to serve the clients
     * for listening and receive message
     */
    class GameServerThread extends Thread {
        Socket socket;
        /**
         * to distinguish different thread
         */
        int nClientNum;
        /**
         * for sake of making all the threads ready ,see more in gameServer
         */
        IData iReadyNum;
        /**
         * like a buffer for control the 'nextdir' in the gamecore
         * only when the server action per timeperiod will the nextdir in gamecore be changed
         */
        int nextDir=0;

        GameServerThread(Socket _socket, int _nClientNum,IData _iReadyNum)
        {
            socket=_socket;
            nClientNum=_nClientNum;
            iReadyNum=_iReadyNum;
        }
        @Override
        public void run() {
            //receive message from clients
            while(true)
            {
                try {
                    ObjectInputStream ois=new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                    Object objHead=ois.readObject();

                    if(objHead.getClass().equals(GameWindowEvent.class))
                    {
                        GameWindowEvent windowEvent= (GameWindowEvent)objHead;
                        //1 GameStartEvent to start the game
                        if(windowEvent.strDepic.equals("start")){
                            synchronized (iReadyNum)
                            {
                                iReadyNum.iData++;
                                iReadyNum.notifyAll();
                            }
                        }
                        //2 Pause the game or continue the game
                        if(windowEvent.strDepic.equals("Pause"))
                        {
                            if(isPause)
                            {
                                timePainter.start();
                                isPause=false;
                                for (Socket socket : sockets) {
                                    ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                                    oos.writeObject(new GameWindowEvent("continue"));
                                    oos.flush();
                                }

                            }else {
                                timePainter.stop();
                                isPause=true;
                                for (Socket socket : sockets) {
                                    ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                                    oos.writeObject(new GameWindowEvent("pause"));
                                    oos.flush();
                                }
                            }
                        }
                    }
                    //3 set nextdir
                    if(objHead.getClass().equals(GameMoveEvent.class))
                    {
                        System.out.println("Server receive : GameMoveEvent");
                        GameMoveEvent MoveEvent= (GameMoveEvent)objHead;
                        nextDir=MoveEvent.nextDir;
                    }
                    //4 control speed
                    if(objHead.getClass().equals(GameSpeedControlEvent.class))
                    {
                        GameSpeedControlEvent gsce=(GameSpeedControlEvent)objHead;
                        int deltaspeed=gsce.isUp()?1:-1;
                        if(deltaspeed>0&&deltaspeed+speed>GameUIConstants.SPEED_MAX) continue;
                        if(deltaspeed<0&&deltaspeed+speed<GameUIConstants.SPEED_MIN) continue;
                        setSpeed(speed+ deltaspeed);
                        for (Socket socket : sockets) {
                            ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                            oos.writeObject(new GameSpeedEvent(speed));
                            oos.flush();
                        }
                    }
                    //5 chat
                    if(objHead.getClass().equals(GameChatEvent.class))
                    {
                       GameChatEvent gce=(GameChatEvent)objHead;
                        for (Socket socket : sockets) {
                            ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                            oos.writeObject(new GameChatEvent("Player "+nClientNum+":\n"+gce.getStrChat()));
                            oos.flush();
                        }
                    }
                }catch (IOException eIO)
                {
                    System.out.println(eIO);
                }catch (ClassNotFoundException eCNF)
                {
                    System.out.println(eCNF);
                }
            }
        }
    }

}
