import gameui.client.GameClient;
import gameui.server.GameServer;

import java.io.*;

public class Main {

    public static void main(String[] args) throws Exception
    {
        (new Thread(){
            @Override
            public void run() {
                try {
                    GameServer gameServer = new GameServer();
                }catch (IOException e)
                {
                    System.out.println(e);
                }
            }
        }).start();
        (new Thread(){
            @Override
            public void run() {
                try {
                    GameClient gameClient=new GameClient();
                }catch (IOException e)
                {
                    System.out.println(e);
                }
            }
        }).start();
        (new Thread(){
            @Override
            public void run() {
                try {
                    GameClient gameClient=new GameClient();
                }catch (IOException e)
                {
                    System.out.println(e);
                }
            }
        }).start();

    }
}
