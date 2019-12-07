package com.o_char.pong;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/* 送信用クラス */
public class PongSender {
  private PongController pongController;
  private Socket socket;
  private BufferedWriter bfw;
  private int index;
  private PrintWriter out;

  private PongSender(PongController npc, Socket ns, BufferedWriter nbfw, int newIndex) {
    this.pongController = npc;
    this.socket = ns;
    this.bfw = nbfw;
    this.index = newIndex;
    this.out = new PrintWriter(this.bfw, true);
  }

  public static PongSender createSender(PongController pc, Socket socket, int index)
      throws IOException {
    OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
    BufferedWriter bfw = new BufferedWriter(osw); // データ送信用バッファの設定

    PongSender pongSender = new PongSender(pc, socket, bfw, index);
    if (pongSender.index != -1) {
      System.out.println("Complete setting : Sender[" + pongSender.index + "] = " + pongSender);
      System.out.println("Complete setting : Sending Buffered Writer[" + pongSender.index + "] = " + bfw);
    } else {
      System.out.println("Complete setting : Sender = " + pongSender);
      System.out.println("Complete setting : Sending Buffered Writer = " + bfw);
    }
    return pongSender;
  }

  public static PongSender createSender(PongController pc, Socket socket)
      throws IOException {
    return PongSender.createSender(pc, socket, -1);
  }

  /**
   * 文字列を送信する.
   *
   * @param string 送信する文字列.
   * @return 送信が成功したかどうか.
   */
  public boolean send(String string) {
    if (this.bfw == null) {
      return false;
    }

    System.out.println("Send: \"" + string + "\" to " + this.socket.getRemoteSocketAddress());
    // データを送信する.
    out.println(string);
    return true;
  }

  // 送信用バッファを閉じる
  public void terminate() {
    if (this.bfw == null) {
      return;
    }
    try {
      if (this.index == -1) {
        System.out.println("Closing : Sending Buffered Writer = " + this.bfw);
      } else {
        System.out.println("Closing : Sending Buffered Writer[" + index + "] = " + this.bfw);
      }
      this.bfw.close();
    } catch (IOException ioe) {
      // Do Nothing.
    } finally {
      this.bfw = null;
    }
  }
}
