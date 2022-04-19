package ru.gb.storage.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
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
                        String messageOut=scanner.nextLine();
                        channel.write(ByteBuffer.wrap(String.format(
                                messageOut
                        ).getBytes()));
                        ByteBuffer byteBuffer2 = ByteBuffer.allocate(256);
                        channel.read(byteBuffer2);
                        String messageIn = new String(byteBuffer2.array());
                        System.out.println(messageIn);
                        byteBuffer2.clear();
                        byteBuffer2.flip();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                scanner.close();
            });
    }

}
