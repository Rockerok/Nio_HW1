package ru.gb.storage.client;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client{
    private final static ExecutorService THREAD_POOL = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        try {
            new Client().start();
        } finally {
            THREAD_POOL.shutdown();
        }
    }

    public void start() {
         THREAD_POOL.execute(() -> {
                System.out.println("New client started ");
                Scanner scanner = new Scanner(System.in);
                try {
                    SocketChannel channel = SocketChannel.open(new InetSocketAddress("localhost", 9000));
                    while (true) {
                        String messageOut=scanner.next();
                        channel.write(ByteBuffer.wrap(String.format(
                                messageOut
                        ).getBytes()));
                        ByteBuffer byteBuffer = ByteBuffer.allocate(256);
                        channel.read(byteBuffer);
                        String messageIn = new String(byteBuffer.array());
                        System.out.println(messageIn);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                scanner.close();
            });
    }

}
