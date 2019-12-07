package com.o_char.pong;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * 外部からの接続を受け付けるサーバーソケット.
 */
public final class SocketConnector implements Runnable {
  private PongServer ps;
  private ServerSocket serverSocket;
  private final int maxSocketNumber;
  /**
   * 最大のクライアントソケット数.
   */
  private int socketNumber;
  /**
   * タイムアウト.
   */
  private final int timeOut = 1000;

  // 受信中かどうか
  private boolean isReceivedNow;

  // 終了要求があったか
  private boolean isTermination;

  private SocketConnector(PongServer nps, int number) {
    this.ps = nps;
    this.maxSocketNumber = number;
    this.socketNumber = 0;
  }

  /**
   * 新しい SocketConnector を生成して返す.
   *
   * @param ps         PongServer.
   * @param portNumber ポート番号.
   * @param n          最大のソケット数.
   * @return 生成したソケット数.
   * @throws IOException ServerSocket を開く時に I/O エラーが起こった場合.
   */
  public static SocketConnector createConnector(PongServer ps, int portNumber, int n) throws IOException {
    SocketConnector sc = new SocketConnector(ps, n);
    sc.initialize(portNumber);
    return sc;
  }

  /**
   * 実行する. 外部からの接続を待機する.
   */
  public void run() {
    // 終了要求がない間
    while (!this.isTermination()) {
      // 新たな接続を受信する
      Socket socket = this.acceptConnection();

      // 接続受信していない場合は再受信
      if (socket == null) {
        continue;
      }

      System.out.println("Connection accepted: " + socket);
      this.ps.acceptSocket(socket);
    }

    // サーバーソケットを閉じる.
    try {
      System.out.println("Closing server socket: " + this.serverSocket);
      this.serverSocket.close();
    } catch (IOException ioe) {
      // Do Nothing.
    } finally {
      this.serverSocket = null;
    }
  }

  // 受信中かどうか取得
  public synchronized boolean isReceivedNow() {
    return this.isReceivedNow;
  }

  // 受信中かどうかの設定
  public synchronized void setReceivedNow(boolean b) {
    this.isReceivedNow = b;
  }

  public synchronized int getNumberOfSocket() {
    return this.socketNumber;
  }

  /**
   * socketNumber を増減させる.
   * @param n 増減させる数. 1 または -1 が入る.
   */
  public synchronized void transNumberOfSocket(int n) {
    if (n == -1) {
      this.socketNumber--;
      this.setReceivedNow(this.socketNumber > 0);
    } else if (n == 1) {
      this.socketNumber++;
    }
  }

  public synchronized void terminate() {
    this.isTermination = true;
  }

  public synchronized boolean isTermination() {
    return isTermination;
  }

  /**
   * サーバーソケットを初期化する.
   *
   * @param portNumber 設定するポート番号.
   * @throws IOException ServerSocket を開く時に I/O エラーが起こった場合.
   */
  public void initialize(int portNumber) throws IOException {
    this.serverSocket = new ServerSocket(portNumber);
    this.serverSocket.setSoTimeout(this.timeOut);
    System.out.println("Started: " + this.serverSocket);
  }

  private Socket acceptConnection() {
    Socket connectingSocket = null;

    try {
      connectingSocket = this.serverSocket.accept(); // コネクション設定要求を待つ
    } catch (SocketTimeoutException toe) {
      // Do Nothing.
    } catch (IOException ioe) {
      System.err.println("ソケット受信に異常");
    }

    if ((connectingSocket != null) && (this.getNumberOfSocket() >= this.maxSocketNumber)) {
      System.err.println("Cannot connect: Connecting socket is Full.");
      connectingSocket = null;
    }
    return connectingSocket;
  }
}
