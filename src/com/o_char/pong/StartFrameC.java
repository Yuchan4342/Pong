package com.o_char.pong;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

// PongClient向けスタート画面: 自分の名前とServer(親機)のホスト名/IPアドレスを指定する。
public class StartFrameC extends StartFrame implements ActionListener {
  PongClient pongClient;
  JPanel p1;
  JPanel p2;
  JPanel p3;

  /**
   * Constructor.
   *
   * @param npc 設定する PongClient.
   */
  public StartFrameC(PongClient npc) {
    super();
    this.pongClient = npc;

    // Setting labels
    this.upperLabel.setText("Input Your Name and Host Name:");
    this.label2.setText("Host Name:");

    // setting initial string of text field
    this.textField2.setText("localhost");

    // JPanel p3: setting 2 labels and 2 textfield
    p3 = new JPanel();
    p3.setLayout(new GridLayout(2, 2));
    p3.add(label1);
    p3.add(textField1);
    p3.add(label2);
    p3.add(textField2);

    // JPanel p2: setting p3 and log's scrollpane
    p2 = new JPanel();
    p2.setLayout(null);
    p2.add(p3);
    p2.add(scrollpane);
    p3.setBounds(0, 0, 400, 60);
    scrollpane.setBounds(0, 60, 400, 200);

    // JPanel p1: setting upper label, p2 and button
    p1 = new JPanel();
    p1.setLayout(null);
    p1.add(upperLabel);
    p1.add(p2);
    p1.add(btn);
    upperLabel.setBounds(120, 0, 400, 40);
    p2.setBounds(120, 30, 400, 260);
    btn.setBounds(290, 300, 60, 30);

    // container: setting p1
    this.container.add(p1, BorderLayout.CENTER);
  }

  /**
   * ボタンが押されたときの動作.
   */
  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);
    Object obj = e.getSource();
    if (obj == this.btn) {
      upperLabel.setText("Connecting...");
    }
  }
}
